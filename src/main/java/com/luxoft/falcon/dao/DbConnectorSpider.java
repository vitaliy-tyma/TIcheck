package com.luxoft.falcon.dao;

import com.luxoft.falcon.config.SpiderConfigAndQuery;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;


@Slf4j
public class DbConnectorSpider {
    private static Connection con;

    public static Connection connectDatabase(SpiderConfigAndQuery configData) throws ClassNotFoundException, SQLException {
            Class.forName(configData.getJdbcDriver());
            con = DriverManager.getConnection(
                    configData.getJdbcUrl(),
                    configData.getJdbcLogin(),
                    configData.getJdbcPassword());
            log.debug("********** Connection to " + configData.getJdbcUrl() + " has been established **********");
        return con;
    }
}
