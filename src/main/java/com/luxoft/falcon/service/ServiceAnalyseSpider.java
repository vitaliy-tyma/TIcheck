package com.luxoft.falcon.service;

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

    private static ConfigDataSpider configDataSpider = new ConfigDataSpider();
    private static Connection con = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet resultSet = null;

    private static LinkedList<ErrorRecord> spiderErrors = new LinkedList<>();
    private static LinkedList<ErrorRecord> spiderErrorsRegression = new LinkedList<>();




    public static void processSpiderChecklist(ChecklistTI checklist, Boolean analyseRegression) {




        log.info("**** in ServiceAnalyseSpider.processSpiderChecklist() ****");

        try {
            con = DbConnectorSpider.connectDatabase(configDataSpider);






            /* Iterate over SPIDER*/
            for (ChecklistEntry entry : checklist.getSpiderSteps()) {
                try {
                    if (checklist.getAutocomplete()) {
                        pstmt = con.prepareStatement(configDataSpider.getQueryLike());
                        pstmt.setString(1, "%" + checklist.getName() + "%");
                    } else {
                        pstmt = con.prepareStatement(configDataSpider.getQueryAccurate());
                        pstmt.setString(1, checklist.getName());
                    }

                    pstmt.setInt(2, checklist.getIteration());
                    pstmt.setString(3, entry.getNameOfErrorToCheckFor());
                    pstmt.setInt(4, MainConfig.getQUERY_LIMIT());

                    resultSet = pstmt.executeQuery();

                    log.info(String.format("************************* Spider query executed(%s)", pstmt.toString()));

                    /* Mark the item in checklist if query has been executed!*/
                    entry.setStepIsChecked(true);
                    entry.setFullQuery(pstmt.toString());




                    while (resultSet.next()) {
                        String fullName = resultSet.getString(MainConfig.getSPIDER_TASK_COL_NAME());
                        String error = resultSet.getString(MainConfig.getSPIDER_JAVA_CLASS_ERROR_COL_NAME());
                        String fullQuery = pstmt.toString();

                        ErrorRecord spiderError = new ErrorRecord(fullName, error, fullQuery);
                        spiderErrors.add(spiderError);

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
            con.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            checklist.addLogOfErrors(e.getMessage());
        }






    }
}