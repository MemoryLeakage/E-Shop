package com.eshop.keycloak.spis;

import com.rabbitmq.client.ConnectionFactory;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class UserEventListenerProviderFactory implements EventListenerProviderFactory {

    // TODO take this value from secure place other than the environment variable
    private static final String MQ_HOST = "MQ_HOST";
    private ConnectionFactory factory;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new UserEventListenerProvider(keycloakSession, factory);
    }

    @Override
    public void init(Config.Scope scope) {
        this.factory = new ConnectionFactory();
        factory.setHost(System.getenv(MQ_HOST));
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "User-Event-Listener";
    }
}
