package com.luxoft.falcon.model;

import lombok.Data;
import lombok.Getter;

/**
 * Entity/Data/POJO to connect to the BIRT Gen.2020
 * To be loaded from XML-file
 */

@Data
public class ConfigDataBirt2020 {
    @Getter
    private String jdbcDriver = "com.mysql.jdbc.Driver";

    @Getter
    private String jdbcUrl = "jdbc:mysql://himdlxbirt01:3306/ndsreport_new"; //For 2020

    @Getter
    private String jdbcLogin = "readonly";
    @Getter
    private String jdbcPassword = "readonly";


    @Getter
    private String queryLike = " " +
            " SELECT s.name AS Task, t.name AS TEST_NAME, tr.result FROM ndsreport_new.tests_results tr\n" +
            " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
            " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
            " WHERE s.name LIKE ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
            //NO ITERATION/REVISION IN SEPARATE COLUMN!!!!!!!!!!!!!!
            " AND t.name = ?\n" +
            " AND tr.result = 'NOK'\n" +
            " LIMIT ?";

    @Getter
    private String queryAccurate = " " +
            " SELECT s.name, t.name, tr.result FROM ndsreport_new.tests_results tr\n" +
            " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
            " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
            " WHERE s.name = ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
            //NO ITERATION/REVISION IN SEPARATE COLUMN!!!!!!!!!!!!!!
            " AND t.name = ?\n" +
            " LIMIT ?";
}

