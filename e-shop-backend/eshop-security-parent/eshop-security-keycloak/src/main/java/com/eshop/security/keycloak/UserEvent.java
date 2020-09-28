package com.eshop.security.keycloak;

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

    public EventType getEventType() {
        return eventType;
    }

    public UserData getUserData() {
        return userData;
    }

}

