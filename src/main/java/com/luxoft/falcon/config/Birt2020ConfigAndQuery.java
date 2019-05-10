package com.luxoft.falcon.config;

import lombok.Data;
import lombok.Getter;

/**
 * Entity/Data/POJO to connect to the BIRT Gen.2020
 * To be loaded from XML-file
 */

@Data
public class Birt2020ConfigAndQuery {
    @Getter
    private String jdbcDriver = "com.mysql.jdbc.Driver";

    @Getter
    private String jdbcUrl = "jdbc:mysql://himdlxbirt01:3306/ndsreport_new"; //For 2020

    @Getter
    private String jdbcLogin = "readonly";
    @Getter
    private String jdbcPassword = "readonly";


    @Getter
    private String queryLike = " \n" +
            " SELECT s.name AS Task, t.name AS TEST_NAME, tr.result TEST_RESULT \n" +
            " FROM ndsreport_new.tests_results tr\n" +
            " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
            " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
            " WHERE s.name LIKE ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
            " AND t.name = ?\n" +
 //           " AND tr.exit_code = 0\n" +
            " ORDER BY TASK, TEST_NAME \n" +
            " LIMIT ?";

    @Getter
    private String queryAccurate = " \n" +
            " SELECT s.name AS Task, t.name AS TEST_NAME, tr.result TEST_RESULT \n" +
            " FROM ndsreport_new.tests_results tr\n" +
            " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
            " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
            " WHERE s.name = ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
            " AND t.name = ?\n" +
//            " AND tr.exit_code = 0\n" +
            " ORDER BY TASK, TEST_NAME \n" +
            " LIMIT ?";
}

