package com.luxoft.falcon.service;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.dao.DbConnectorBirt;
import com.luxoft.falcon.model.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;


import com.luxoft.falcon.model.QueryToCheckGeneration;

/**
 * Is used to process TI checklist by data source
 */
@Slf4j
public class ServiceAnalyseBirt {

    private static Connection con = null;
    private static ConfigDataBirt2010 configDataBirt2010 = new ConfigDataBirt2010();
    private static ConfigDataBirt2020 configDataBirt2020 = new ConfigDataBirt2020();
    private static QueryToCheckGeneration queryToCheckGeneration = new QueryToCheckGeneration();


    public static LinkedList<MapError> processBirt(Checklist checklist, Pon pon) {

        LinkedList<MapError> birtErrors = new LinkedList<>();
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



            PreparedStatement pstmtChecker;
            ResultSet resultSetChecker;

//2010

            String queryToCheck = queryToCheckGeneration.getG2010();
            pstmtChecker = con.prepareStatement(queryToCheck);
            pstmtChecker.setString(1, "%" + pon.getName() + "%");
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



//            PreparedStatement pstmtChecker;
//            ResultSet resultSetChecker;
            queryToCheck = queryToCheckGeneration.getG2020();
            pstmtChecker = con.prepareStatement(queryToCheck);
            pstmtChecker.setString(1, "%" + pon.getName() + "%");
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
                for (Map.Entry<String, Boolean> entry : checklist.getBirtSteps().entrySet()) {
//                log.info(String.format("*** Iterate over birt steps [{}:{}] ***"),
//                        entry.getKey(), entry.getValue().toString());


                    PreparedStatement pstmt;
                    ResultSet resultSet;


                    try {
                        if (pon.getAutocomplete()) {
                            pstmt = con.prepareStatement(queryLike);
                            pstmt.setString(1, "%" + pon.getName() + "%" + Integer.valueOf(pon.getIteration()));
                        } else {
                            pstmt = con.prepareStatement(queryAccurate);
                            pstmt.setString(1, pon.getName() + "_R" + Integer.valueOf(pon.getIteration()));
                        }

                        //pstmt.setInt(2, pon.getIteration());
                        pstmt.setString(2, entry.getKey());
                        pstmt.setInt(3, MainConfig.getQUERY_LIMIT());


                        /* Save query to display it in browser*/
                        //configDataSpider.setQueryFinal(pstmt.toString());
                        //-pon.setQueryFull(pstmt.toString());
                        //log.info("SQL = " + pstmt.toString());


                        resultSet = pstmt.executeQuery();
                        log.info(String.format("************************* Birt query executed(%s)", pstmt.toString()));
                        /* Mark the item in checklist if query has been executed!*/
                        entry.setValue(true);


                        /* Extracts data from resultSet and appends ErrorList to spiderData entity (by arguments)*/
                        //DataExtractor.getData(resultSet, spiderData);

                    /*if (!resultSet.isBeforeFirst()) {
                        log.info("***************************** There is no Birt errors *****************");
                        pon.setNoBirtErrorsPresent(true);

                    }*/

                        while (resultSet.next()) {


                            String fullName = resultSet.getString("Task");
                            String testName = resultSet.getString("TEST_NAME");
                            String fullQuery = pstmt.toString();


                            MapError birtError = new MapError(fullName, testName, fullQuery);
                            birtErrors.add(birtError);
                        }


                        resultSet.close();


                        pstmt.close();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }

            }
            con.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            //-log.error(String.format("!!!!!!!!!! Birt request failed with (%s)", pon.getUsedQuery()));
            pon.setOutputOfErrors(e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            pon.setOutputOfErrors(e.getMessage());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            pon.setOutputOfErrors(e.getMessage());
        }


        return birtErrors;
    }


}