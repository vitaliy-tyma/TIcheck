package com.luxoft.falcon.config;

import com.luxoft.falcon.config.inter.ConfigAndQueryInterface;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/* Entity/Data/POJO to connect to the BIRT Gen.2010 */

@Data
public class Birt2010ConfigAndQuery implements ConfigAndQueryInterface {

    private static Birt2010ConfigAndQuery _instance = new Birt2010ConfigAndQuery();

    private Birt2010ConfigAndQuery() {
    }

    public static synchronized Birt2010ConfigAndQuery getInstance() {
        return _instance;
    }

    @Getter @Setter
    private String jdbcDriver;

    @Getter @Setter
    private String jdbcUrl;

    @Getter @Setter
    private String jdbcLogin;
    @Getter @Setter
    private String jdbcPassword;


    @Getter @Setter
    private String queryLike;
    //TODO Clarify with the exitcode parameter

    @Getter @Setter
    private String queryIs;
    //TODO Clarify with the exitcode parameter
}

