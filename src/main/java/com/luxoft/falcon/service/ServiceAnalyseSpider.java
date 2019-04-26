package com.luxoft.falcon.service;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.dao.DbConnectorSpider;
import com.luxoft.falcon.model.Checklist;
import com.luxoft.falcon.model.ConfigDataSpider;
import com.luxoft.falcon.model.Pon;
import com.luxoft.falcon.model.MapError;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;

/**
 * Is used to process TI checklist by data source
 */
@Slf4j
public class ServiceAnalyseSpider {

    private static ConfigDataSpider configDataSpider = new ConfigDataSpider();
    private static Connection con = null;


    public static LinkedList<MapError> processSpider(Checklist checklist, Pon pon) {

//        StringBuilder result = new StringBuilder();
        LinkedList<MapError> spiderErrors = new LinkedList<>();

        log.info("**** in ServiceAnalyseSpider.processSpider() ****");


        try {

            con = DbConnectorSpider.connectDatabase(configDataSpider);


            /* Iterate over SPIDER*/
            for (Map.Entry<String, Boolean> entry : checklist.getSpiderSteps().entrySet()) {
//                log.info(String.format("*** Iterate over spider steps [{}:{}] ***"),
//                        entry.getKey(), entry.getValue().toString());

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
                    pstmt.setString(3, entry.getKey());
                    pstmt.setInt(4, MainConfig.getQUERY_LIMIT());


                    /* Save query to display it in browser*/
                    //configDataSpider.setQueryFinal(pstmt.toString());
                    //-pon.setQueryFull(pstmt.toString());
                    //log.info("SQL = " + pstmt.toString());


                    resultSet = pstmt.executeQuery();
                    log.info(String.format("************************* Spider query executed(%s)", pstmt.toString()));


                    /* Extracts data from resultSet and appends ErrorList to spiderData entity (by arguments)*/
                    //DataExtractor.getData(resultSet, spiderData);

                    /*if (!resultSet.isBeforeFirst()) {
                        log.info("***************************** There is no Spider errors *****************");
                        pon.setNoSpiderErrorsPresent(true);

                    }*/

                    while (resultSet.next()) {


                        String fullName = resultSet.getString("Task");
                        String error = resultSet.getString("JAVA_CLASS_ERROR");
                        String fullQuery = pstmt.toString();


                        MapError spiderError = new MapError(fullName, error, fullQuery);
                        spiderErrors.add(spiderError);
                    }


                    resultSet.close();


                    pstmt.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }


            }
            con.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            //-log.error(String.format("!!!!!!!!!!!!!!!!!!!!!!!!! Spider request failed with (%s)", pon.getQueryFull()));
            pon.setOutput(e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            pon.setOutput(e.getMessage());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            pon.setOutput(e.getMessage());
        }


        return spiderErrors;
    }


}