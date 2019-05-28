package com.luxoft.falcon.util;

import com.luxoft.falcon.config.MainConfig;
import com.luxoft.falcon.model.ChecklistsList;
import com.luxoft.falcon.model.ChecklistsListEntry;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;
import java.io.*;

@Slf4j
public class ReadChecklistsFiles {
    private static ChecklistsList checklistsList = ChecklistsList.getInstance();

    public static void getChecklistsList(String checklists_path) {

        File folder = new File(checklists_path);
        File[] listOfFiles = folder.listFiles();
        checklistsList.clearChecklistsList();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String checklistFileName = listOfFiles[i].getName();

                ChecklistsListEntry entry = getChecklistNameFromFile(
                        checklists_path +
                                MainConfig.getPATH_DELIMITER(),
                                checklistFileName);


                checklistsList.addLine(entry);
            }
        }
    }


    private static ChecklistsListEntry getChecklistNameFromFile(String filePath, String fileName) {
        String checklistName = "";
        String checklistDefaultFlagString = "";
        boolean defaultFlag = false;
        String simpleChecklistFileName = fileName.
                replace(".xml", "");

        try {
            File fXmlFile = new File(filePath + MainConfig.getPATH_DELIMITER() + fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("checklist");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    checklistName = eElement.getElementsByTagName(
                            "name").item(0).getTextContent().
                            replace("\n", "").trim();

                    checklistDefaultFlagString = eElement.getElementsByTagName(
                            "default").item(0).getTextContent().
                            replace("\n", "").trim();
                    if (checklistDefaultFlagString.equals("yes")) {
                        defaultFlag = true;
                    } else {
                        defaultFlag = false;
                    }
                }
            }

        } catch (Exception e) {
            log.error("Read checklist XML-file failed with the error " + e.getMessage());
        }

        return new ChecklistsListEntry(
                checklistName,
                simpleChecklistFileName,
                fileName,
                defaultFlag);
    }

}
