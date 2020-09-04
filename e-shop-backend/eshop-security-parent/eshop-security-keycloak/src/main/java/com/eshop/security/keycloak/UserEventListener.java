package com.eshop.security.keycloak;

import com.eshop.models.entities.User;
import com.eshop.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(UserEventListener.class);
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private UserRepository userRepository;

    @Autowired
    public UserEventListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void listen(byte[] messageBytes) {
        String message = new String(messageBytes);
        try {
            UserEvent userEvent = mapper.readerFor(UserEvent.class).readValue(message);
            UserData userData = userEvent.getUserData();
            if (userEvent.getEventType().equals(EventType.CREATE)) {
                User user = new User.Builder()
                        .email(userData.getEmail())
                        .firstName(userData.getFirstName())
                        .lastName(userData.getLastName())
                        .username(userData.getUsername())
                        .build();
                userRepository.addUser(user);
            } else if (userEvent.getEventType().equals(EventType.UPDATE)) {
                userRepository.updatePII(userData.getFirstName(),
                        userData.getLastName(),
                        userData.getEmail(),
                        userData.getUsername());

            }
        } catch (JsonProcessingException e) {
            LOG.error("Error deserializing MQ for user event " + e.getMessage());
            LOG.debug(Arrays.toString(e.getStackTrace()));
        }

    }
}
