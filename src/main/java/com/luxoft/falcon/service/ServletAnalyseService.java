package com.luxoft.falcon.service;

import com.luxoft.falcon.configuration.MainConfig;
import com.luxoft.falcon.model.ConfigData;
import com.luxoft.falcon.util.ReadXmlFile;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Is used to process TI checklist */
@Slf4j
public class ServletAnalyseService {
//    private static final String CONFIG_FILE_NAME = MainConfig.getCONFIG_FILE_NAME();
    private static final String CONFIG_PATH =
                    MainConfig.getCONFIG_PATH()+
                    MainConfig.getCONFIG_FILE_NAME();

    /**Shows only ONE config - must be updated to process whole XML-file!!!!!!!!!!!*/
    public static String service(String sourceName) {

        StringBuilder result = new StringBuilder();
        ReadXmlFile rXml = new ReadXmlFile();
        Path currentRelativePath = null;

        try {
            /** Get current path to be displayed in case of error */
            currentRelativePath = Paths.get("");

            ConfigData configData = rXml.readXmlFile(CONFIG_PATH, sourceName);

            /*********************************************/
            result.append("<p>Data from <b>" + CONFIG_PATH + "</b> and SOURCE <b>" + sourceName + "</b>:</p>");
            result.append("<p>");

            /** For output in HTML format*/
            result.append("<b>Run</b> = " + configData.getRun() + "<br/>");
            result.append("<b>Market</b> = " + configData.getMarket() + "<br/>");
            result.append("<b>Project</b> = " + configData.getProject() + "<br/>");
            result.append("<b>Iteration</b> = " + configData.getIteration() + "<br/><br/>");
            result.append("<b>JdbcDriver</b> = " + configData.getJdbcDriver() + "<br/>");
            result.append("<b>JdbcUrl</b> = " + configData.getJdbcUrl() + "<br/>");
            result.append("<b>JdbcLogin</b> = " + configData.getJdbcLogin() + "<br/>");
            result.append("<b>JdbcPassword</b> = " + configData.getJdbcPassword() + "<br/><br/>");
            result.append("<b>Query for SPIDER</b> = <font color = green>" + configData.getQuery() + "</font><br/>");
            result.append("</p>");

        } catch (Exception e) {
            log.error(String.valueOf(e));

            result.append("<font color = red>" + String.valueOf(e) + "</font>" +
                    "<br/>" +
                    "File <b>" + CONFIG_PATH + "</b> must be in <b>" +
                    currentRelativePath.toAbsolutePath().toString() + "</b>");
        }
        return result.toString();
    }
}
