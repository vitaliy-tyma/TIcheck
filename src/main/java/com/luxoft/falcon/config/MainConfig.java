package com.luxoft.falcon.config;


import lombok.*;
import java.io.File;
//TODO Store all items in separate XML file and load it's content at start and on request.

@Getter
public final class MainConfig {

    private static MainConfig _instance = null;

    private MainConfig() {}

    public static synchronized MainConfig getInstance() {
        if (_instance == null)
            _instance = new MainConfig();
        return _instance;
    }



    @Getter
    private static String PATH_DELIMITER = File.separator;
    @Getter
    private static String CONFIG_PATH_AND_NAME =
            "resources" +
            PATH_DELIMITER +
            "config.xml";

    @Getter @Setter
    private static String PON_NAME_REQUEST = "pon_name";
    @Getter @Setter
    private static String PON_ITERATION_REQUEST = "pon_iteration";
    @Getter @Setter
    private static String USE_QUERY_LIKE_REQUEST = "use_query_like";
    @Getter @Setter
    private static String CHECKLISTS_REQUEST = "checklists";
    @Getter @Setter
    private static String CHECKLISTS_REGRESSION = "regression_check";
    @Getter @Setter
    private static String PON_NAME_PREV_REQUEST = "prev_pon_name";
    @Getter @Setter
    private static String PON_ITERATION_PREV_REQUEST = "prev_pon_iteration";
    @Getter @Setter
    private static String USE_QUERY_LIKE_PREV_REQUEST = "use_query_like_for_prev";
    @Getter @Setter
    private static String QUERY_LIMIT = "limit";

    @Getter @Setter
    private static String STEP_NAME = "STEP";

    @Getter @Setter
    private static String SOURCE_NAME_SPIDER = "SPIDER";
    @Getter @Setter
    private static String SPIDER_TASK_COL_NAME = "Task";
    @Getter @Setter
    private static String SPIDER_JAVA_CLASS_ERROR_COL_NAME = "JAVA_CLASS_ERROR";

    @Getter @Setter
    private static String SOURCE_NAME_BIRT = "BIRT";
    @Getter @Setter
    private static String BIRT_TASK_COL_NAME = "Task";
    @Getter @Setter
    private static String BIRT_TEST_COL_NAME = "TEST_NAME";
    @Getter @Setter
    private static  String BIRT_TEST_RESULT_NAME = "TEST_RESULT";

    @Getter @Setter
    private static String SOURCE_NAME_NDS = "NDS";

}
