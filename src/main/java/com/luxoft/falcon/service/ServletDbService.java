package com.luxoft.falcon.service;

import com.luxoft.falcon.dao.SpiderDbConnector;
import com.luxoft.falcon.model.ConfigData;
import com.luxoft.falcon.configuration.MainConfig;
import com.luxoft.falcon.model.SpiderDataClass;
import com.luxoft.falcon.util.ReadXmlFile;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class ServletDbService {
    private static final String CONFIG_FILE_NAME = MainConfig.getCONFIG_FILE_NAME();

    public static String servletDbService(String sourceName) {

        SpiderDataClass spiderData = null;
        StringBuilder result = new StringBuilder();
        ReadXmlFile rXml = null;
        Path currentRelativePath = null;

        try {
            currentRelativePath = Paths.get("");
            rXml = new ReadXmlFile();

            ConfigData configData = rXml.readXmlFile(CONFIG_FILE_NAME, sourceName);

            SpiderDbConnector dbConnector = new SpiderDbConnector();
            spiderData = dbConnector.connectDatabase(configData);


            /** For demonstration purposes only - String output*/
            //log.info("********** Result (in String format) of connectLocalDatabase is: " + spiderData.toString());

            /** Output in HTML*/
            result.append("<p>Data from <b>" + configData.getJdbcUrl() + "</b> for</p>");
            result.append("<p>");
            result.append(spiderData.getHtml("<br/>"));
            result.append("</p>");

        } catch (Exception e) {
            log.error(String.valueOf(e));
            /** Set error message for output*/
            result.append("<font color = red>" + String.valueOf(e) + "</font>" + "<br/>" +
                    "See file <b>" + CONFIG_FILE_NAME + "</b> in <b>" +
                    currentRelativePath.toAbsolutePath().toString() + "</b>");
        }
        return result.toString();
    }
}
