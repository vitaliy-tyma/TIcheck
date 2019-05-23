package com.luxoft.falcon.util;

import com.luxoft.falcon.config.*;
import com.luxoft.falcon.config.inter.ConfigAndQueryInterface;
import com.luxoft.falcon.model.Checklist;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.*;

@Slf4j
public class ReadXML {
    private static MainConfig mainConfig = MainConfig.getInstance();
    private static SpiderConfigAndQuery spiderConfigAndQuery = SpiderConfigAndQuery.getInstance();
    private static Birt2010ConfigAndQuery birt2010ConfigAndQuery = Birt2010ConfigAndQuery.getInstance();
    private static Birt2020ConfigAndQuery birt2020ConfigAndQuery = Birt2020ConfigAndQuery.getInstance();
    private static BirtQueryToCheckGeneration birtQueryToCheckGeneration = BirtQueryToCheckGeneration.getInstance();

    private static NdsConfigAndQuery ndsConfigAndQuery = NdsConfigAndQuery.getInstance();


    private static final String MAIN = "MAIN";
    private static final String SPIDER = "SPIDER";
    private static final String BIRT2010 = "BIRT2010";
    private static final String BIRT2020 = "BIRT2020";
    private static final String BIRT_GENERATION_SELECTOR = "BIRT_GENERATION_SELECTOR";
    private static final String NDS = "NDS";


    public static void readMainConfigFromFile(String fileAbsolutePathAndName) {


        try {
            File fXmlFile = new File(fileAbsolutePathAndName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName(MAIN);

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    mainConfig.setPON_NAME_REQUEST(
                            eElement.getElementsByTagName(
                                    "PON_NAME_REQUEST").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setPON_ITERATION_REQUEST(
                            eElement.getElementsByTagName(
                                    "PON_ITERATION_REQUEST").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setUSE_QUERY_LIKE_REQUEST(
                            eElement.getElementsByTagName(
                                    "USE_QUERY_LIKE_REQUEST").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setCHECKLISTS_REQUEST(
                            eElement.getElementsByTagName(
                                    "CHECKLISTS_REQUEST").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setCHECKLISTS_REGRESSION(
                            eElement.getElementsByTagName(
                                    "CHECKLISTS_REGRESSION").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setPON_NAME_PREV_REQUEST(
                            eElement.getElementsByTagName(
                                    "PON_NAME_PREV_REQUEST").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setPON_ITERATION_PREV_REQUEST(
                            eElement.getElementsByTagName(
                                    "PON_ITERATION_PREV_REQUEST").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setUSE_QUERY_LIKE_PREV_REQUEST(
                            eElement.getElementsByTagName(
                                    "USE_QUERY_LIKE_PREV_REQUEST").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setQUERY_LIMIT(
                            eElement.getElementsByTagName(
                                    "QUERY_LIMIT").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setSTEP_NAME(
                            eElement.getElementsByTagName(
                                    "STEP_NAME").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setSOURCE_NAME_SPIDER(
                            eElement.getElementsByTagName(
                                    "SOURCE_NAME_SPIDER").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setSPIDER_TASK_COL_NAME(
                            eElement.getElementsByTagName(
                                    "SPIDER_TASK_COL_NAME").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setSPIDER_JAVA_CLASS_ERROR_COL_NAME(
                            eElement.getElementsByTagName(
                                    "SPIDER_JAVA_CLASS_ERROR_COL_NAME").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setSOURCE_NAME_BIRT(
                            eElement.getElementsByTagName(
                                    "SOURCE_NAME_BIRT").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setBIRT_TASK_COL_NAME(
                            eElement.getElementsByTagName(
                                    "BIRT_TASK_COL_NAME").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setBIRT_TEST_COL_NAME(
                            eElement.getElementsByTagName(
                                    "BIRT_TEST_COL_NAME").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setBIRT_TEST_RESULT_NAME(
                            eElement.getElementsByTagName(
                                    "BIRT_TEST_RESULT_NAME").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    mainConfig.setSOURCE_NAME_NDS(
                            eElement.getElementsByTagName(
                                    "SOURCE_NAME_NDS").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());


                }
            }


            nList = doc.getElementsByTagName(SPIDER);
            fillConfig(nList, spiderConfigAndQuery);

            nList = doc.getElementsByTagName(BIRT2010);
            fillConfig(nList, birt2010ConfigAndQuery);

            nList = doc.getElementsByTagName(BIRT2020);
            fillConfig(nList, birt2020ConfigAndQuery);

            nList = doc.getElementsByTagName(NDS);
            fillConfig(nList, ndsConfigAndQuery);

            nList = doc.getElementsByTagName(BIRT_GENERATION_SELECTOR);
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    birtQueryToCheckGeneration.setG2010(
                            eElement.getElementsByTagName(
                                    "g2010").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                    birtQueryToCheckGeneration.setG2020(
                            eElement.getElementsByTagName(
                                    "g2020").
                                    item(0).getTextContent().
                                    replace("\n", "").trim());
                }
            }
            mainConfig.setConfigIsLoaded(true);
            log.info("************************ MainConfig has been read ************************");

        } catch (
                Exception e) {
            /* Set all values as default*/
            setDefaultValuesBecauseOfException();
            log.error("Read XML failed with the error " + e.getMessage());
        }
    }


    private static void setDefaultValuesBecauseOfException() {

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

        spiderConfigAndQuery.setJdbcDriver("org.postgresql.Driver");
        spiderConfigAndQuery.setJdbcUrl("jdbc:postgresql://himdlxspider01:5432/DBPROD");
        spiderConfigAndQuery.setJdbcLogin("readonly");
        spiderConfigAndQuery.setJdbcPassword("readonly");
        spiderConfigAndQuery.setQueryLike(" \n" +
                "        SELECT ftel.task AS Task, ftel.revision AS Revision, fcel.java_class_error AS JAVA_CLASS_ERROR\n" +
                "        FROM spider_bmd.full_task_error_list ftel\n" +
                "        JOIN spider_bmd.full_compiler_error_list fcel ON ftel.java_class_error_id = fcel.id\n" +
                "        WHERE ftel.task LIKE ?\n" +
                "        AND ftel.revision = ?\n" +
                "        AND fcel.java_class_error = ?\n" +
                "        ORDER BY Task, Revision, JAVA_CLASS_ERROR\n" +
                "        LIMIT ?");
        spiderConfigAndQuery.setQueryAccurate(" \n" +
                "        SELECT ftel.task AS Task, ftel.revision AS Revision, fcel.java_class_error AS JAVA_CLASS_ERROR\n" +
                "        FROM spider_bmd.full_task_error_list ftel\n" +
                "        JOIN spider_bmd.full_compiler_error_list fcel ON ftel.java_class_error_id = fcel.id\n" +
                "        WHERE ftel.task = ?\n" +
                "        AND ftel.revision = ?\n" +
                "        AND fcel.java_class_error = ?\n" +
                "        ORDER BY Task, Revision, JAVA_CLASS_ERROR\n" +
                "        LIMIT ?");

        birt2010ConfigAndQuery.setJdbcDriver("com.mysql.jdbc.Driver");
        birt2010ConfigAndQuery.setJdbcUrl("jdbc:mysql://himdlxbirt01:3306/ndsreport"); //For 2010
        birt2010ConfigAndQuery.setJdbcLogin("readonly");
        birt2010ConfigAndQuery.setJdbcPassword("readonly");
        birt2010ConfigAndQuery.setQueryLike(" \n" +
                " SELECT s.testsuitename AS Task, td.name AS TEST_NAME, tr.testresult \n" +
                " TEST_RESULT FROM ndsreport.test t\n" +
                " JOIN ndsreport.testsuite s ON s.id = t.testsuite_id\n" +
                " JOIN ndsreport.testresult tr ON tr.id = t.testresult_id\n" +
                " JOIN ndsreport.testdescription td ON td.id = t.testdescription_id\n" +
                " WHERE s.testsuitename LIKE ?\n" +//ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
                " AND td.name = ?\n" +
//TODO Clarify with the exitcode parameter
//          " AND tr.exitcode = 0\n" +
                " ORDER BY TASK, TEST_NAME \n" +
                " LIMIT ?");
        birt2010ConfigAndQuery.setQueryAccurate(" \n" +
                " SELECT s.testsuitename AS Task, td.name AS TEST_NAME, tr.testresult TEST_RESULT \n" +
                " FROM ndsreport.test t\n" +
                " JOIN ndsreport.testsuite s ON s.id = t.testsuite_id\n" +
                " JOIN ndsreport.testresult tr ON tr.id = t.testresult_id\n" +
                " JOIN ndsreport.testdescription td ON td.id = t.testdescription_id\n" +
                " WHERE s.testsuitename = ?\n" +
                " AND td.name = ?\n" +
//TODO Clarify with the exitcode parameter
//          " AND tr.exitcode = 0\n" +
                " ORDER BY TASK, TEST_NAME \n" +
                " LIMIT ?");

        birt2020ConfigAndQuery.setJdbcDriver("com.mysql.jdbc.Driver");
        birt2020ConfigAndQuery.setJdbcUrl("jdbc:mysql://himdlxbirt01:3306/ndsreport_new"); //For 2010
        birt2020ConfigAndQuery.setJdbcLogin("readonly");
        birt2020ConfigAndQuery.setJdbcPassword("readonly");
        birt2020ConfigAndQuery.setQueryLike(" \n" +
                " SELECT s.name AS Task, t.name AS TEST_NAME, tr.result TEST_RESULT \n" +
                " FROM ndsreport_new.tests_results tr\n" +
                " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
                " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
                " WHERE s.name LIKE ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
                " AND t.name = ?\n" +
//TODO Clarify with the exit_code parameter
//          " AND tr.exit_code = 0\n" +
                " ORDER BY TASK, TEST_NAME \n" +
                " LIMIT ?");
        birt2020ConfigAndQuery.setQueryAccurate(" \n" +
                " SELECT s.name AS Task, t.name AS TEST_NAME, tr.result TEST_RESULT \n" +
                " FROM ndsreport_new.tests_results tr\n" +
                " JOIN ndsreport_new.suites s ON s.id = tr.suite_id\n" +
                " JOIN ndsreport_new.tests t ON t.id = tr.test_id\n" +
                " WHERE s.name = ?\n" + //ITERATION/REVISION IS THE LAST SYMBOL OF NAME!!!!
                " AND t.name = ?\n" +
//TODO Clarify with the exit_code parameter
//          " AND tr.exit_code = 0\n" +
                " ORDER BY TASK, TEST_NAME \n" +
                " LIMIT ?");


        birtQueryToCheckGeneration.setG2010(
                "SELECT * FROM ndsreport.testsuite WHERE testsuitename LIKE ? LIMIT ?");
        birtQueryToCheckGeneration.setG2020(
                "SELECT * FROM ndsreport_new.suites WHERE name LIKE ? LIMIT ?");


        //NDS defaults are not entered yet!!!!!!!!!!!!!
    }


    private static void fillConfig(NodeList nList, ConfigAndQueryInterface configClass) {

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                configClass.setJdbcDriver(
                        eElement.getElementsByTagName(
                                "jdbcDriver").
                                item(0).getTextContent().
                                replace("\n", "").trim());
                configClass.setJdbcUrl(
                        eElement.getElementsByTagName(
                                "jdbcUrl").
                                item(0).getTextContent().
                                replace("\n", "").trim());
                configClass.setJdbcLogin(
                        eElement.getElementsByTagName(
                                "jdbcLogin").
                                item(0).getTextContent().
                                replace("\n", "").trim());
                configClass.setJdbcPassword(
                        eElement.getElementsByTagName(
                                "jdbcPassword").
                                item(0).getTextContent().
                                replace("\n", "").trim());
                configClass.setQueryLike(
                        eElement.getElementsByTagName(
                                "queryLike").
                                item(0).getTextContent().
                                replace("\n", "").trim());
                configClass.setQueryAccurate(
                        eElement.getElementsByTagName(
                                "queryAccurate").
                                item(0).getTextContent().
                                replace("\n", "").trim());
            }
        }


    }


    /* Read checklist from file by given name using SAX parser - JUST FOR EXAMPLE*/
    public static Checklist readChecklistFromFile(String fileAbsolutePathAndName)
            throws ParserConfigurationException, SAXException, IOException {

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

        try {
            File file = new File(currentDir);
            InputStream inputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");

            /* Parse checklist's file to set object's values */
            saxParser.parse(is, handler);

        } catch (Exception e) {

        }
        return checklist;
    }
}
