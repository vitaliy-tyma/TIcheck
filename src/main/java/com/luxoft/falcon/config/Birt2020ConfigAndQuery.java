package com.luxoft.falcon.config;

import com.luxoft.falcon.config.inter.ConfigAndQueryInterface;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/* Entity/Data/POJO to connect to the BIRT Gen.2020 */

@Data
public class Birt2020ConfigAndQuery implements ConfigAndQueryInterface {

    private static Birt2020ConfigAndQuery _instance = new Birt2020ConfigAndQuery();

    private Birt2020ConfigAndQuery() {
    }

    public static synchronized Birt2020ConfigAndQuery getInstance() {
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

    @Getter @Setter
    private String queryIs;
}

