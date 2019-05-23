package com.luxoft.falcon.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public final class BirtQueryToCheckGeneration {

    private static BirtQueryToCheckGeneration _instance = new BirtQueryToCheckGeneration();

    private BirtQueryToCheckGeneration() {
    }

    public static synchronized BirtQueryToCheckGeneration getInstance() {
        return _instance;
    }

//    @Getter @Setter
    private String g2010;
//    @Getter @Setter
    private String g2020;
}
