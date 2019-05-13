package com.luxoft.falcon.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Slf4j
public class DbConnectorBirt {
    private static Connection con;

    public static Connection connectDatabase(String url, String login, String password) throws ClassNotFoundException, SQLException {
            con = DriverManager.getConnection( url, login, password);
            log.debug("********** Connection to " + url + " has been established **********");
        return con;
    }
}
