/*
 * Project        Checklist Analyser
 * (c) copyright  2019
 * Company
 *
 *        All rights reserved
 *
 *
 * File           SQLiteDataSource.java
 * Creation date  2019-05-14
 */

package com.luxoft.falcon.dao;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * SQLite data source
 *
 * @author NDriuchatyi
 */
public class SQLiteDataSource
{
    private Connection connection;

    public SQLiteDataSource(Path p)
    {
        try
        {
            Class.forName(org.sqlite.JDBC.class.getCanonicalName());
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:" + p.normalize().toString());
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Could not create connection to " + p.toString(), e);
        }
    }

    /**
     * Get connection to the SQLite file
     *
     * @return connection to the SQLite file
     */
    public Connection getConnection()
    {
        return connection;
    }
}

