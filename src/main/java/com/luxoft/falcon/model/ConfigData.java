package com.luxoft.falcon.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/** Entity/Data/POJO to pass config data between classes of app */
@Getter (AccessLevel.PUBLIC)
@Setter
public class ConfigData {
    private String run = null;
    private String market = null;
    private String project = null;
    private String iteration = null;

    private String jdbcDriver = null;
    private String jdbcUrl = null;
    private String jdbcLogin = null;
    private String jdbcPassword = null;
    private String query = null;

    public ConfigData() {
    }

    public ConfigData(String run, String market, String project, String iteration,
                      String jdbcDriver, String jdbcUrl, String jdbcLogin, String jdbcPassword,
                      String query) {
        this.run = run;
        this.market = market;
        this.project = project;
        this.iteration = iteration;
        this.jdbcDriver = jdbcDriver;
        this.jdbcUrl = jdbcUrl;
        this.jdbcLogin = jdbcLogin;
        this.jdbcPassword = jdbcPassword;
        this.query = query;
    }
}
