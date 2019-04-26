package com.luxoft.falcon.model;

import lombok.Getter;


public class QueryToCheckGeneration {
    @Getter
    private String g2010 = " SELECT * FROM ndsreport.testsuite WHERE testsuitename LIKE ? LIMIT ?";
    @Getter
    private String g2020 = " SELECT * FROM ndsreport_new.suites WHERE name LIKE ? LIMIT ?";
}
