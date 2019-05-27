package com.luxoft.falcon.config;

import lombok.*;
import java.io.File;

public final class MainConfig {

    private static MainConfig _instance = new MainConfig();

    private MainConfig() {
        this.configIsLoaded = false;
    }

    public static synchronized MainConfig getInstance() {
        return _instance;
    }

    @Getter
    private static String PATH_DELIMITER = File.separator;
    @Getter
    private static String CONFIG_PATH_AND_NAME =
            "resources" +
            PATH_DELIMITER +
            "config.xml";
    @Getter
    private static String CHECKLISTS_PATH =
            "resources" +
            PATH_DELIMITER +
            "checklists";

    @Getter @Setter
    private String PON_NAME_REQUEST;
    @Getter @Setter
    private String PON_ITERATION_REQUEST;
    @Getter @Setter
    private String USE_QUERY_LIKE_REQUEST;
    @Getter @Setter
    private String CHECKLISTS_REQUEST;
    @Getter @Setter
    private String CHECKLISTS_REGRESSION;
    @Getter @Setter
    private String PON_NAME_PREV_REQUEST;
    @Getter @Setter
    private String PON_ITERATION_PREV_REQUEST;
    @Getter @Setter
    private String USE_QUERY_LIKE_PREV_REQUEST;
    @Getter @Setter
    private String QUERY_LIMIT;

    @Getter @Setter
    private String STEP_NAME;

    @Getter @Setter
    private String CHECKLIST_NAME;

    @Getter @Setter
    private String SOURCE_NAME_SPIDER;
    @Getter @Setter
    private String SPIDER_TASK_COL_NAME;
    @Getter @Setter
    private String SPIDER_JAVA_CLASS_ERROR_COL_NAME;

    @Getter @Setter
    private String SOURCE_NAME_BIRT;
    @Getter @Setter
    private String BIRT_TASK_COL_NAME;
    @Getter @Setter
    private String BIRT_TEST_COL_NAME;
    @Getter @Setter
    private String BIRT_TEST_RESULT_NAME;

    @Getter @Setter
    private String SOURCE_NAME_NDS;

//    @Getter
    @Setter
    private boolean MULTITHREAD;
    public boolean getMULTITHREAD() {
        return this.MULTITHREAD;
    }

//    @Getter
    @Setter
    private boolean configIsLoaded;// = false;
    public boolean getConfigIsLoaded() {
        return this.configIsLoaded;
    }


}
