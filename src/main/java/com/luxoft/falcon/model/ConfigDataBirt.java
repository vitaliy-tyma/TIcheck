package com.luxoft.falcon.model;

import lombok.Data;
import lombok.Getter;

/** Entity/Data/POJO to connect to the SPIDER
 * To be loaded from XML-file*/

@Data
public class ConfigDataBirt {
    @Getter
    private String jdbcDriver = "org.mysql.jdbc.Driver";
    @Getter
    private String jdbcUrl = "jdbc:mysql://himdlxbirt01:3306/ndsreport_new";
    @Getter
    private String jdbcLogin = "readonly";
    @Getter
    private String jdbcPassword = "readonly";


    @Getter
    private String queryLike = " " +
//            "        SELECT ftel.task AS Task, ftel.revision AS Revision, fcel.java_class_error AS JAVA_CLASS_ERROR\n" +
//            "        FROM spider_bmd.full_task_error_list ftel\n" +
//            "        JOIN spider_bmd.full_compiler_error_list fcel ON ftel.java_class_error_id = fcel.id\n" +
//            "        WHERE ftel.task LIKE ?\n" +
//            "        AND ftel.revision = ?\n" +
//            "        AND fcel.java_class_error = ?\n" +
//            "        ORDER BY Task, Revision, JAVA_CLASS_ERROR\n" +
//            "        LIMIT ?";
    " SELECT s.name, t.name, tr.result FROM ndsreport_new.tests_results tr\n" +
    " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
    " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
    " WHERE s.name LIKE ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
    //NO ITERATION/REVISION IN SEPARATE COLUMN!!!!!!!!!!!!!!
    " AND t.name = ?\n" +
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

//    @Getter
//    @Setter
//    private String queryFinal = "";




}
