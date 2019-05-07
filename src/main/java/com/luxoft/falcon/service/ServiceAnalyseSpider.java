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
import java.util.List;


/* Is used to process checklist for SPIDER */
@Slf4j
public class ServiceAnalyseSpider {

    private static ConfigAndQueryForSpider configAndQueryForSpider = new ConfigAndQueryForSpider();
    private static Connection con = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet resultSet = null;


    public static void processSpiderChecklist(Checklist checklist, Report report, Boolean analyseRegression) {


        log.info("**** in ServiceAnalyseSpider.processSpiderChecklist() ****");

        try {
            con = DbConnectorSpider.connectDatabase(configAndQueryForSpider);






            /* Iterate over SPIDER*/
            List<ChecklistEntry> fillSpiderErrors = new LinkedList<>();

            for (String errorToCheck : checklist.getSpiderSteps()) {

                try {
                    if (report.getAutocomplete()) {
                        pstmt = con.prepareStatement(configAndQueryForSpider.getQueryLike());
                        pstmt.setString(1, "%" + report.getName() + "%");
                    } else {
                        pstmt = con.prepareStatement(configAndQueryForSpider.getQueryAccurate());
                        pstmt.setString(1, report.getName());
                    }

                    pstmt.setInt(2, report.getIteration());
                    pstmt.setString(3, errorToCheck);
                    pstmt.setInt(4, report.getLimit());
                    String fullQuery = pstmt.toString();

                    resultSet = pstmt.executeQuery();

                    log.info(String.format("************************* Spider query has been executed (%s)", fullQuery));


                    /*If ResultSet is empty create one new item in List of Spider Errors with step os checked Flag*/
                    if (!resultSet.isBeforeFirst()) {
                        fillSpiderErrors.add(
                                new ChecklistEntry(
                                        errorToCheck,
                                        Boolean.TRUE,
                                        Boolean.FALSE,
                                        fullQuery,
                                        report.getName(),
                                        "Not found"));
                    }





                    /* Ir ResultSet is not empty - check all rows and create new items in List of Spider Errors*/
                    while (resultSet.next()) {
                        String fullName = resultSet.getString(MainConfig.getSPIDER_TASK_COL_NAME());
                        String error = resultSet.getString(MainConfig.getSPIDER_JAVA_CLASS_ERROR_COL_NAME());


                        fillSpiderErrors.add(
                                new ChecklistEntry(
                                        error,
                                        true,
                                        true,
                                        fullQuery,
                                        fullName,
                                        "NOK"));

                        log.info(String.format("************************* Spider resultSet item (%s) processed !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", fullName));
                    }


                } catch (Exception e) {
                    log.error(e.getMessage());
                    report.addLogOfErrors(e.getMessage());
                }

            }


            /* Store items in report*/
            report.setSpiderSteps(fillSpiderErrors);









            /* CHECK FOR REGRESSION */
            if (analyseRegression) {
                analyseRegression(report);
            }


            resultSet.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            report.addLogOfErrors(e.getMessage());
        }


    }


    private static void analyseRegression(Report report) {


        for (ChecklistEntry entry : report.getSpiderSteps()) {

            try {

//                String restOfOriginalName = entry.getFullNameOfPon().replace(report.getName(), "");

                String[] restOfOriginalNameArray = entry.getFullNameOfPon().split(report.getName(), 2);
                String nameToCheck;
                try{
                    nameToCheck = restOfOriginalNameArray[0] + report.getPrevName() + restOfOriginalNameArray[1];
                } catch (Exception e){
                    nameToCheck = report.getPrevName();
                    log.error(String.format("Regression check - error while converting names - work with %s", nameToCheck));
                    report.addLogOfErrors(
                            String.format("Regression check - error while converting names - work with %s. Error is [%s]",
                                    nameToCheck,
                                    e.getMessage()));
                }


                if (report.getPrevAutocomplete()) {
                    pstmt = con.prepareStatement(configAndQueryForSpider.getQueryLike());
                    pstmt.setString(1, "%" + nameToCheck.trim() + "%");
                } else {
                    pstmt = con.prepareStatement(configAndQueryForSpider.getQueryAccurate());
                    pstmt.setString(1, nameToCheck.trim());
                }

                pstmt.setInt(2, report.getPrevIteration());
                pstmt.setString(3, entry.getNameOfErrorToCheckFor());
                pstmt.setInt(4, report.getLimit());
                String fullQuery = pstmt.toString();

                resultSet = pstmt.executeQuery();

                log.info(
                        String.format(
                                "************************* Spider query for regression check has been executed (%s)",
                                fullQuery));


                if (!resultSet.isBeforeFirst() && entry.getResultOfCheckIsNOK()) {
                    entry.setIsRegression("Yes");
                    entry.setFullNameOfRegressionPon(nameToCheck);
                }

//                if (analyseRegression && !entry.getResultOfCheckIsNOK()) {
//                    entry.setIsRegression("No");
//                }

                while (resultSet.next()) {
                    String fullName = resultSet.getString(MainConfig.getSPIDER_TASK_COL_NAME());
                    String restOfPrevName = fullName.replace(report.getPrevName(), "");
                    String restOfOriginalName = entry.getFullNameOfPon().replace(report.getName(), "");

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