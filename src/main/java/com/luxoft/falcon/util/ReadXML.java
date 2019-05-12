package com.luxoft.falcon.util;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.Checklist;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

public class ReadXML {

    public static Checklist readChecklistFromFile(String fileAbsolutePathAndName) throws ParserConfigurationException, SAXException, IOException {

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


                if (qName.equalsIgnoreCase(MainConfig.getSOURCE_NAME_SPIDER())) {
                    spider = true;
                    birt = false;
                    nds = false;
                }
                if (qName.equalsIgnoreCase(MainConfig.getSOURCE_NAME_BIRT())) {
                    birt = true;
                    spider = false;
                    nds = false;
                }
                if (qName.equalsIgnoreCase(MainConfig.getSOURCE_NAME_NDS())) {
                    nds = true;
                    spider = false;
                    birt = false;
                }
                if (qName.equalsIgnoreCase(MainConfig.getSTEP_NAME())) {
                    step = true;
                }

            }

            public void endElement(String uri,
                                   String localName,
                                   String qName) {
                if (qName.equalsIgnoreCase(MainConfig.getSOURCE_NAME_SPIDER())) {
                    spider = false;
                }
                if (qName.equalsIgnoreCase(MainConfig.getSOURCE_NAME_BIRT())) {
                    birt = false;
                }
                if (qName.equalsIgnoreCase(MainConfig.getSOURCE_NAME_NDS())) {
                    nds = false;
                }
                if (qName.equalsIgnoreCase(MainConfig.getSTEP_NAME())) {
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
                MainConfig.getPATH_DELIMITER() +
                        "resources" +
                        MainConfig.getPATH_DELIMITER() +
                        "checklists" +
                        MainConfig.getPATH_DELIMITER() +
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
