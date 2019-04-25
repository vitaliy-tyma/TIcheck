package com.luxoft.falcon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MapError {
    @Getter
    private String fullName;
    @Getter
    private String javaClassError;
    @Getter
    private String queryFull;
}
