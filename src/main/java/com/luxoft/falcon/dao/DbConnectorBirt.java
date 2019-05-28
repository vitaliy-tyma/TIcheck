package com.luxoft.falcon.dao;

import com.luxoft.falcon.config.Birt2010ConfigAndQuery;
import com.luxoft.falcon.config.SpiderConfigAndQuery;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Slf4j
public class DbConnectorBirt {
    private static Connection con;

    public static Connection connectDatabase(
            Birt2010ConfigAndQuery birt2010ConfigAndQuery)
//            String url, String login, String password)
                throws ClassNotFoundException, SQLException {

//            Class.forName("com.mysql.cj.jdbc.Driver");
                    Class.forName(birt2010ConfigAndQuery.getJdbcDriver());
            con = DriverManager.getConnection(
                    birt2010ConfigAndQuery.getJdbcUrl(),
                    birt2010ConfigAndQuery.getJdbcLogin(),
                    birt2010ConfigAndQuery.getJdbcPassword());
//                    url, login, password);
            log.debug("********** Connection to " + birt2010ConfigAndQuery.getJdbcUrl() + " has been established **********");
        return con;
    }
}
