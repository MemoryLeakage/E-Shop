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
        if (eventTypes.contains(event.getType())) {
            UserProvider users = keycloakSession.userStorageManager();
            RealmModel realm = keycloakSession.getContext().getRealm();
            ConnectionFactory factory = new ConnectionFactory();
            // TODO make this attribute dynamic.
            factory.setHost("rabbitmq");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(USER_CHANNEL, true, false, false, null);
                UserModel user = users.getUserById(event.getUserId(), realm);
                byte[] userJsonBytes = buildMessageJsonBytes(user, event);
                channel.basicPublish("", USER_CHANNEL, null, userJsonBytes);
                logger.debug("Sent message to " + USER_CHANNEL + " : " + new String(userJsonBytes));
            } catch (IOException | TimeoutException e) {
                logger.error("Error Sending Message: " + e.getCause());
                throw new RuntimeException(e);
            }

        }
    }

    private byte[] buildMessageJsonBytes(UserModel user, Event event) {
        String userJsonBuilder = "{\"username\":\""+user.getUsername()+"\"," +
                "\"firstName\":\"" +
                user.getFirstName() +
                "\",\"lastName\":\"" +
                user.getLastName() +
                "\",\"email\":\"" +
                user.getEmail() +
                "\"}";
        return buildMessageBytes(event.getType().name(),
                userJsonBuilder);
    }

    private byte[] buildMessageBytes(String eventTypeName, String jsonUserData) {
        String message = "{\"type\":\"" + eventTypeName + "\"," +
                "\"userData\":" + jsonUserData + "}";
        return message.getBytes();
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {

        if (adminOperationTypes.contains(event.getOperationType())
                && adminResourceTypes.contains(event.getResourceType())) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabitmq");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(ADMIN_CHANNEL, true, false, false, null);
                
                byte[] userJsonBytes = buildMessageBytes(event.getOperationType().name(),
                        event.getRepresentation());
                channel.basicPublish("", ADMIN_CHANNEL, null, userJsonBytes);
                logger.debug("Sent message to " + ADMIN_CHANNEL + " : " + new String(userJsonBytes));
            } catch (IOException | TimeoutException e) {
                logger.error("Error Sending Message: " + e.getCause());
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void close() {
    }
}
