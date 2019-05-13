package com.luxoft.falcon.config;

import lombok.Getter;
import lombok.Setter;
//TODO Store all items in separate XML file and load it's content at start and on request.

public final class BirtQueryToCheckGeneration {

    private static BirtQueryToCheckGeneration _instance = new BirtQueryToCheckGeneration();

    private BirtQueryToCheckGeneration() {
    }

    public static synchronized BirtQueryToCheckGeneration getInstance() {
        return _instance;
    }

    @Getter @Setter
    private String g2010;// = " \nSELECT * FROM ndsreport.testsuite WHERE testsuitename LIKE ? LIMIT ?\n";
    @Getter @Setter
    private String g2020;// = " \nSELECT * FROM ndsreport_new.suites WHERE name LIKE ? LIMIT ?\n";
}
