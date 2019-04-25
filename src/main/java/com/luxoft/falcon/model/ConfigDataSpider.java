package com.luxoft.falcon.model;

import lombok.*;

/** Entity/Data/POJO to connect to the SPIDER
 * To be loaded from XML-file*/

@Data
public class ConfigDataSpider {
    @Getter
    private String jdbcDriver = "org.postgresql.Driver";
    @Getter
    private String jdbcUrl = "jdbc:postgresql://himdlxspider01:5432/DBPROD";
    @Getter
    private String jdbcLogin = "readonly";
    @Getter
    private String jdbcPassword = "readonly";


    @Getter
    private String queryLike = " " +
            "        SELECT ftel.task AS Task, ftel.revision AS Revision, fcel.java_class_error AS JAVA_CLASS_ERROR\n" +
            "        FROM spider_bmd.full_task_error_list ftel\n" +
            "        JOIN spider_bmd.full_compiler_error_list fcel ON ftel.java_class_error_id = fcel.id\n" +
            "        WHERE ftel.task LIKE ?\n" +
            "        AND ftel.revision = ?\n" +
            "        AND fcel.java_class_error = ?\n" +
            "        ORDER BY Task, Revision, JAVA_CLASS_ERROR\n" +
            "        LIMIT ?";



    @Getter
    private String queryAccurate = " " +
            "        SELECT ftel.task AS Task, ftel.revision AS Revision, fcel.java_class_error AS JAVA_CLASS_ERROR\n" +
            "        FROM spider_bmd.full_task_error_list ftel\n" +
            "        JOIN spider_bmd.full_compiler_error_list fcel ON ftel.java_class_error_id = fcel.id\n" +
            "        WHERE ftel.task = ?\n" +
            "        AND ftel.revision = ?\n" +
            "        AND fcel.java_class_error = ?\n" +
            "        ORDER BY Task, Revision, JAVA_CLASS_ERROR\n" +
            "        LIMIT ?";

//    @Getter
//    @Setter
//    private String queryFinal = "";




}
