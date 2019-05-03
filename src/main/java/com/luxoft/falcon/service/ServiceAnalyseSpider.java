package com.luxoft.falcon.service;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.dao.DbConnectorSpider;
import com.luxoft.falcon.model.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;


/**
 * Is used to process TI checklist for SPIDER
 */
@Slf4j
public class ServiceAnalyseSpider {

    private static ConfigDataSpider configDataSpider = new ConfigDataSpider();
    private static Connection con = null;


    public static LinkedList<ErrorRecord> processSpider(ChecklistTI checklistTI, Pon pon) {

        LinkedList<ErrorRecord> spiderErrors = new LinkedList<>();
        log.info("**** in ServiceAnalyseSpider.processSpider() ****");

        try {
            con = DbConnectorSpider.connectDatabase(configDataSpider);

            /* Iterate over SPIDER*/
            for (ChecklistTiEntry entry : checklistTI.getSpiderSteps()) {
                PreparedStatement pstmt;
                ResultSet resultSet;


                try {
                    if (pon.getAutocomplete()) {
                        pstmt = con.prepareStatement(configDataSpider.getQueryLike());
                        pstmt.setString(1, "%" + pon.getName() + "%");
                    } else {
                        pstmt = con.prepareStatement(configDataSpider.getQueryAccurate());
                        pstmt.setString(1, pon.getName());
                    }

                    pstmt.setInt(2, pon.getIteration());
                    pstmt.setString(3, entry.getNameOfError());
                    pstmt.setInt(4, MainConfig.getQUERY_LIMIT());

                    resultSet = pstmt.executeQuery();

                    log.info(String.format("************************* Spider query executed(%s)", pstmt.toString()));

                    /* Mark the item in checklistTI if query has been executed!*/
                    entry.setStepIsChecked(true);
                    entry.setFullQuery(pstmt.toString());

                    while (resultSet.next()) {
                        String fullName = resultSet.getString(MainConfig.getSPIDER_TASK_COL_NAME());
                        String error = resultSet.getString(MainConfig.getSPIDER_JAVA_CLASS_ERROR_COL_NAME());
                        String fullQuery = pstmt.toString();

                        ErrorRecord spiderError = new ErrorRecord(fullName, error, fullQuery);
                        spiderErrors.add(spiderError);

                        entry.setResultOfCheckIsNOK(true);
                    }

                    resultSet.close();
                    pstmt.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                    pon.setOutputOfErrors(e.getMessage());
                }
            }
            con.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            pon.setOutputOfErrors(e.getMessage());
        }
        return spiderErrors;
    }
}