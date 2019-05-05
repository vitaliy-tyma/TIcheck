package com.luxoft.falcon.config;


import lombok.*;
import java.io.File;

@Data
public class MainConfig {
    @Getter
    private static final String PATH_DELIMITER = File.separator;
    @Getter
    private static final String CONFIG_PATH = "resources" + PATH_DELIMITER;

    @Getter
    private static final String CONFIG_FILE_NAME = "config.xml";
    @Getter
    private static final String CHECKLIST_FILE_NAME = "checklist.xml";

    @Getter
    private static final String PON_NAME_REQUEST_PARAMETER_KEY = "pon_name";
    @Getter
    private static final String PON_NAME_REQUEST_PARAMETER_VALUE = "pon_name";
    @Getter
    private static final String PON_ITERATION_REQUEST_PARAMETER_KEY = "pon_iteration";
    @Getter
    private static final String PON_ITERATION_REQUEST_PARAMETER_VALUE = "pon_iteration";

    @Getter
    private static final String AUTOCOMPLETE_PON_REQUEST_PARAMETER_KEY = "autocomplete_pon";
    @Getter
    private static final String AUTOCOMPLETE_PON_REQUEST_PARAMETER_VALUE = "autocomplete_pon";

    @Getter
    private static final String CHECKLISTS_REQUEST_PARAMETER_KEY = "checklists";
    @Getter
    private static final String CHECKLISTS_REQUEST_PARAMETER_VALUE = "checklists";



    @Getter
    private static final String PON_NAME_PREV_REQUEST_PARAMETER_KEY = "prev_pon_name";
    @Getter
    private static final String PON_NAME_PREV_REQUEST_PARAMETER_VALUE = "prev_pon_name";
    @Getter
    private static final String PON_ITERATION_PREV_REQUEST_PARAMETER_KEY = "prev_pon_iteration";
    @Getter
    private static final String PON_ITERATION_PREV_REQUEST_PARAMETER_VALUE = "prev_pon_iteration";

    @Getter
    private static final String AUTOCOMPLETE_PON_PREV_REQUEST_PARAMETER_KEY = "autocomplete_prev_pon";
    @Getter
    private static final String AUTOCOMPLETE_PON_PREV_REQUEST_PARAMETER_VALUE = "autocomplete_prev_pon";

    @Getter
    private static final String GET_NOK_REQUEST_PARAMETER_KEY = "get_NOK_only";
    @Getter
    private static final String GET_NOK_REQUEST_PARAMETER_VALUE = "get_NOK_only";




    @Getter
    private static final String SOURCE_NAME_SPIDER = "spider";
    @Getter
    private static final String SOURCE_NAME_BIRT = "birt";
    @Getter
    private static final String SOURCE_NAME_NDS = "nds";

    @Getter
    private static final int QUERY_LIMIT = 1000;



    @Getter
    private static final String SPIDER_TASK_COL_NAME = "Task";
    @Getter
    private static final String SPIDER_JAVA_CLASS_ERROR_COL_NAME = "JAVA_CLASS_ERROR";


    @Getter
    private static final String BIRT_TASK_COL_NAME = "Task";
    @Getter
    private static final String BIRT_TEST_COL_NAME = "TEST_NAME";


    @Getter
    private static final String CHECKLISTS_NAME_TI = "TI";

    @Getter
    private static final String CHECKLISTS_NAME_UNDEF = "Undefined";


}
