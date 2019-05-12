package com.luxoft.falcon.config;


import lombok.*;
import java.io.File;
//TODO Store all items in separate XML file and load it's content at start and on request.

@Data
public class MainConfig {
    @Getter
    private static final String PATH_DELIMITER = File.separator;
//    @Getter
//    private static final String CONFIG_PATH = "resources" + PATH_DELIMITER;
//
//    @Getter
//    private static final String CONFIG_FILE_NAME = "config.xml";
//    @Getter
//    private static final String CHECKLIST_FILE_NAME = "checklists/TI.xml";

    @Getter
    private static final String PON_NAME_REQUEST = "pon_name";

    @Getter
    private static final String PON_ITERATION_REQUEST = "pon_iteration";


    @Getter
    private static final String USE_QUERY_LIKE_REQUEST = "use_query_like";


    @Getter
    private static final String CHECKLISTS_REQUEST = "checklists";


    @Getter
    private static final String CHECKLISTS_REGRESSION = "regression_check";



    @Getter
    private static final String PON_NAME_PREV_REQUEST = "prev_pon_name";

    @Getter
    private static final String PON_ITERATION_PREV_REQUEST = "prev_pon_iteration";


    @Getter
    private static final String USE_QUERY_LIKE_PREV_REQUEST = "use_query_like_for_prev";

    @Getter
    private static final String QUERY_LIMIT = "limit";
    @Getter
    private static final String QUERY_LIMIT_VALUE = "limit";






    @Getter
    private static final String SOURCE_NAME_SPIDER = "SPIDER";
    @Getter
    private static final String SOURCE_NAME_BIRT = "BIRT";
    @Getter
    private static final String SOURCE_NAME_NDS = "NDS";
    @Getter
    private static final String STEP_NAME = "STEP";



    @Getter
    private static final String SPIDER_TASK_COL_NAME = "Task";
    @Getter
    private static final String SPIDER_JAVA_CLASS_ERROR_COL_NAME = "JAVA_CLASS_ERROR";


    @Getter
    private static final String BIRT_TASK_COL_NAME = "Task";
    @Getter
    private static final String BIRT_TEST_COL_NAME = "TEST_NAME";
    @Getter
    private static final String BIRT_TEST_RESULT_NAME = "TEST_RESULT";

//    @Getter
//    private static final String CHECKLISTS_NAME_TI = "TI";
//
//    @Getter
//    private static final String CHECKLISTS_NAME_UNDEF = CHECKLISTS_NAME_TI;


}
