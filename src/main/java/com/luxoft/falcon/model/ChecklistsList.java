package com.luxoft.falcon.model;



import lombok.Getter;
import lombok.Setter;

import java.util.*;

public final class ChecklistsList {
    private static ChecklistsList _instance = new ChecklistsList();

    @Getter @Setter
    private Map<String, Boolean> checklistsList;
    public void addLine(String name, Boolean defaultFlag){
        checklistsList.put(name, defaultFlag);
    }


    private ChecklistsList() {
        checklistsList = new Map<String, Boolean>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(Object key) {
                return false;
            }

            @Override
            public boolean containsValue(Object value) {
                return false;
            }

            @Override
            public Boolean get(Object key) {
                return null;
            }

            @Override
            public Boolean put(String key, Boolean value) {
                return null;
            }

            @Override
            public Boolean remove(Object key) {
                return null;
            }

            @Override
            public void putAll(Map<? extends String, ? extends Boolean> m) {

            }

            @Override
            public void clear() {

            }

            @Override
            public Set<String> keySet() {
                return null;
            }

            @Override
            public Collection<Boolean> values() {
                return null;
            }

            @Override
            public Set<Entry<String, Boolean>> entrySet() {
                return null;
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }
        };
    }

    public static ChecklistsList getInstance() {
        return _instance;
    }


}
