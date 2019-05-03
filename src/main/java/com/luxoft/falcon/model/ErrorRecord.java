package com.luxoft.falcon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ErrorRecord {
    @Getter
    private String fullName;
    @Getter
    private String error;
    @Getter
    private String usedQuery;
}
