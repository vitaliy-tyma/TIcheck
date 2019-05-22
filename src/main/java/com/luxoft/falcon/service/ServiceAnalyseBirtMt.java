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
 * As an option - error list in string format (serialized by toString) - NOT IMPLEMENTED
 *
 * In case of exception returns text of error in report.logOfErrors
 */
@Slf4j
public class ServiceAnalyseBirtMt extends Thread {
    private Checklist checklist;
    private Report report;
    private Boolean analyseRegression;

    private Connection con = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;


    private static MainConfig mainConfig = MainConfig.getInstance();
    private Birt2010ConfigAndQuery birt2010ConfigAndQuery = Birt2010ConfigAndQuery.getInstance();
    private Birt2020ConfigAndQuery birt2020ConfigAndQuery = Birt2020ConfigAndQuery.getInstance();
    private BirtQueryToCheckGeneration birtQueryToCheckGeneration = BirtQueryToCheckGeneration.getInstance();

    private PreparedStatement pstmtChecker;
    private ResultSet resultSetChecker;

    private String queryLike = null;
    private String queryAccurate = null;
    private boolean isGenDefined = false;
    private int requestsCount;





    public ServiceAnalyseBirtMt(Checklist checklist, Report report, Boolean analyseRegression){
        this.checklist = checklist;
        this.report = report;
        this.analyseRegression = analyseRegression;
    }

    public void run(){
        processBirtChecklist(checklist, report, analyseRegression);
    }
    public List<ChecklistEntry> getSteps (){
        return report.getBirtSteps();
    }
    public int getRequestsCount(){
        return requestsCount;
    }


    public int processBirtChecklist(Checklist checklist, Report report, Boolean analyseRegression) {
        requestsCount = 0;
        log.debug("**** in ServiceAnalyseBirtMt.processBirt() ****");

        try {

            con = DbConnectorBirt.connectDatabase(
                    birt2010ConfigAndQuery.getJdbcUrl(),
                    birt2010ConfigAndQuery.getJdbcLogin(),
                    birt2010ConfigAndQuery.getJdbcPassword());

            /* CHECK GENERATION */
            isGenDefined = checkGeneration(report);
            requestsCount += 2;

            List<ChecklistEntry> fillBirtErrors;// = new LinkedList<>();


            /* Iterate over BIRT*/
            if (isGenDefined) {
                fillBirtErrors = analyseActual(checklist.getBirtSteps(), report);


                /* Store items in report*/
                report.setBirtSteps(fillBirtErrors);



                /* CHECK FOR REGRESSION */
                if (analyseRegression) {
                    analyseRegression(report);
                }
            } else {
                List<ChecklistEntry> emptyBirtErrors = new LinkedList<>();
                for (String birtTestname : checklist.getBirtSteps()){
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

        log.debug(String.format(
                "****************************** PROCESSING BIRT of PON {} HAS BEEN FINISHED ******************"),
                report.getName());

        return requestsCount;
    }


    private boolean checkGeneration(Report report) throws SQLException, ClassNotFoundException {
        isGenDefined = Boolean.FALSE;
        //2010
        String queryToCheck = birtQueryToCheckGeneration.getG2010();
        pstmtChecker = con.prepareStatement(queryToCheck);
        pstmtChecker.setString(1, "%" + report.getName() + "%");
        pstmtChecker.setInt(2, report.getLimit());
        resultSetChecker = pstmtChecker.executeQuery();
        requestsCount++;
        if (resultSetChecker.isBeforeFirst()) {
            log.debug("***************************** Use Birt 2010 *****************");
            queryLike = birt2010ConfigAndQuery.getQueryLike();
            queryAccurate = birt2010ConfigAndQuery.getQueryAccurate();
            isGenDefined = true;
        }
        resultSetChecker.close();
        pstmtChecker.close();
        con.close();


//2020
        con = DbConnectorBirt.connectDatabase(
                birt2020ConfigAndQuery.getJdbcUrl(),
                birt2020ConfigAndQuery.getJdbcLogin(),
                birt2020ConfigAndQuery.getJdbcPassword());
        queryToCheck = birtQueryToCheckGeneration.getG2020();
        pstmtChecker = con.prepareStatement(queryToCheck);
        pstmtChecker.setString(1, "%" + report.getName() + "%");
        pstmtChecker.setInt(2, report.getLimit());
        resultSetChecker = pstmtChecker.executeQuery();
        requestsCount++;
        if (resultSetChecker.isBeforeFirst()) {
            log.debug("***************************** Use Birt 2020 *****************");
            queryLike = birt2020ConfigAndQuery.getQueryLike();
            queryAccurate = birt2020ConfigAndQuery.getQueryAccurate();
            isGenDefined = true;
        }
        resultSetChecker.close();
        pstmtChecker.close();

        return isGenDefined;
    }


    private List<ChecklistEntry> analyseActual(List<String> steps, Report report) throws SQLException {
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
//            try {
                pstmt.setString(2, errorToCheck);


                String fullQuery = pstmt.toString().replace("com.mysql.cj.jdbc.ClientPreparedStatement: ","");
                resultSet = pstmt.executeQuery();
                requestsCount++;

                log.debug(String.format("************************* Birt query has been executed (%s)", fullQuery));
                /*If response is empty = store only one item for the step with NOT FOUND description*/
                if (!resultSet.isBeforeFirst()) {
                    fillBirtErrors.add(
                            new ChecklistEntry(
                                    errorToCheck,
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
                    ChecklistEntry tempChecklistEntry = new ChecklistEntry(errorToCheck);
                    while (resultSet.next()) {
                        String resultOfTest = resultSet.getString(mainConfig.getBIRT_TEST_RESULT_NAME());
                        String testName = resultSet.getString(mainConfig.getBIRT_TEST_COL_NAME());


                        if (firstRow) {
                            resultOfCheckFirstRow = resultOfTest;

                            tempChecklistEntry.setStepIsChecked(Boolean.TRUE);

                            tempChecklistEntry.setFullQuery(fullQuery);
                            tempChecklistEntry.setFullNameOfPon(report.getName());
                            tempChecklistEntry.setResultOfCheckText(resultOfTest);
                            if (resultOfTest.toUpperCase().equals("NOK")) {
                                tempChecklistEntry.setResultOfCheckIsNOK(Boolean.TRUE);
                            } else {
                                tempChecklistEntry.setResultOfCheckIsNOK(Boolean.FALSE);
                            }
                            firstRow = Boolean.FALSE;
                        }
                        if (resultOfTest.toUpperCase().equals(resultOfCheckFirstRow)) {
                            continue;
                        }
                        aggregateBirtSteps = Boolean.FALSE;
                        break;
                    }

                    if (aggregateBirtSteps) {
                        fillBirtErrors.add(tempChecklistEntry);
                    } else {
                        resultSet.beforeFirst();
                        while (resultSet.next()) {
                            String fullName = resultSet.getString(mainConfig.getBIRT_TASK_COL_NAME());
                            String testName = resultSet.getString(mainConfig.getBIRT_TEST_COL_NAME());
                            String resultOfTest = resultSet.getString(mainConfig.getBIRT_TEST_RESULT_NAME());

                            ChecklistEntry entry = new ChecklistEntry(testName);

                            entry.setStepIsChecked(Boolean.TRUE);
                            entry.setFullQuery(fullQuery);
                            entry.setFullNameOfPon(fullName);

                            /*Save result of check as string and as boolean value (may be deleted as non representative)*/
                            entry.setResultOfCheckText(resultOfTest);
                            if (resultOfTest.toUpperCase().equals("NOK")) {
                                entry.setResultOfCheckIsNOK(Boolean.TRUE);
                            } else {
                                entry.setResultOfCheckIsNOK(Boolean.FALSE);
                            }

                            /*Store all results */
                            fillBirtErrors.add(entry);

                            log.debug(String.format("************************* BIRT resultSet item (%s) processed !!!!!!!!!!!!!!!!!!!!!!!!!!!!", fullName));

                        }

                    }
                }


//            } catch (Exception e) {
//                log.error(e.getMessage());
//                report.addLogOfErrors(e.getMessage());
//            }
        }


        return fillBirtErrors;
    }

    /* Make regression analysis*/
    private void analyseRegression(Report report) throws SQLException {
//        try {
        for (ChecklistEntry entry : report.getBirtSteps()) {


            if (entry.getResultOfCheckIsNOK()) {
//TODO Doesn't work if name of PON is with wronge case of letters. Must use report.getName().toUpperCase() to avoid!
                String[] restOfOriginalNameArray = entry.getFullNameOfPon().split(report.getName(), 2);
                String nameToCheck;
                try {

                    if (restOfOriginalNameArray[1].contains("_")) {
                        String[] region = restOfOriginalNameArray[1].split("_");
                        nameToCheck = restOfOriginalNameArray[0] + report.getPrevName() + "_" + region[1];
                    } else {
                        nameToCheck = restOfOriginalNameArray[0] + report.getPrevName();
                    }

                } catch (Exception e) {
                    nameToCheck = report.getPrevName();
                    log.error(String.format("Regression check - error while converting names (%s) - work with %s",
                            entry.toString(),
                            nameToCheck));
                    report.addLogOfErrors(
                            String.format("Regression check - error while converting names (%s)- work with %s. Error is [%s]",
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

                resultSet = pstmt.executeQuery();
                requestsCount++;

                log.debug(
                        String.format(
                                "************************* Birt query for regression check has been executed (%s)",
                                fullQuery));

                if (!resultSet.isBeforeFirst()) {

                    entry.setIsRegression("Not found");
                    entry.setFullNameOfRegressionPon(nameToCheck + " [is not found]");
                }

                while (resultSet.next()) {
                    String resultPrevTest = resultSet.getString(mainConfig.getBIRT_TEST_RESULT_NAME());
                    String resultOriginalTest = entry.getResultOfCheckText();

                    //FixME - provide logic that analyses case that in previous PON test was skipped!
                    if (resultOriginalTest.equals(resultPrevTest)) {
                        entry.setIsRegression("No");
                    } else if (resultOriginalTest.toUpperCase().equals("NOK") && resultPrevTest.toUpperCase().equals("OK")) {
                        entry.setIsRegression("Yes");
                        entry.setFullNameOfRegressionPon(nameToCheck);
                    } else {
                        entry.setIsRegression("Yes (see comment)");
                        entry.setFullNameOfRegressionPon(
                                String.format(" {} [ with result of prev test {}]",
                                        nameToCheck,
                                        resultPrevTest));
                    }
                }
            }

        }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            report.addLogOfErrors(e.getMessage());
//        }
    }
}
