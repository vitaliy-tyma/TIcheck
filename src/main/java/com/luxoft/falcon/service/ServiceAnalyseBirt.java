package com.luxoft.falcon.service;

import com.luxoft.falcon.config.Birt2010ConfigAndQuery;
import com.luxoft.falcon.config.Birt2020ConfigAndQuery;
import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.dao.DbConnectorBirt;
import com.luxoft.falcon.model.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


import com.luxoft.falcon.config.BirtQueryToCheckGeneration;

/**
 * Is used to process TI checklist for BIRT
 */
@Slf4j
public class ServiceAnalyseBirt {

    private static Connection con = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet resultSet = null;

    private static Birt2010ConfigAndQuery birt2010ConfigAndQuery = new Birt2010ConfigAndQuery();
    private static Birt2020ConfigAndQuery birt2020ConfigAndQuery = new Birt2020ConfigAndQuery();
    private static BirtQueryToCheckGeneration birtQueryToCheckGeneration = new BirtQueryToCheckGeneration();

    private static PreparedStatement pstmtChecker;
    private static ResultSet resultSetChecker;

    private static String queryLike = null;
    private static String queryAccurate = null;
    private static boolean isGenDefined = false;
    private static int requestsCount;

    public static int processBirtChecklist(Checklist checklist, Report report, Boolean analyseRegression) {
        requestsCount = 0;
        log.debug("**** in ServiceAnalyseBirt.processBirt() ****");

        try {
            /* CHECK GENERATION FIRST */
            con = DbConnectorBirt.connectDatabase(
                    birt2010ConfigAndQuery.getJdbcUrl(),
                    birt2010ConfigAndQuery.getJdbcLogin(),
                    birt2010ConfigAndQuery.getJdbcPassword());
            isGenDefined = checkGeneration(report);

            List<ChecklistEntry> fillBirtErrors = new LinkedList<>();
            /* Iterate over BIRT*/
            if (isGenDefined) {

                if (report.getAutocomplete()) {
                    pstmt = con.prepareStatement(queryLike);
                    pstmt.setString(1, "%" + report.getName() + "%R" + report.getIteration());
                } else {
                    pstmt = con.prepareStatement(queryAccurate);
                    pstmt.setString(1, report.getName() + "_R" + report.getIteration());
                }
                pstmt.setInt(3, report.getLimit());

                for (String errorToCheck : checklist.getBirtSteps()) {
                    try {
                        pstmt.setString(2, errorToCheck);


                        String fullQuery = pstmt.toString();
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
                        }



                        /*Check is it possible to aggregate all rows to one with simplified name of PON*/
//                        Boolean aggregateBirtSteps = Boolean.TRUE;
//                        String resultOfCheckFirstRow = null;
//                        Boolean firstRow = Boolean.TRUE;
//                        while (resultSet.next()) {
////                            String fullName = resultSet.getString(MainConfig.getBIRT_TASK_COL_NAME());
//                            String resultOfTest = resultSet.getString(MainConfig.getBIRT_TEST_RESULT_NAME());
//
//                            if (firstRow){
//                                resultOfCheckFirstRow = resultOfTest;
//                                firstRow = Boolean.FALSE;
//                            }
//                            if (resultOfTest.toUpperCase().equals(resultOfCheckFirstRow)) {
//                                continue;
//                            }
//                            aggregateBirtSteps = Boolean.FALSE;
//                            break;
//                        }
//                        if (aggregateBirtSteps){
////                            fillBirtErrors.clear();
//                            fillBirtErrors.add(
//                                    new ChecklistEntry(
//                                            errorToCheck,
//                                            Boolean.TRUE,
//                                            Boolean.FALSE,
//                                            fullQuery,
//                                            report.getName(),
//                                            resultOfCheckFirstRow));
//                        } else {
//                            resultSet.beforeFirst();
                            while (resultSet.next()) {
                                String fullName = resultSet.getString(MainConfig.getBIRT_TASK_COL_NAME());
                                String testName = resultSet.getString(MainConfig.getBIRT_TEST_COL_NAME());
                                String resultOfTest = resultSet.getString(MainConfig.getBIRT_TEST_RESULT_NAME());

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

                                /*Store only NOK results */
                                fillBirtErrors.add(entry);

                                log.debug(String.format("************************* BIRT resultSet item (%s) processed !!!!!!!!!!!!!!!!!!!!!!!!!!!!", fullName));

                            }

//                        }








                    } catch (Exception e) {
                        log.error(e.getMessage());
                        report.addLogOfErrors(e.getMessage());
                    }
                }









                /* Store items in report*/
                report.setBirtSteps(fillBirtErrors);

                /* CHECK FOR REGRESSION */
                if (analyseRegression) {
                    analyseRegression(report);
                }

                resultSet.close();
                pstmt.close();
            }
            con.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            report.addLogOfErrors(e.getMessage());
        } finally {
            /*Breaks BIRT processing*/
//            if (resultSet != null) {
//                resultSet = null;
//            }
//            if (pstmt != null) {
//                pstmt = null;
//            }
//            if (con != null) {
//                con = null;
//            }
        }


        return requestsCount;
    }


    private static boolean checkGeneration(Report report) throws SQLException, ClassNotFoundException {
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






    /* Make regression analysis*/
    private static void analyseRegression(Report report) {
        for (ChecklistEntry entry : report.getBirtSteps()) {
            try {
                String[] restOfOriginalNameArray = entry.getFullNameOfPon().split(report.getName(), 2);
                String nameToCheck;
                try {

                    if (restOfOriginalNameArray[1].contains("_")){
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

                if (report.getPrevAutocomplete()) {
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

                if (!resultSet.isBeforeFirst() && entry.getResultOfCheckIsNOK()) {
                    entry.setIsRegression("Yes");
                    entry.setFullNameOfRegressionPon(nameToCheck);
                }

                while (resultSet.next()) {
                    String resultPrevTest = resultSet.getString(MainConfig.getBIRT_TEST_RESULT_NAME());
                    String resultOriginalTest = entry.getResultOfCheckText();

                    if (resultOriginalTest.equals(resultPrevTest)) {
                        entry.setIsRegression("No");
                    } else if (resultOriginalTest.toUpperCase().equals("NOK") && resultPrevTest.toUpperCase().equals("OK")) {
                        entry.setIsRegression("Yes");
                        entry.setFullNameOfRegressionPon(nameToCheck);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                report.addLogOfErrors(e.getMessage());
            }
        }
    }
}
