package com.luxoft.falcon.dao;

import com.luxoft.falcon.model.SpiderDataClass;
import com.luxoft.falcon.model.ConfigData;
import com.luxoft.falcon.service.DataExtractor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * Contains method to get data from Spider database
 *
 * Data source: Spider PostgreSQL DB on requested Server:port and with credentials
 *
 * Input data: the full name of the PON and iteration #
 * (comes from XML-file or from outer application/class)
 * Note: Market and region are not processed!
 *
 * Transformation: toString and getHTML by template.
 *
 * Output data: SpiderDataClass entity
 * As an option - error list in string format (serialized by toString).
 *
 * In case of exception returns text of error in SpiderDataClass.setJdbcError
 */
@Slf4j
public class SpiderDbConnector {
    private static final int RUN_COLUMN_NUMBER = 1;
    private static final int ITERATION_COLUMN_NUMBER = 2;


    public SpiderDataClass connectDatabase(ConfigData configData)
            throws SQLException, ClassNotFoundException {

        log.debug("********** Run connectDatabase **********");

        SpiderDataClass spiderData = new SpiderDataClass(
                configData.getRun(), configData.getMarket(), configData.getProject(), configData.getIteration());


        try {

            Class.forName(configData.getJdbcDriver());
            Connection con = DriverManager.getConnection(
                    configData.getJdbcUrl(), configData.getJdbcLogin(), configData.getJdbcPassword());
            log.debug("********** Connection to " + configData.getJdbcUrl() + " has been established **********");

            try {

                PreparedStatement pstmt = con.prepareStatement(configData.getQuery());
                pstmt.setString(RUN_COLUMN_NUMBER, configData.getRun());
//                pstmt.setString(RUN_COLUMN_NUMBER, configData.getRun() + "%");
                pstmt.setInt(ITERATION_COLUMN_NUMBER, Integer.valueOf(configData.getIteration()));

                /** Save query to display it in browser*/
                spiderData.setQuery(pstmt.toString());

                ResultSet resultSet = pstmt.executeQuery();


                /** Extracts data from resultSet and appends ErrorList to spiderData entity (by arguments)*/
                DataExtractor.getData(resultSet, spiderData);
                /** Now data must be uploaded into the existing spiderData class*/

                resultSet.close();
                pstmt.close();
            } finally {
                con.close();
            }
        } catch (
                Exception e) {
            log.error(String.valueOf(e));

            /** Set error message for output in HTML format*/
            spiderData.setJdbcError("<font color = red>" + String.valueOf(e) + "</font><br/>");
            /** Send exception to the upper level*/
            throw e;
        }

        return spiderData;
    }
}
