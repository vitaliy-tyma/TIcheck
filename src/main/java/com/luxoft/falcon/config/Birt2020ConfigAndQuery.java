package com.luxoft.falcon.config;

import com.luxoft.falcon.config.inter.ConfigAndQueryInterface;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/* Entity/Data/POJO to connect to the BIRT Gen.2020 */

@Data
public class Birt2020ConfigAndQuery implements ConfigAndQueryInterface {

    private static Birt2020ConfigAndQuery _instance = new Birt2020ConfigAndQuery();

    private Birt2020ConfigAndQuery() {
    }

    public static synchronized Birt2020ConfigAndQuery getInstance() {
        return _instance;
    }

    @Getter @Setter
    private String jdbcDriver;

    @Getter @Setter
    private String jdbcUrl;// = "jdbc:mysql://himdlxbirt01:3306/ndsreport_new"; //For 2020

    @Getter @Setter
    private String jdbcLogin;// = "readonly";
    @Getter @Setter
    private String jdbcPassword;// = "readonly";


    @Getter @Setter
    private String queryLike;// = " \n" +
//            " SELECT s.name AS Task, t.name AS TEST_NAME, tr.result TEST_RESULT \n" +
//            " FROM ndsreport_new.tests_results tr\n" +
//            " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
//            " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
//            " WHERE s.name LIKE ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
//            " AND t.name = ?\n" +
//            //TODO Clarify with the exit_code parameter
// //           " AND tr.exit_code = 0\n" +
//            " ORDER BY TASK, TEST_NAME \n" +
//            " LIMIT ?";

    @Getter @Setter
    private String queryAccurate;// = " \n" +
//            " SELECT s.name AS Task, t.name AS TEST_NAME, tr.result TEST_RESULT \n" +
//            " FROM ndsreport_new.tests_results tr\n" +
//            " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
//            " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
//            " WHERE s.name = ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
//            " AND t.name = ?\n" +
//            //TODO Clarify with the exit_code parameter
////            " AND tr.exit_code = 0\n" +
//            " ORDER BY TASK, TEST_NAME \n" +
//            " LIMIT ?";
}

