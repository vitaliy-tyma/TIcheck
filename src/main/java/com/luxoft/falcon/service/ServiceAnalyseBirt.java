package com.luxoft.falcon.service;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.dao.DbConnectorBirt;
import com.luxoft.falcon.dao.DbConnectorSpider;
import com.luxoft.falcon.model.*;
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
public class ServiceAnalyseBirt {

    private static ConfigDataBirt configDataBirt = new ConfigDataBirt();
    private static Connection con = null;


    public static LinkedList<MapError> processBirt(Checklist checklist, Pon pon) {

//        StringBuilder result = new StringBuilder();
        LinkedList<MapError> spiderErrors = new LinkedList<>();

        log.info("**** in ServiceAnalyseBirt.processBirt() ****");


        try {

            con = DbConnectorBirt.connectDatabase(configDataBirt);


            /* Iterate over BIRT*/
            for (Map.Entry<String, Boolean> entry : checklist.getBirtSteps().entrySet()) {
                log.info(String.format("*** Iterate over birt steps [{}:{}] ***"),
                        entry.getKey(), entry.getValue().toString());

                PreparedStatement pstmt;
                ResultSet resultSet;


                try {
                    if (pon.getAutocomplete()) {
                        pstmt = con.prepareStatement(configDataBirt.getQueryLike());
                        pstmt.setString(1, "%" + pon.getName() + "%" + Integer.valueOf(pon.getIteration()));
                    } else {
                        pstmt = con.prepareStatement(configDataBirt.getQueryAccurate());
                        pstmt.setString(1, pon.getName()+ "_R" + Integer.valueOf(pon.getIteration()));
                    }

                    //pstmt.setInt(2, pon.getIteration());
                    pstmt.setString(2, entry.getKey());
                    pstmt.setInt(3, MainConfig.getQUERY_LIMIT());


                    /* Save query to display it in browser*/
                    //configDataSpider.setQueryFinal(pstmt.toString());
                    pon.setQueryFull(pstmt.toString());
                    //log.info("SQL = " + pstmt.toString());


                    resultSet = pstmt.executeQuery();



                    /* Extracts data from resultSet and appends ErrorList to spiderData entity (by arguments)*/
                    //DataExtractor.getData(resultSet, spiderData);

                    if (!resultSet.isBeforeFirst()) {
                        log.info("***************************** There is no Birt errors *****************");
                        pon.setNoSpiderErrorsPresent(true);

                    }

                    while (resultSet.next()) {


                        String fullName = resultSet.getString("Task");
                        String testName = resultSet.getString("TEST_NAME");
                        String fullQuery = pstmt.toString();


                        MapError birtError = new MapError(fullName, testName, fullQuery);
                        spiderErrors.add(birtError);
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
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        } finally {
            log.error(String.format("!!!!!!!!!! Birt connect failed with (%s)", pon.getQueryFull()));

        }


        return spiderErrors;
    }


}