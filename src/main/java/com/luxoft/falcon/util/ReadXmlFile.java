package com.luxoft.falcon.util;

import com.luxoft.falcon.model.ConfigData;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Class reads XML file (argument 1 = name)
 * and looks for the source (argument 2 = attribute)
 *
 * Returns ConfigData class which contains all credentials and requested PON-iteration
 */
@Slf4j
public class ReadXmlFile {

    public ConfigData readXmlFile(String configFileName, String sourceName)
            throws IOException, SAXException, ParserConfigurationException

    {
        ConfigData configData = new ConfigData();

        try {
            File fXmlFile = new File(configFileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            /** Note: DOM Parser is slow and will consume a lot of memory when it loads an XML document
             * which contains a lot of data.
             * Please consider SAX parser as solution for it,
             * SAX is faster than DOM and use less memory.
             */
            doc.getDocumentElement().normalize();
            /** System.out.println("Root element :" + doc.getDocumentElement().getNodeName());*/

            NodeList nList = doc.getElementsByTagName("Request");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                /* System.out.println("\nCurrent Element :" + nNode.getNodeName());*/
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    configData.setRun(eElement.getElementsByTagName("run").item(0).getTextContent());
                    configData.setMarket(eElement.getElementsByTagName("market").item(0).getTextContent());
                    configData.setProject(eElement.getElementsByTagName("project").item(0).getTextContent());
                    configData.setIteration(eElement.getElementsByTagName("iteration").item(0).getTextContent());
                }
            }

            /* Iterate on all connections to find first source by it's attribute*/
            NodeList nListConnect = doc.getElementsByTagName("Connect");
            for (int temp = 0; temp < nListConnect.getLength(); temp++) {
                Node nNode = nListConnect.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    if (!eElement.getAttribute("source").equals(sourceName)) {
                        continue;
                    } else {
                        /* System.out.println("Connect source : " + eElement.getAttribute("source"));*/
                        configData.setJdbcDriver(eElement.getElementsByTagName("jdbcDriver").item(0).getTextContent());
                        configData.setJdbcUrl(eElement.getElementsByTagName("jdbcUrl").item(0).getTextContent());
                        configData.setJdbcLogin(eElement.getElementsByTagName("jdbcLogin").item(0).getTextContent());
                        configData.setJdbcPassword(eElement.getElementsByTagName("jdbcPassword").item(0).getTextContent());
                        configData.setQuery(eElement.getElementsByTagName("Query").item(0).getTextContent());
                        break;
                    }
                }
            }
        } catch (
                Exception e) {
            log.info(String.valueOf(e));
            throw e;
        } finally {
            /** Close XML-file?*/
        }
        return configData;
    }
}
