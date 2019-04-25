package com.luxoft.falcon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SpiderError{
    @Getter
    private String fullName;
    @Getter
    private String javaClassError;
}
