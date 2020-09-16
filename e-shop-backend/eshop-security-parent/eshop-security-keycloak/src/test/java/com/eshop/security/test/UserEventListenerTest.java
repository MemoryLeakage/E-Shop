package com.eshop.security.test;

import com.eshop.models.entities.User;
import com.eshop.repositories.UserRepository;
import com.eshop.security.keycloak.UserEventListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEventListenerTest {


    @Mock
    private UserRepository repository;

    @Test
    void givenNullUserRepository_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new UserEventListener(null));
        assertEquals("user repository can not be null", thrown.getMessage());
    }

//    TODO test log output and to do that we need to enhance the way logs are being used (static methods usage is not ideal).
//    @Test
//    void givenInvalidMessageBytes_whenDeserializingJson_thenLogError() {
//
//        UserEventListener listener = new UserEventListener(repository);
//        listener.listen("invalid message".getBytes());
//        Mockito.verify(logger).error(Mockito.anyString());
//    }

    @Test
    void givenValidMessageBytes_whenDeserializing_thenBehaveAsExpected() throws JsonProcessingException {
        String username = "USername";
        String firstName = "first name";
        String lastName = "last name";
        String email = "EMail@email.com";
        String jsonUserData = "{\"username\":\"" + username + "\"," +
                "\"firstName\":\"" +
                firstName +
                "\",\"lastName\":\"" +
                lastName +
                "\",\"email\":\"" +
                email +
                "\"}";
        String create = "CREATE";
        String update = "UPDATE";
        String createMessage = "{\"type\":\"" + create + "\"," +
                "\"userData\":" + jsonUserData + "}";
        String updateMessage =  "{\"type\":\"" + update + "\"," +
                "\"userData\":" + jsonUserData + "}";
        UserEventListener listener = new UserEventListener(repository);
        listener.listen(createMessage.getBytes());
        listener.listen(updateMessage.getBytes());
        User user = new User.Builder()
                .email(email.toLowerCase())
                .firstName(firstName)
                .lastName(lastName)
                .username(username.toLowerCase())
                .build();
        verify(repository,times(1)).addUser(user);
        verify(repository,times(1)).updatePII(firstName,lastName,email.toLowerCase(),username.toLowerCase());
    }
}
