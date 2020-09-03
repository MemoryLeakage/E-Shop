package com.eshop.app.security.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserEvent {

    @JsonProperty(value = "type")
    private EventType eventType;
    @JsonProperty(value = "userData")
    private UserData userData;

    public UserEvent() {
    }

    public UserEvent(EventType eventType, UserData userData) {
        this.eventType = eventType;
        this.userData = userData;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "eventType=" + eventType +
                ", userData=" + userData.toString() +
                '}';
    }
}

