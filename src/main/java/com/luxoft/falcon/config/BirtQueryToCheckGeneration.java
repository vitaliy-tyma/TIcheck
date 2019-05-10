package com.luxoft.falcon.config;

import lombok.Getter;

public class BirtQueryToCheckGeneration {
    @Getter
    private String g2010 = " \nSELECT * FROM ndsreport.testsuite WHERE testsuitename LIKE ? LIMIT ?\n";
    @Getter
    private String g2020 = " \nSELECT * FROM ndsreport_new.suites WHERE name LIKE ? LIMIT ?\n";
}
