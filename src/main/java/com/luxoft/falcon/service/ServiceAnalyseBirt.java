package com.luxoft.falcon.service;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.dao.DbConnectorBirt;
import com.luxoft.falcon.model.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;


import com.luxoft.falcon.model.QueryToCheckGeneration;

/**
 * Is used to process TI checklist for BIRT
 */
@Slf4j
public class ServiceAnalyseBirt {

    private static Connection con = null;
    private static ConfigDataBirt2010 configDataBirt2010 = new ConfigDataBirt2010();
    private static ConfigDataBirt2020 configDataBirt2020 = new ConfigDataBirt2020();
    private static QueryToCheckGeneration queryToCheckGeneration = new QueryToCheckGeneration();

    private static PreparedStatement pstmtChecker;
    private static ResultSet resultSetChecker;

    public static LinkedList<ErrorRecord> processBirtChecklist(ChecklistTI checklist, Boolean analyseRegression) {

        LinkedList<ErrorRecord> birtErrors = new LinkedList<>();
        String queryLike = null;
        String queryAccurate = null;
        boolean isGenDefined = false;
        log.info("**** in ServiceAnalyseBirt.processBirt() ****");


        try {
            /* CHECK GENERATION FIRST */
            con = DbConnectorBirt.connectDatabase(
                    configDataBirt2010.getJdbcUrl(),
                    configDataBirt2010.getJdbcLogin(),
                    configDataBirt2010.getJdbcPassword());



//2010
            String queryToCheck = queryToCheckGeneration.getG2010();
            pstmtChecker = con.prepareStatement(queryToCheck);
            pstmtChecker.setString(1, "%" + checklist.getName() + "%");
            pstmtChecker.setInt(2, MainConfig.getQUERY_LIMIT());
            resultSetChecker = pstmtChecker.executeQuery();
            if (resultSetChecker.isBeforeFirst()) {
                log.info("***************************** Use Birt 2010 *****************");
                queryLike = configDataBirt2010.getQueryLike();
                queryAccurate = configDataBirt2010.getQueryAccurate();
                isGenDefined = true;
            }
            resultSetChecker.close();
            pstmtChecker.close();
            con.close();


//2020
            con = DbConnectorBirt.connectDatabase(
                    configDataBirt2020.getJdbcUrl(),
                    configDataBirt2020.getJdbcLogin(),
                    configDataBirt2020.getJdbcPassword());
            queryToCheck = queryToCheckGeneration.getG2020();
            pstmtChecker = con.prepareStatement(queryToCheck);
            pstmtChecker.setString(1, "%" + checklist.getName() + "%");
            pstmtChecker.setInt(2, MainConfig.getQUERY_LIMIT());
            resultSetChecker = pstmtChecker.executeQuery();
            if (resultSetChecker.isBeforeFirst()) {
                log.info("***************************** Use Birt 2020 *****************");
                queryLike = configDataBirt2020.getQueryLike();
                queryAccurate = configDataBirt2020.getQueryAccurate();
                isGenDefined = true;
            }
            resultSetChecker.close();
            pstmtChecker.close();





            /* Iterate over BIRT*/
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

                        log.info(String.format("************************* Birt query executed(%s)", pstmt.toString()));
                        /* Mark the item in checklistTI if query has been executed!*/
                        entry.setStepIsChecked(true);
                        entry.setFullQuery(pstmt.toString());

                        while (resultSet.next()) {
                            String fullName = resultSet.getString(MainConfig.getBIRT_TASK_COL_NAME());
                            String testName = resultSet.getString(MainConfig.getBIRT_TEST_COL_NAME());
                            String fullQuery = pstmt.toString();

                            ErrorRecord birtError = new ErrorRecord(fullName, testName, fullQuery);
                            birtErrors.add(birtError);

                            entry.setResultOfCheckIsNOK(true);
                            entry.setFullNameOfPon(fullName);
                        }

                    } catch (Exception e) {
                        log.error(e.getMessage());
                        checklist.addLogOfErrors(e.getMessage());
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
        return birtErrors;
    }





}