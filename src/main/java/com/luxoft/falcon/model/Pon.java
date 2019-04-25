package com.luxoft.falcon.model;

import lombok.*;

import java.util.LinkedList;
import java.util.Map;


@NoArgsConstructor
//@AllArgsConstructor
@ToString
@EqualsAndHashCode
public final class Pon {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int iteration;
    @Getter
    @Setter
    private Boolean autocomplete = false;


    @Getter
    @Setter
    private LinkedList<SpiderError> spiderErrors;
    @Getter
    @Setter
    private Map<String, Boolean> birt;
    @Getter
    @Setter
    private Map<String, Boolean> nds;

    @Getter
    @Setter
    private String output;

    @Getter
    @Setter
    private String queryFull;

}



