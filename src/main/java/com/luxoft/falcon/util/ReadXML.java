package com.luxoft.falcon.util;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
import lombok.Getter;
import lombok.Setter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

public class ReadXML {

    public static void readMainConfigFromFile(String fileAbsolutePathAndName){
        MainConfig mainConfig = MainConfig.getInstance();


        mainConfig.setPON_NAME_REQUEST("pon_name");
        mainConfig.setPON_ITERATION_REQUEST("pon_iteration");
        mainConfig.setUSE_QUERY_LIKE_REQUEST("use_query_like");
        mainConfig.setCHECKLISTS_REQUEST("checklists");
        mainConfig.setCHECKLISTS_REGRESSION("regression_check");
        mainConfig.setPON_NAME_PREV_REQUEST("prev_pon_name");
        mainConfig.setPON_ITERATION_PREV_REQUEST("prev_pon_iteration");
        mainConfig.setUSE_QUERY_LIKE_PREV_REQUEST("use_query_like_for_prev");
        mainConfig.setQUERY_LIMIT("limit");
        mainConfig.setSTEP_NAME("STEP");
        mainConfig.setSOURCE_NAME_SPIDER("SPIDER");
        mainConfig.setSPIDER_TASK_COL_NAME("Task");
        mainConfig.setSPIDER_JAVA_CLASS_ERROR_COL_NAME("JAVA_CLASS_ERROR");
        mainConfig.setSOURCE_NAME_BIRT("BIRT");
        mainConfig.setBIRT_TASK_COL_NAME("Task");
        mainConfig.setBIRT_TEST_COL_NAME("TEST_NAME");
        mainConfig.setBIRT_TEST_RESULT_NAME("TEST_RESULT");
        mainConfig.setSOURCE_NAME_NDS("NDS");
    }


    public static Checklist readChecklistFromFile(String fileAbsolutePathAndName) throws ParserConfigurationException, SAXException, IOException {

        MainConfig mainConfig = MainConfig.getInstance();

        Checklist checklist = new Checklist();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();


        DefaultHandler handler = new DefaultHandler() {
            boolean spider = false;
            boolean birt = false;
            boolean nds = false;
            boolean step = false;


            public void startElement(String uri,
                                     String localName,
                                     String qName,
                                     Attributes attributes) {


                if (qName.equalsIgnoreCase(mainConfig.getSOURCE_NAME_SPIDER())) {
                    spider = true;
                    birt = false;
                    nds = false;
                }
                if (qName.equalsIgnoreCase(mainConfig.getSOURCE_NAME_BIRT())) {
                    birt = true;
                    spider = false;
                    nds = false;
                }
                if (qName.equalsIgnoreCase(mainConfig.getSOURCE_NAME_NDS())) {
                    nds = true;
                    spider = false;
                    birt = false;
                }
                if (qName.equalsIgnoreCase(mainConfig.getSTEP_NAME())) {
                    step = true;
                }

            }

            public void endElement(String uri,
                                   String localName,
                                   String qName) {
                if (qName.equalsIgnoreCase(mainConfig.getSOURCE_NAME_SPIDER())) {
                    spider = false;
                }
                if (qName.equalsIgnoreCase(mainConfig.getSOURCE_NAME_BIRT())) {
                    birt = false;
                }
                if (qName.equalsIgnoreCase(mainConfig.getSOURCE_NAME_NDS())) {
                    nds = false;
                }
                if (qName.equalsIgnoreCase(mainConfig.getSTEP_NAME())) {
                    step = false;
                }
            }

            public void characters(char ch[], int start, int length) {

                String s = new String(ch, start, length).replace("\n", "").trim();
                if (step) {
                    if (spider) {
                        checklist.addSpiderSteps(s);
                    }
                    if (birt) {
                        checklist.addBirtSteps(s);
                    }
                    if (nds) {
                        checklist.addNdsSteps(s);
                    }
                }
            }
        };


        String currentDir = new java.io.File(".").getCanonicalPath();
        currentDir +=
                mainConfig.getPATH_DELIMITER() +
                        "resources" +
                        mainConfig.getPATH_DELIMITER() +
                        "checklists" +
                        mainConfig.getPATH_DELIMITER() +
                        fileAbsolutePathAndName + ".xml";


        File file = new File(currentDir);
        InputStream inputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(inputStream, "UTF-8");

        InputSource is = new InputSource(reader);
        is.setEncoding("UTF-8");

        saxParser.parse(is, handler);

        return checklist;
    }
}
