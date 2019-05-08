package com.luxoft.falcon.dao;

import com.luxoft.falcon.config.ConfigAndQueryForSpider;
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
public class DbConnectorSpider {
    private static Connection con;

    public static Connection connectDatabase(ConfigAndQueryForSpider configData) throws ClassNotFoundException, SQLException {
            Class.forName(configData.getJdbcDriver());
            con = DriverManager.getConnection(
                    configData.getJdbcUrl(),
                    configData.getJdbcLogin(),
                    configData.getJdbcPassword());
            log.debug("********** Connection to " + configData.getJdbcUrl() + " has been established **********");
        return con;
    }
}
