package com.luxoft.falcon.model;



import lombok.Getter;
import lombok.Setter;

import java.util.*;

public final class ChecklistsList {
    private static ChecklistsList _instance = new ChecklistsList();

    @Getter @Setter
    private List<ChecklistsListEntry> checklistsList = new LinkedList<>();
    public void addLine(ChecklistsListEntry entry){
        checklistsList.add(entry);
    }
    public void clearChecklistsList() {
        checklistsList.clear();
    }

    private ChecklistsList() {
    }

    public static ChecklistsList getInstance() {
        return _instance;
    }



}
