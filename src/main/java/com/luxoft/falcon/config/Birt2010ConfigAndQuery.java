package com.luxoft.falcon.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity/Data/POJO to connect to the BIRT Gen.2010
 * To be loaded from XML-file
 */

@Data
public class Birt2010ConfigAndQuery {
    @Getter
    private String jdbcDriver = "com.mysql.jdbc.Driver";

    @Getter
    private String jdbcUrl = "jdbc:mysql://himdlxbirt01:3306/ndsreport"; //For 2010

    @Getter
    private String jdbcLogin = "readonly";
    @Getter
    private String jdbcPassword = "readonly";


    @Getter
    private String queryLike = " \n" +
            " SELECT s.testsuitename AS Task, td.name AS TEST_NAME, tr.testresult \n" +
            " TEST_RESULT FROM ndsreport.test t\n" +
            " JOIN ndsreport.testsuite s ON s.id = t.testsuite_id\n" +
            " JOIN ndsreport.testresult tr ON tr.id = t.testresult_id\n" +
            " JOIN ndsreport.testdescription td ON td.id = t.testdescription_id\n" +
            " WHERE s.testsuitename LIKE ?\n" +//ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
            " AND td.name = ?\n" +
  //          " AND tr.exitcode = 0\n" +
            " ORDER BY TASK, TEST_NAME \n" +
            " LIMIT ?";


    @Getter
    private String queryAccurate = " \n" +
            " SELECT s.testsuitename AS Task, td.name AS TEST_NAME, tr.testresult TEST_RESULT \n" +
            " FROM ndsreport.test t\n" +
            " JOIN ndsreport.testsuite s ON s.id = t.testsuite_id\n" +
            " JOIN ndsreport.testresult tr ON tr.id = t.testresult_id\n" +
            " JOIN ndsreport.testdescription td ON td.id = t.testdescription_id\n" +
            " WHERE s.testsuitename = ?\n" +
            " AND td.name = ?\n" +
//            " AND tr.exitcode = 0\n" +
            " ORDER BY TASK, TEST_NAME \n" +
            " LIMIT ?";
}

