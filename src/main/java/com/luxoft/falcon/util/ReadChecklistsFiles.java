package com.luxoft.falcon.util;

import com.luxoft.falcon.model.ChecklistsList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class ReadChecklistsFiles {
    private static ChecklistsList checklistsList = ChecklistsList.getInstance();

    public static Map<String, Boolean> getChecklistsList(String checklists_path) throws IOException {

        File folder = new File(checklists_path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                checklistsList.addLine(listOfFiles[i].getName(), false);
//            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }


        return checklistsList.getChecklistsList();
    }



}
