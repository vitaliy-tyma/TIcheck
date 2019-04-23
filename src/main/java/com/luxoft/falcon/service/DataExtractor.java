package com.luxoft.falcon.service;

import com.luxoft.falcon.model.SpiderDataClass;
import com.luxoft.falcon.model.SpiderErrorClass;

import java.sql.*;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * DataExtractor.getData class returns SpiderDataClass spiderData (in arguments) appended with ErrorList
 */
@Slf4j
public class DataExtractor {
    private static final String ERROR_START = " ERROR ";
    private static final String ERROR_FINISH = " - ";
    private static final int ERROR_COLUMN_NUMBER = 1;
    private static final int ERROR_JAVA_CLASS_COLUMN_NUMBER = 2;

    private static final int JIRALINK_COLUMN_NUMBER = 3;
    private static final int COUNT_COLUMN_NUMBER = 4;
    private static final int TASK_COLUMN_NUMBER = 5;
    private static final int DESCRIPTION_COLUMN_NUMBER = 6;
//    private static final int BRANCH_COLUMN_NUMBER = 7; /*Not used now*/
//    private static final int PROJECT_COLUMN_NUMBER = 8; /*Not used now*/

    /**
     * It is a static method!
     */
    public static void getData(ResultSet resultSet, SpiderDataClass spiderData) throws SQLException {

        log.debug("********** Run getDataFromSpider **********");
        ArrayList<SpiderErrorClass> errorList = new ArrayList<SpiderErrorClass>();

        try {
            while (resultSet.next()) {
                SpiderErrorClass spiderError = new SpiderErrorClass();

                String error = resultSet.getString(ERROR_COLUMN_NUMBER);
                spiderError.setError(error);

                String errorJavaClass = resultSet.getString(ERROR_JAVA_CLASS_COLUMN_NUMBER);
                spiderError.setErrorJavaClass(errorJavaClass.trim()); /*Cut white spaces before and after*/

                try {
                    spiderError.setErrorExtracted(
                            error.substring(
                                    (error.indexOf(ERROR_START) + ERROR_START.length()), error.indexOf(ERROR_FINISH)));

                } catch (Exception e) {
                    log.error(String.valueOf(e));
                    spiderError.setErrorExtracted(error +
                            " [" + DataExtractor.class.getName() + ": " + String.valueOf(e) + "]");
                }

                String jiraLink = resultSet.getString(JIRALINK_COLUMN_NUMBER);
                spiderError.setJiraTicket(jiraLink);

                String count = resultSet.getString(COUNT_COLUMN_NUMBER);
                spiderError.setCount(count);

                String task = resultSet.getString(TASK_COLUMN_NUMBER);
                spiderError.setTask(task);

                String description = resultSet.getString(DESCRIPTION_COLUMN_NUMBER);
                spiderError.setDescription(description);

//                String branchName = resultSet.getString(BRANCH_COLUMN_NUMBER); /*Not used now*/
//                spiderError.setBranchName(branchName);

//                String projectName = resultSet.getString(PROJECT_COLUMN_NUMBER); /*Not used now*/
//                spiderError.setProjectName(projectName);


                errorList.add(spiderError);
            }
        } catch (Exception e) {
            /** ERROR PROCESSING - SEND TO LOG*/
            log.error(String.valueOf(e));

            spiderData.setJdbcError("<font color = red>" + String.valueOf(e) + "</font><br/>");

            /** SEND EXCEPTION TO UPPER LEVEL*/
            throw e;
        }

        /** Upload error list to the existing entity*/
        spiderData.setErrorsList(errorList);

        return;
    }
}
