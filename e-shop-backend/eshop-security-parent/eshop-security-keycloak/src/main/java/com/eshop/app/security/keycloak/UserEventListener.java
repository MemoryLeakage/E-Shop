package com.eshop.app.security.keycloak;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class UserEventListener {

    private static final String USER_CHANNEL = "user-change-channel";
    private static final String MQ_HOST = "MQ_HOST";
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public void Listen() {
        ConnectionFactory factory = new ConnectionFactory();
//         TODO make this attribute dynamic.
        factory.setHost(System.getenv(MQ_HOST));
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(USER_CHANNEL, true, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                UserEvent userEvent = mapper.readerFor(UserEvent.class).readValue(message);
                System.out.println(userEvent.toString());
            };
            channel.basicConsume(USER_CHANNEL, true, deliverCallback, consumerTag -> {
            });

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
