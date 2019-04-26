package com.luxoft.falcon.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity/Data/POJO to connect to the SPIDER
 * To be loaded from XML-file
 */

@Data
public class ConfigDataBirt2010 {
    @Getter
    private String jdbcDriver = "com.mysql.jdbc.Driver";

    @Getter
    private String jdbcUrl = "jdbc:mysql://himdlxbirt01:3306/ndsreport"; //To be chosen from 2010 or 2020
//    @Getter
//    private String jdbcUrl2010 = "jdbc:mysql://himdlxbirt01:3306/ndsreport";
//    @Getter
//    private String jdbcUrl2020 = "jdbc:mysql://himdlxbirt01:3306/ndsreport_new";

    @Getter
    private String jdbcLogin = "readonly";
    @Getter
    private String jdbcPassword = "readonly";


    @Getter
    private String queryLike = " " +
            " SELECT s.testsuitename AS Task, td.name AS TEST_NAME, tr.testresult  FROM ndsreport.test t\n" +
            " JOIN ndsreport.testsuite s ON s.id = t.testsuite_id\n" +
            " JOIN ndsreport.testresult tr ON tr.id = t.testresult_id\n" +
            " JOIN ndsreport.testdescription td ON td.id = t.testdescription_id\n" +
            " WHERE s.testsuitename LIKE ?\n" +//ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
            //NO ITERATION/REVISION IN SEPARATE COLUMN!!!!!!!!!!!!!!
            " AND td.name = ?\n" +
            " AND tr.testresult = 'NOK'\n" +
            " LIMIT ?";


//    @Getter
//    private String queryLike2020 = " " +
//            " SELECT s.name AS Task, t.name AS TEST_NAME, tr.result FROM ndsreport_new.tests_results tr\n" +
//            " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
//            " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
//            " WHERE s.name LIKE ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
//            //NO ITERATION/REVISION IN SEPARATE COLUMN!!!!!!!!!!!!!!
//            " AND t.name = ?\n" +
//            " AND tr.result = 'NOK'\n" +
//            " LIMIT ?";


    @Getter
    private String queryAccurate = " " +
            " SELECT s.testsuitename AS Task, td.name AS TEST_NAME, tr.testresult  FROM ndsreport.test t\n" +
            " JOIN ndsreport.testsuite s ON s.id = t.testsuite_id\n" +
            " JOIN ndsreport.testresult tr ON tr.id = t.testresult_id\n" +
            " JOIN ndsreport.testdescription td ON td.id = t.testdescription_id\n" +
            " WHERE s.testsuitename = ?\n" +
            " AND td.name = ?\n" +
            " AND tr.testresult = 'NOK'\n" +
            " LIMIT ?";

//    @Getter
//    private String queryAccurate2020 = " " +
//            " SELECT s.name, t.name, tr.result FROM ndsreport_new.tests_results tr\n" +
//            " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
//            " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
//            " WHERE s.name = ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
//            //NO ITERATION/REVISION IN SEPARATE COLUMN!!!!!!!!!!!!!!
//            " AND t.name = ?\n" +
//            " LIMIT ?";

//    @Getter
//    @Setter
//    private String queryToCheckGen = "x";


}

