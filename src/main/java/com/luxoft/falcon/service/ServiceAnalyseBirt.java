package com.luxoft.falcon.service;

import com.luxoft.falcon.config.ConfigAndQueryForBirt2010;
import com.luxoft.falcon.config.ConfigAndQueryForBirt2020;
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


import com.luxoft.falcon.config.QueryToCheckGeneration;

/**
 * Is used to process TI checklist for BIRT
 */
@Slf4j
public class ServiceAnalyseBirt {

    private static Connection con = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet resultSet = null;

    private static ConfigAndQueryForBirt2010 configAndQueryForBirt2010 = new ConfigAndQueryForBirt2010();
    private static ConfigAndQueryForBirt2020 configAndQueryForBirt2020 = new ConfigAndQueryForBirt2020();
    private static QueryToCheckGeneration queryToCheckGeneration = new QueryToCheckGeneration();

    private static PreparedStatement pstmtChecker;
    private static ResultSet resultSetChecker;

    private static String queryLike = null;
    private static String queryAccurate = null;
    private static boolean isGenDefined = false;


    public static void processBirtChecklist(Checklist checklist, Report report, Boolean analyseRegression) {


        log.info("**** in ServiceAnalyseBirt.processBirt() ****");


        try {
            /* CHECK GENERATION FIRST */
            con = DbConnectorBirt.connectDatabase(
                    configAndQueryForBirt2010.getJdbcUrl(),
                    configAndQueryForBirt2010.getJdbcLogin(),
                    configAndQueryForBirt2010.getJdbcPassword());
            isGenDefined = checkGeneration(report);






            /* Iterate over BIRT*/
            List<ChecklistEntry> fillBirtErrors = new LinkedList<>();
            if (isGenDefined) {
//                PreparedStatement pstmt = null;
//                ResultSet resultSet = null;

                for (String errorToCheck : checklist.getBirtSteps()) {


                    try {
                        if (report.getAutocomplete()) {
                            pstmt = con.prepareStatement(queryLike);
                            pstmt.setString(1, "%" + report.getName() + "%R" + report.getIteration());
                        } else {
                            pstmt = con.prepareStatement(queryAccurate);
                            pstmt.setString(1, report.getName() + "_R" + report.getIteration());
                        }

                        //pstmt.setInt(2, pon.getIteration());
                        pstmt.setString(2, errorToCheck);
                        pstmt.setInt(3, report.getLimit());

                        String fullQuery = pstmt.toString();
                        resultSet = pstmt.executeQuery();

                        log.info(String.format("************************* Birt query has been executed (%s)", fullQuery));
                        /* Mark the item in checklist if query has been executed!*/


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


                        while (resultSet.next()) {
                            String fullName = resultSet.getString(MainConfig.getBIRT_TASK_COL_NAME());
                            String testName = resultSet.getString(MainConfig.getBIRT_TEST_COL_NAME());
                            String resultOfTest = resultSet.getString(MainConfig.getBIRT_TEST_RESULT_NAME());


                            ChecklistEntry entry = new ChecklistEntry(testName);

                            entry.setStepIsChecked(Boolean.TRUE);
                            entry.setFullQuery(fullQuery);
                            entry.setFullNameOfPon(fullName);

                            if (resultOfTest.toUpperCase().equals("NOK")) {
                                entry.setResultOfCheckIsNOK(Boolean.TRUE);
                            } else {
                                entry.setResultOfCheckIsNOK(Boolean.FALSE);
                            }
                            entry.setResultOfCheckText(resultOfTest);

                            fillBirtErrors.add(entry);
                            log.info(String.format("************************* BIRT resultSet item (%s) processed !!!!!!!!!!!!!!!!!!!!!!!!!!!!", fullName));

                        }

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
        }


    }

    private static boolean checkGeneration(Report report) throws SQLException, ClassNotFoundException {
        isGenDefined = Boolean.FALSE;
        //2010
        String queryToCheck = queryToCheckGeneration.getG2010();
        pstmtChecker = con.prepareStatement(queryToCheck);
        pstmtChecker.setString(1, "%" + report.getName() + "%");
        pstmtChecker.setInt(2, report.getLimit());
        resultSetChecker = pstmtChecker.executeQuery();
        if (resultSetChecker.isBeforeFirst()) {
            log.info("***************************** Use Birt 2010 *****************");
            queryLike = configAndQueryForBirt2010.getQueryLike();
            queryAccurate = configAndQueryForBirt2010.getQueryAccurate();
            isGenDefined = true;
        }
        resultSetChecker.close();
        pstmtChecker.close();
        con.close();


//2020
        con = DbConnectorBirt.connectDatabase(
                configAndQueryForBirt2020.getJdbcUrl(),
                configAndQueryForBirt2020.getJdbcLogin(),
                configAndQueryForBirt2020.getJdbcPassword());
        queryToCheck = queryToCheckGeneration.getG2020();
        pstmtChecker = con.prepareStatement(queryToCheck);
        pstmtChecker.setString(1, "%" + report.getName() + "%");
        pstmtChecker.setInt(2, report.getLimit());
        resultSetChecker = pstmtChecker.executeQuery();
        if (resultSetChecker.isBeforeFirst()) {
            log.info("***************************** Use Birt 2020 *****************");
            queryLike = configAndQueryForBirt2020.getQueryLike();
            queryAccurate = configAndQueryForBirt2020.getQueryAccurate();
            isGenDefined = true;
        }
        resultSetChecker.close();
        pstmtChecker.close();

        return isGenDefined;
    }


    private static void analyseRegression(Report report) {


        for (ChecklistEntry entry : report.getBirtSteps()) {


            try {

                String[] restOfOriginalNameArray = entry.getFullNameOfPon().split(report.getName(), 2);
                String nameToCheck;
                try {
                    nameToCheck = restOfOriginalNameArray[0] + report.getPrevName() + restOfOriginalNameArray[1];
                } catch (Exception e) {
                    nameToCheck = report.getPrevName();
                    log.error(String.format("Regression check - error while converting names - work with %s", nameToCheck));
                    report.addLogOfErrors(
                            String.format("Regression check - error while converting names - work with %s. Error is [%s]",
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

                //pstmt.setInt(2, pon.getIteration());//No used for BIRT in this way!
                pstmt.setString(2, entry.getNameOfErrorToCheckFor());
                pstmt.setInt(3, report.getLimit());
                String fullQuery = pstmt.toString();

                resultSet = pstmt.executeQuery();

                log.info(
                        String.format(
                                "************************* Birt query for regression check has been executed (%s)",
                                fullQuery));


                if (!resultSet.isBeforeFirst() && entry.getResultOfCheckIsNOK()) {
                    entry.setIsRegression("Yes");
                    entry.setFullNameOfRegressionPon(nameToCheck);
                }

//                if (analyseRegression && !entry.getResultOfCheckIsNOK()) {
//                    entry.setIsRegression("No");
//                }

                while (resultSet.next()) {
                    String fullName = resultSet.getString(MainConfig.getBIRT_TASK_COL_NAME());


                    String restOfOriginalName = entry.getFullNameOfPon().replace(report.getName(), "");
                    String restOfPrevName = fullName.replace(report.getPrevName(), "");


                    if (restOfOriginalName.equals(restOfPrevName)) {
                        entry.setIsRegression("No");
                    }


                }

            } catch (Exception e) {
                log.error(e.getMessage());
                report.addLogOfErrors(e.getMessage());
            }
        }


    }
}
