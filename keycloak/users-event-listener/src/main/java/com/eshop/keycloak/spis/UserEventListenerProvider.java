package com.eshop.keycloak.spis;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeoutException;


public class UserEventListenerProvider implements EventListenerProvider {


    private static final Logger logger = Logger.getLogger(UserEventListenerProvider.class);
    private static final String USER_CHANNEL = "user-change-channel";
    private static final String ADMIN_CHANNEL = "admin-change-channel";

    private static final Set<OperationType> adminOperationTypes =
            Set.of(OperationType.CREATE,
                    OperationType.DELETE,
                    OperationType.UPDATE);
    private static final Set<ResourceType> adminResourceTypes =
            Collections.singleton(ResourceType.USER);

    private static final Set<EventType> eventTypes =
            Set.of(EventType.REGISTER,
                    EventType.UPDATE_EMAIL,
                    EventType.UPDATE_PROFILE);
    private final KeycloakSession keycloakSession;

    public UserEventListenerProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    @Override
    public void onEvent(Event event) {
        UserProvider users = keycloakSession.userStorageManager();
        RealmModel realm = keycloakSession.getContext().getRealm();
        ConnectionFactory factory = new ConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(USER_CHANNEL, true, false, false, null);
            if (eventTypes.contains(event.getType())) {
                UserModel user = users.getUserById(event.getUserId(), realm);
                byte[] userJsonBytes = buildJsonFromUser(user);
                channel.basicPublish(event.getType().name(), USER_CHANNEL, null, userJsonBytes);
                logger.error("Message Has Been Sent");
            }
        } catch (IOException | TimeoutException e) {
            logger.error("Error Sending Message");
            throw new RuntimeException(e);
        }


    }

    private byte[] buildJsonFromUser(UserModel user) {
        String userJsonBuilder = "{\"firstName\":\"" +
                user.getUsername() +
                "\",\"lastName\":\"" +
                user.getLastName() +
                "\",\"email\":\"" +
                user.getEmail() +
                "\"}";
        return userJsonBuilder.getBytes();
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {

        if (adminOperationTypes.contains(event.getOperationType())
                && adminResourceTypes.contains(event.getResourceType())) {
            logger.error("ADMIN EVENT: " + event.getRepresentation());
        }
    }

    @Override
    public void close() {

    }
}
