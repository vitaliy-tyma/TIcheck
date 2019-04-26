package com.luxoft.falcon.model;

public enum QueryToCheckGeneration {
    G2010(" SELECT * FROM ndsreport.testsuite WHERE testsuitename LIKE ? LIMIT ?"),
    G2020(" SELECT * FROM ndsreport_new.suites WHERE name LIKE ? LIMIT ?");

    public static String queryToChechGeneration;

    QueryToCheckGeneration(String generation) {
//        this.queryToChechGeneration = queryToChechGeneration;
    }

    public static String getQueryToChechGeneration() {
        return queryToChechGeneration;
    }
}
