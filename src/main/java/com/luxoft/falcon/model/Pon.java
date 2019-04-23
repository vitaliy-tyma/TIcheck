package com.luxoft.falcon.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor (access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
public final class Pon {
    private String name;
    private Map<String, String> result;
    private String output;

    private static Pon ourInstance = new Pon();

    public static Pon getInstance() {
        return ourInstance;
    }

}
