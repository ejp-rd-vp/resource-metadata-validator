package org.ejprd.metadata.resource.api.rest;

import scala.concurrent.impl.FutureConvertersImpl;

import java.util.Arrays;

public enum ResultType {
    JSON("json"),
    COMPACT("compact"),
    COMPACT_DETAIL ("compact-detail");

    private String name;

    ResultType(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public static ResultType fromString(String s) throws IllegalArgumentException {
        return Arrays.stream(ResultType.values())
                .filter(v -> v.name.equals(s))
                .findFirst()
                .orElse(JSON);
    }
}
