package com.luxoft.falcon.configuration;

import lombok.Getter;
import java.io.File;


public class MainConfig {
    @Getter
    private static final String PATH_DELIMITER = File.separator;
    @Getter
    private static final String CONFIG_PATH = "src" + PATH_DELIMITER +
                                              "main" + PATH_DELIMITER +
                                              "resources" + PATH_DELIMITER;

    @Getter
    private static final String CONFIG_FILE_NAME = "TIcheck.xml";
    @Getter
    private static final String SOURCE_NAME_SPIDER = "spider";
    @Getter
    private static final String SOURCE_NAME_LOCALHOST = "localhost";

}
