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
    private static final Logger logger = LoggerFactory.getLogger(UserEventListener.class);
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final UserRepository userRepository;

    @Autowired
    public UserEventListener(UserRepository userRepository) {
        if(userRepository == null)
            throw new IllegalArgumentException("user repository can not be null");
        this.userRepository = userRepository;
    }

    public void listen(byte[] messageBytes) {
        String message = new String(messageBytes);
        logger.debug("received message {}", message);
        try {
            UserEvent userEvent = mapper.readerFor(UserEvent.class).readValue(message);
            UserData userData = userEvent.getUserData();
            String email = userData.getEmail();
            String lowerCaseEmail = email == null ? "" : email.toLowerCase();
            if (userEvent.getEventType().equals(EventType.CREATE)) {
                User user = new User.Builder()
                        .email(lowerCaseEmail)
                        .firstName(userData.getFirstName())
                        .lastName(userData.getLastName())
                        .username(userData.getUsername().toLowerCase())
                        .build();
                logger.debug("adding new user {} to database", user.getUsername());
                userRepository.addUser(user);
            } else if (userEvent.getEventType().equals(EventType.UPDATE)) {
                logger.debug("updating existing user {}", userData.getUsername());
                userRepository.updatePII(userData.getFirstName(),
                        userData.getLastName(),
                        lowerCaseEmail,
                        userData.getUsername().toLowerCase());

            }
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing MQ for user event " + e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
        }
    }
}
