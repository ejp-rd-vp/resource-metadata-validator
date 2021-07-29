package org.ejprd.metadata.resource.model;

public enum ResourceMetaModel {
    LOCATION("location");

    private final String name;

    ResourceMetaModel(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
