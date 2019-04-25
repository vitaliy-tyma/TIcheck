package com.luxoft.falcon.dao;

import com.luxoft.falcon.model.ConfigDataSpider;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * Contains method to get data from Spider database
 * <p>
 * Data source: Spider PostgreSQL DB on requested Server:port and with credentials
 * <p>
 * Input data: the full name of the PON and iteration #
 * (comes from XML-file or from outer application/class)
 * Note: Market and region are not processed!
 * <p>
 * Transformation: toString and getHTML by template.
 * <p>
 * Output data: SpiderDataClass entity
 * As an option - error list in string format (serialized by toString).
 * <p>
 * In case of exception returns text of error in SpiderDataClass.setJdbcError
 */
@Slf4j
public class SpiderDbConnector {

    private static Connection con;

    public static Connection connectDatabase(ConfigDataSpider configData) throws ClassNotFoundException, SQLException {

        log.debug("********** Run connectDatabase **********");

            Class.forName(configData.getJdbcDriver());
            con = DriverManager.getConnection(
                    configData.getJdbcUrl(),
                    configData.getJdbcLogin(),
                    configData.getJdbcPassword());
            log.info("********** Connection to " + configData.getJdbcUrl() + " has been established **********");




        return con;
    }
}
