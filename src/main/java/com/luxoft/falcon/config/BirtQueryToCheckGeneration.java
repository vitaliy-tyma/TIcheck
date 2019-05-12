package com.luxoft.falcon.config;

import lombok.Getter;
//TODO Store all items in separate XML file and load it's content at start and on request.

public class BirtQueryToCheckGeneration {
    @Getter
    private String g2010 = " \nSELECT * FROM ndsreport.testsuite WHERE testsuitename LIKE ? LIMIT ?\n";
    @Getter
    private String g2020 = " \nSELECT * FROM ndsreport_new.suites WHERE name LIKE ? LIMIT ?\n";
}
