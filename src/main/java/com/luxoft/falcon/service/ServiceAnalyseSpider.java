package com.luxoft.falcon.service;

import com.luxoft.falcon.config.ConfigAndQueryForSpider;
import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.dao.DbConnectorSpider;
import com.luxoft.falcon.model.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;


/**
 * Is used to process TI checklist for SPIDER
 */
@Slf4j
public class ServiceAnalyseSpider {

    private static ConfigAndQueryForSpider configAndQueryForSpider = new ConfigAndQueryForSpider();
    private static Connection con = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet resultSet = null;


    public static void processSpiderChecklist(ChecklistTI checklist, Boolean analyseRegression, Boolean nokOnlyBool) {


        log.info("**** in ServiceAnalyseSpider.processSpiderChecklist() ****");

        try {
            con = DbConnectorSpider.connectDatabase(configAndQueryForSpider);






            /* Iterate over SPIDER*/
            LinkedList<ChecklistEntry> spiderErrors = new LinkedList<>();
            for (ChecklistEntry entry : checklist.getSpiderSteps()) {

                try {
                    if (checklist.getAutocomplete()) {
                        pstmt = con.prepareStatement(configAndQueryForSpider.getQueryLike());
                        pstmt.setString(1, "%" + checklist.getName() + "%");
                    } else {
                        pstmt = con.prepareStatement(configAndQueryForSpider.getQueryAccurate());
                        pstmt.setString(1, checklist.getName());
                    }

                    pstmt.setInt(2, checklist.getIteration());
                    pstmt.setString(3, entry.getNameOfErrorToCheckFor());
                    pstmt.setInt(4, MainConfig.getQUERY_LIMIT());

                    resultSet = pstmt.executeQuery();

                    log.info(String.format("************************* Spider query has been executed(%s)", pstmt.toString()));

                    /* Mark the item in checklist if query has been executed!*/
                    entry.setStepIsChecked(true);
                    entry.setFullQuery(pstmt.toString());


                    if (!resultSet.isBeforeFirst()) {
                        entry.setResultOfCheckIsNOK(false);
                        spiderErrors.add(new ChecklistEntry(entry.getNameOfErrorToCheckFor(), true, false,
                                entry.getFullQuery(), checklist.getName(), "Not analysed"));

                    }


                    while (resultSet.next()) {
                        String fullName = resultSet.getString(MainConfig.getSPIDER_TASK_COL_NAME());
                        String error = resultSet.getString(MainConfig.getSPIDER_JAVA_CLASS_ERROR_COL_NAME());
                        String fullQuery = pstmt.toString();


                        entry.setResultOfCheckIsNOK(true);
                        entry.setFullNameOfPon(fullName);


                        spiderErrors.add(new ChecklistEntry(error, true, true, fullQuery, fullName, "Not analysed"));


                    }


                } catch (Exception e) {
                    log.error(e.getMessage());
                    checklist.addLogOfErrors(e.getMessage());
                }

            }


            checklist.setSpiderSteps(spiderErrors);









            /* CHECK FOR REGRESSION */
            if (analyseRegression) {

                for (ChecklistEntry entry : checklist.getSpiderSteps()) {

                    try {
                        if (checklist.getAutocomplete()) {
                            pstmt = con.prepareStatement(configAndQueryForSpider.getQueryLike());
                            pstmt.setString(1, "%" + checklist.getName() + "%");
                        } else {
                            pstmt = con.prepareStatement(configAndQueryForSpider.getQueryAccurate());
                            pstmt.setString(1, checklist.getName());
                        }

                        pstmt.setInt(2, checklist.getIteration());
                        pstmt.setString(3, entry.getNameOfErrorToCheckFor());
                        pstmt.setInt(4, MainConfig.getQUERY_LIMIT());

                        resultSet = pstmt.executeQuery();

                        log.info(String.format("************************* Spider query for regression check has been executed(%s)", pstmt.toString()));


                        if (!resultSet.isBeforeFirst() && entry.getResultOfCheckIsNOK()) {
                            entry.setIsRegression("Yes");
                        }

                        if (!entry.getResultOfCheckIsNOK()){
                            entry.setIsRegression("No");
                        }

                        while (resultSet.next()) {
                            String fullName = resultSet.getString(MainConfig.getSPIDER_TASK_COL_NAME());
//                            String error = resultSet.getString(MainConfig.getSPIDER_JAVA_CLASS_ERROR_COL_NAME());
//                            String fullQuery = pstmt.toString();

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
            con.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            checklist.addLogOfErrors(e.getMessage());
        }


    }
}