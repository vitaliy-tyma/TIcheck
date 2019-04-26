package com.luxoft.falcon.dao;

import com.luxoft.falcon.model.ConfigDataBirt2010;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
public class DbConnectorBirt {

    private static Connection con;

    public static Connection connectDatabase(String url, String login, String password) throws ClassNotFoundException, SQLException {

        log.debug("********** Run connectDatabase **********");

            //Class.forName(configData.getJdbcDriver());
            con = DriverManager.getConnection( url, login, password);
            log.info("********** Connection to " + url + " has been established **********");




        return con;
    }
}
