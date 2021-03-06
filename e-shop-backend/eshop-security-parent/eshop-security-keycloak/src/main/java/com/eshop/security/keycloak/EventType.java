package com.eshop.security.keycloak;

public enum EventType {
    CREATE("CREATE"),
    UPDATE("UPDATE");

    String name;

    EventType(String name) {
        this.name = name;
    }
}
