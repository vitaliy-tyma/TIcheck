package com.luxoft.falcon.service;

import com.luxoft.falcon.config.*;
import com.luxoft.falcon.dao.DbConnectorBirt;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.ChecklistEntry;
import com.luxoft.falcon.model.Report;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


/**
 * Contains method to get data from BIRT database
 *
 * Data source: BIRT MySQL DB on requested Server:port and with credentials
 *
 * Input data: the name of the PON and iteration #
 * (comes from XML-file or from outer application/class via API)
 * Note: Market and region are not processed!
 *
 * Transformation: toString and getHTML by template - NOT IMPLEMENTED
 *
 * Output data: returns Requests Count and updates local Report object passed in arguments
 *
 * In case of exception returns text of error in report.logOfErrors
 */

//TODO - Logic of BIRT check must be such:
// 1) if PON name is full (i.e. found in one DB) - look only in 2010 or 2020 DB
// 2) if PON name is partial (i.e. found in both DBs) - look both on 2010 and 2020 - issue with regression check!!!

@Slf4j
public class ServiceAnalyseBirtMt extends Thread {
    private Checklist checklist;
    private Report report;
    private Boolean analyseRegression;

    private Connection con = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;


    private static MainConfig mainConfig = MainConfig.getInstance();
    private static Birt2010ConfigAndQuery birt2010ConfigAndQuery = Birt2010ConfigAndQuery.getInstance();
    private static Birt2020ConfigAndQuery birt2020ConfigAndQuery = Birt2020ConfigAndQuery.getInstance();
    private static BirtQueryToCheckGeneration birtQueryToCheckGeneration = BirtQueryToCheckGeneration.getInstance();

    private String queryLike = null;
    private String queryAccurate = null;
    private int requestsCount;


    public ServiceAnalyseBirtMt(Checklist checklist, Report report, Boolean analyseRegression) {
        this.checklist = checklist;
        this.report = report;
        this.analyseRegression = analyseRegression;
    }

    public void run() {
        processBirtChecklist(checklist, report, analyseRegression);
    }

    public List<ChecklistEntry> getSteps() {
        return report.getBirtSteps();
    }

    public int getRequestsCount() {
        return requestsCount;
    }


    public int processBirtChecklist(Checklist checklist, Report report, Boolean analyseRegression) {
        requestsCount = 0;
        log.info("**** in ServiceAnalyseBirtMt.processBirt() ****");

        try {

            con = DbConnectorBirt.connectDatabase(birt2010ConfigAndQuery);
//            .getJdbcUrl(),
//                    birt2010ConfigAndQuery.getJdbcLogin(),
//                    birt2010ConfigAndQuery.getJdbcPassword());

            /* CHECK GENERATION */
            boolean isGen2010 = checkGeneration(birtQueryToCheckGeneration.getG2010(), report);
            boolean isGen2020 = checkGeneration(birtQueryToCheckGeneration.getG2020(), report);

            List<ChecklistEntry> fillBirtErrors;// = new LinkedList<>();



            /* Iterate over BIRT2010*/
            if (isGen2010) {
                log.info("***************************** Look in Birt 2010 *****************");
                queryLike = birt2010ConfigAndQuery.getQueryLike();
                queryAccurate = birt2010ConfigAndQuery.getQueryIs();
                fillBirtErrors = analyseActual(checklist.getBirtSteps(), report, isGen2020?" (2010)":"");
                /* Store items in report*/
                report.addBirtSteps(fillBirtErrors);
            }

            /* Iterate over BIRT2020*/
            if (isGen2020) {
                log.info("***************************** Look in Birt 2020 *****************");
                queryLike = birt2020ConfigAndQuery.getQueryLike();
                queryAccurate = birt2020ConfigAndQuery.getQueryIs();
                fillBirtErrors = analyseActual(checklist.getBirtSteps(), report, isGen2010?" (2020)":"");
                /* Store items in report*/
                report.addBirtSteps(fillBirtErrors);
            }





                /* CHECK FOR REGRESSION */
                if (analyseRegression) {
                    analyseRegression(report);
                }

            /* Iterate over BIRTs is not processed*/
            if (!isGen2010 && !isGen2020) {
                List<ChecklistEntry> emptyBirtErrors = new LinkedList<>();
                for (String birtTestname : checklist.getBirtSteps()) {
                    emptyBirtErrors.add(
                            new ChecklistEntry(
                                    birtTestname,
                                    Boolean.TRUE,
                                    Boolean.FALSE,
                                    "Generation had not been detected",
                                    report.getName(),
                                    "Not found"));
                }
                report.setBirtSteps(emptyBirtErrors);
            }


            resultSet.close();
            pstmt.close();

            con.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            report.addLogOfErrors(e.getMessage());
        } finally {

            if (resultSet != null) {
                resultSet = null;
            }
            if (pstmt != null) {
                pstmt = null;
            }
            if (con != null) {
                con = null;
            }
        }

        log.info("****************************** PROCESSING BIRT of PON %s HAS BEEN FINISHED ******************",
                report.getName());

        return requestsCount;
    }


    private boolean checkGeneration(String queryToCheck ,Report report) throws SQLException {
        PreparedStatement pstmtChecker = con.prepareStatement(queryToCheck);
        pstmtChecker.setString(1, "%" + report.getName() + "%");
        pstmtChecker.setInt(2, report.getLimit());
        ResultSet resultSetChecker = pstmtChecker.executeQuery();
        requestsCount++;
        return resultSetChecker.isBeforeFirst();
    }





    /* THE MAIN ACTION IS BELOW!!!*/
    private List<ChecklistEntry> analyseActual(List<String> steps, Report report, String generation) throws SQLException {
        List<ChecklistEntry> fillBirtErrors = new LinkedList<>();


        if (report.getUseQueryLike()) {
            pstmt = con.prepareStatement(queryLike);
            pstmt.setString(1, "%" + report.getName() + "%R" + report.getIteration());
        } else {
            pstmt = con.prepareStatement(queryAccurate);
            pstmt.setString(1, report.getName() + "_R" + report.getIteration());
        }
        pstmt.setInt(3, report.getLimit());

        for (String errorToCheck : steps) {
            pstmt.setString(2, errorToCheck);

            String fullQuery =
                    pstmt.toString().replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", "");
            resultSet = pstmt.executeQuery();
            requestsCount++;

            log.debug(String.format("************************* Birt query has been executed (%s)", fullQuery));
            /*If response is empty = store only one item for the step with NOT FOUND description*/
            if (!resultSet.isBeforeFirst()) {
                fillBirtErrors.add(
                        new ChecklistEntry(
                                errorToCheck + generation,
                                Boolean.TRUE,
                                Boolean.FALSE,
                                fullQuery,
                                report.getName(),
                                "Not found"));
            } else {

                /* Check is it possible to aggregate all rows to one
                 * with simplified name of PON*/
                Boolean aggregateBirtSteps = Boolean.TRUE;
                String resultOfCheckFirstRow = null;
                Boolean firstRow = Boolean.TRUE;
                StringBuilder ponNamesAggregated = new StringBuilder();
                ChecklistEntry tempChecklistEntry =
                        new ChecklistEntry(errorToCheck + generation);
                while (resultSet.next()) {

                    String resultOfTest = resultSet.getString(mainConfig.getBIRT_TEST_RESULT_NAME());
                    String ponName = resultSet.getString(mainConfig.getBIRT_TASK_COL_NAME());
//                        String testName = resultSet.getString(mainConfig.getBIRT_TEST_COL_NAME());


                    if (firstRow) {
                        resultOfCheckFirstRow = resultOfTest;

                        tempChecklistEntry.setStepIsChecked(Boolean.TRUE);

                        tempChecklistEntry.setFullQuery(fullQuery);
//                        tempChecklistEntry.setFullNameOfPon(report.getName() + " (aggregated)");
                        tempChecklistEntry.setResultOfCheckText(resultOfTest);
                        if (resultOfTest.toUpperCase().equals("NOK")) {
                            tempChecklistEntry.setResultOfCheckIsNOK(Boolean.TRUE);
                        } else {
                            tempChecklistEntry.setResultOfCheckIsNOK(Boolean.FALSE);
                        }
                        firstRow = Boolean.FALSE;
                    }
                    if (resultOfTest.toUpperCase().equals(resultOfCheckFirstRow)) {
                        ponNamesAggregated.append("&nbsp;");
                        ponNamesAggregated.append(ponName);
                        ponNamesAggregated.append("&nbsp;\n");
                        continue;
                    }
                    aggregateBirtSteps = Boolean.FALSE;
                    break;
                }

                if (aggregateBirtSteps) {
                    /* It is possible to aggregate steps to one! */
                    tempChecklistEntry.setAggregatedNames(ponNamesAggregated.toString());
                    tempChecklistEntry.setFullNameOfPon(
                            "[Aggregated] " + report.getName() );
                    fillBirtErrors.add(tempChecklistEntry);
                } else {
                    /* Proceed with all results from the beginning - aggregation is not necessary! */
                    resultSet.beforeFirst();
                    while (resultSet.next()) {
                        String fullName = resultSet.getString(mainConfig.getBIRT_TASK_COL_NAME());
                        String testName = resultSet.getString(mainConfig.getBIRT_TEST_COL_NAME());
                        String resultOfTest = resultSet.getString(mainConfig.getBIRT_TEST_RESULT_NAME());

                        ChecklistEntry entry =
                                new ChecklistEntry(testName + generation);

                        entry.setStepIsChecked(Boolean.TRUE);
                        entry.setFullQuery(fullQuery);
                        entry.setFullNameOfPon(fullName);

                        /*Save result of check as string and as boolean value */
                        entry.setResultOfCheckText(resultOfTest);
                        if (resultOfTest.toUpperCase().equals("NOK")) {
                            entry.setResultOfCheckIsNOK(Boolean.TRUE);
                        } else {
                            entry.setResultOfCheckIsNOK(Boolean.FALSE);
                        }
                        /*Store all results */
                        fillBirtErrors.add(entry);

                        log.debug(
                                String.format(
                                "************************* BIRT resultSet item (%s) processed !!!!!!!!!!!!!!!!!!!!!!!!!!!!", fullName));
                    }
                }
            }
        }
        return fillBirtErrors;
    }


    /* Make regression analysis*/
    private void analyseRegression(Report report) throws SQLException {

        for (ChecklistEntry entry : report.getBirtSteps()) {

            if (entry.getResultOfCheckIsNOK()) {
                String nameToCheck;
                /*Split the name to two parts - first before name from request and second - the rest*/
                String[] restOfOriginalNameArray =
                        entry.getFullNameOfPon().split(report.getName(), 2);
                try {

                    if (restOfOriginalNameArray[1].contains("_")) {
                        String[] region = restOfOriginalNameArray[1].split("_");
                        /* Make the new name for request from the first part (before entered name),
                         * previous name, symbol "_" and the region*/
                        nameToCheck = restOfOriginalNameArray[0] +
                                report.getPrevName() + "_" + region[1];
                    } else {
                        nameToCheck = restOfOriginalNameArray[0] + report.getPrevName();
                    }

                } catch (Exception e) {
                    nameToCheck = report.getPrevName();
                    log.error(
                            String.format(
                            "Regression check - error while converting names (%s) - work with %s",
                            entry.toString(),
                            nameToCheck));
                    report.addLogOfErrors(
                            String.format(
                            "Regression check - error while converting names (%s)- work with %s. Error is [%s]",
                            entry.toString(),
                            nameToCheck,
                            e.getMessage()));
                }

                if (report.getUseQueryLikeForPrev()) {
                    pstmt = con.prepareStatement(queryLike);
                    pstmt.setString(1, "%" + nameToCheck.trim() + "%R" + report.getPrevIteration());
                } else {
                    pstmt = con.prepareStatement(queryAccurate);
                    pstmt.setString(1, nameToCheck.trim() + "_R" + report.getPrevIteration());
                }
                pstmt.setString(2, entry.getNameOfErrorToCheckFor());
                pstmt.setInt(3, report.getLimit());
                String fullQuery = pstmt.toString();
                entry.setFullQuery(entry.getFullQuery() +
                        "<br/><br/>\nRegression query:<br/>\n" +
                        fullQuery.replace("com.mysql.cj.jdbc.ClientPreparedStatement: ", ""));

                resultSet = pstmt.executeQuery();
                requestsCount++;

                log.debug(
                        String.format(
                        "************************* Birt query for regression check has been executed (%s)",
                        fullQuery));

                if (!resultSet.isBeforeFirst()) {
                    entry.setIsRegression("Not found");
                    entry.setFullNameOfRegressionPon(" No results for <br/>\n " + nameToCheck);
                }

                while (resultSet.next()) {
                    String resultPrevTest = resultSet.getString(mainConfig.getBIRT_TEST_RESULT_NAME());
                    String resultOriginalTest = entry.getResultOfCheckText();


                    if (resultOriginalTest.equals(resultPrevTest)) {
                        entry.setIsRegression("No");
                        entry.setFullNameOfRegressionPon(nameToCheck);
                    } else if (resultOriginalTest.toUpperCase().equals("NOK") &&
                               resultPrevTest.toUpperCase().equals("OK")) {
                        entry.setIsRegression("Yes");
                        entry.setFullNameOfRegressionPon(nameToCheck);
                    } else {
                        //Case that in previous PON test was skipped!
                        entry.setIsRegression("Yes (see comment)");
                        entry.setFullNameOfRegressionPon(
                                String.format(" %s [ with result of prev test %s]",
                                        nameToCheck,
                                        resultPrevTest));
                    }
                }
            }
        }
    }
}
