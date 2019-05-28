package com.luxoft.falcon.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Contains method to get data from Spider database
 *
 * Data source: NDS SQLite DB on requested Server:port and with credentials
 *
 * Input data: the full name of the PON and iteration #
 * (comes from XML-file or from outer application/class)
 * Note: Market and region are not processed!
 *
 * Transformation: toString and getHTML by template.
 *
 * Output data: Nds entity
 *
 * In case of exception returns text of error in SpiderDataClass.setJdbcError
 */
@Slf4j
public class DbConnectorNds {
    private static Connection con;

    public static Connection connectDatabase(String url, String login, String password) throws ClassNotFoundException, SQLException {
            con = DriverManager.getConnection( url, login, password);
            log.debug("********** Connection to " + url + " has been established **********");
        return con;
    }
}
