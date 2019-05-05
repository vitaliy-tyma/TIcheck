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
import java.util.LinkedList;


import com.luxoft.falcon.config.QueryToCheckGeneration;

/**
 * Is used to process TI checklist for BIRT
 */
@Slf4j
public class ServiceAnalyseBirt {

    private static Connection con = null;
    private static ConfigAndQueryForBirt2010 configAndQueryForBirt2010 = new ConfigAndQueryForBirt2010();
    private static ConfigAndQueryForBirt2020 configAndQueryForBirt2020 = new ConfigAndQueryForBirt2020();
    private static QueryToCheckGeneration queryToCheckGeneration = new QueryToCheckGeneration();

    private static PreparedStatement pstmtChecker;
    private static ResultSet resultSetChecker;

    public static void processBirtChecklist(ChecklistTI checklist, Boolean analyseRegression, Boolean nokOnlyBool) {

//        LinkedList<ErrorRecord> birtErrors = new LinkedList<>();
        String queryLike = null;
        String queryAccurate = null;
        boolean isGenDefined = false;
        log.info("**** in ServiceAnalyseBirt.processBirt() ****");


        try {
            /* CHECK GENERATION FIRST */
            con = DbConnectorBirt.connectDatabase(
                    configAndQueryForBirt2010.getJdbcUrl(),
                    configAndQueryForBirt2010.getJdbcLogin(),
                    configAndQueryForBirt2010.getJdbcPassword());



//2010
            String queryToCheck = queryToCheckGeneration.getG2010();
            pstmtChecker = con.prepareStatement(queryToCheck);
            pstmtChecker.setString(1, "%" + checklist.getName() + "%");
            pstmtChecker.setInt(2, MainConfig.getQUERY_LIMIT());
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
            pstmtChecker.setString(1, "%" + checklist.getName() + "%");
            pstmtChecker.setInt(2, MainConfig.getQUERY_LIMIT());
            resultSetChecker = pstmtChecker.executeQuery();
            if (resultSetChecker.isBeforeFirst()) {
                log.info("***************************** Use Birt 2020 *****************");
                queryLike = configAndQueryForBirt2020.getQueryLike();
                queryAccurate = configAndQueryForBirt2020.getQueryAccurate();
                isGenDefined = true;
            }
            resultSetChecker.close();
            pstmtChecker.close();





            /* Iterate over BIRT*/
            LinkedList<ChecklistEntry> birtErrors = new LinkedList<>();
            if (isGenDefined) {
                PreparedStatement pstmt = null;
                ResultSet resultSet = null;

                for (ChecklistEntry entry : checklist.getBirtSteps()) {


                    try {
                        if (checklist.getAutocomplete()) {
                            pstmt = con.prepareStatement(queryLike);
                            pstmt.setString(1, "%" + checklist.getName() + "%R" + Integer.valueOf(checklist.getIteration()));
                        } else {
                            pstmt = con.prepareStatement(queryAccurate);
                            pstmt.setString(1, checklist.getName() + "_R" + Integer.valueOf(checklist.getIteration()));
                        }

                        //pstmt.setInt(2, pon.getIteration());
                        pstmt.setString(2, entry.getNameOfErrorToCheckFor());
                        pstmt.setInt(3, MainConfig.getQUERY_LIMIT());

                        resultSet = pstmt.executeQuery();

                        log.info(String.format("************************* Birt query has been executed(%s)", pstmt.toString()));
                        /* Mark the item in checklistTI if query has been executed!*/
                        entry.setStepIsChecked(true);
                        entry.setFullQuery(pstmt.toString());




                        if (!resultSet.isBeforeFirst()){
                            entry.setResultOfCheckIsNOK(false);
                            birtErrors.add(new ChecklistEntry(entry.getNameOfErrorToCheckFor(), true, false,
                                    entry.getFullQuery(), checklist.getName(), "Not analysed"));
                        }


                        while (resultSet.next()) {
                            String fullName = resultSet.getString(MainConfig.getBIRT_TASK_COL_NAME());
                            String testName = resultSet.getString(MainConfig.getBIRT_TEST_COL_NAME());
                            String fullQuery = pstmt.toString();

//                            ErrorRecord birtError = new ErrorRecord(fullName, testName, fullQuery);
//                            birtErrors.add(birtError);
                            birtErrors.add(new ChecklistEntry(testName, true, true, fullQuery, fullName, "Not analysed"));


                            entry.setResultOfCheckIsNOK(true);
                            entry.setFullNameOfPon(fullName);
                        }

                    } catch (Exception e) {
                        log.error(e.getMessage());
                        checklist.addLogOfErrors(e.getMessage());
                    }
                }

                checklist.setBirtSteps(birtErrors);












                /* CHECK FOR REGRESSION */
                if (analyseRegression) {


                    for (ChecklistEntry entry : checklist.getBirtSteps()) {


                        try {
                            if (checklist.getAutocomplete()) {
                                pstmt = con.prepareStatement(queryLike);
                                pstmt.setString(1, "%" + checklist.getName() + "%R" + Integer.valueOf(checklist.getIteration()));
                            } else {
                                pstmt = con.prepareStatement(queryAccurate);
                                pstmt.setString(1, checklist.getName() + "_R" + Integer.valueOf(checklist.getIteration()));
                            }

                            //pstmt.setInt(2, pon.getIteration());
                            pstmt.setString(2, entry.getNameOfErrorToCheckFor());
                            pstmt.setInt(3, MainConfig.getQUERY_LIMIT());

                            resultSet = pstmt.executeQuery();

                            log.info(String.format("************************* Birt query for regression check has been executed(%s)", pstmt.toString()));
                            /* Mark the item in checklistTI if query has been executed!*/
                            entry.setStepIsChecked(true);
                            entry.setFullQuery(pstmt.toString());





                            if (!resultSet.isBeforeFirst() && entry.getResultOfCheckIsNOK()) {
                                entry.setIsRegression("Yes");
                            }

                            if (!entry.getResultOfCheckIsNOK()){
                                entry.setIsRegression("No");
                            }

                            while (resultSet.next()) {
                                String fullName = resultSet.getString(MainConfig.getBIRT_TASK_COL_NAME());

                                if (entry.getFullNameOfPon().equals(fullName)) {
                                    entry.setIsRegression("No");
                                }

                            }

                        } catch (Exception e) {
                            log.error(e.getMessage());
                            checklist.addLogOfErrors(e.getMessage());
                        }
                    }


















                }




                resultSet.close();
                pstmt.close();
            }
            con.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            checklist.addLogOfErrors(e.getMessage());
        }


    }





}