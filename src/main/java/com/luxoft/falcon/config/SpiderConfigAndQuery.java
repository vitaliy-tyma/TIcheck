package com.luxoft.falcon.config;

import com.luxoft.falcon.config.inter.ConfigAndQueryInterface;
import lombok.*;

/** Entity/Data/POJO to connect to the SPIDER */

@Data
public final class SpiderConfigAndQuery implements ConfigAndQueryInterface {

    private static SpiderConfigAndQuery _instance = new SpiderConfigAndQuery();

    private SpiderConfigAndQuery() {
    }

    public static synchronized SpiderConfigAndQuery getInstance() {
        return _instance;
    }

    @Getter
    private String jdbcDriver;
    @Getter
    private String jdbcUrl;
    @Getter
    private String jdbcLogin;
    @Getter
    private String jdbcPassword;
    @Getter
    private String queryLike;
    @Getter
    private String queryIs;
}
