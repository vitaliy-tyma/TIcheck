package com.luxoft.falcon.config.inter;




public interface ConfigAndQueryInterface {

    public String jdbcDriver = null;
    public String jdbcUrl = null;
    public String jdbcLogin = null;
    public String jdbcPassword = null;
    public String queryLike = null;
    public String queryAccurate = null;


    public void setJdbcDriver(String string);
    public void setJdbcUrl(String string);
    public void setJdbcLogin(String string);
    public void setJdbcPassword(String string);
    public void setQueryLike(String string);
    public void setQueryAccurate(String string);
}
