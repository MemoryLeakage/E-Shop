package com.eshop.business.user.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.business.user.handlers.mocks.MockAuthUser;
import com.eshop.business.user.handlers.mocks.MockUserDataProvider;
import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetUserInfoHandlerTest {

    private MockAuthUser authenticatedUser;
    private GetUserInfoHandler getUserInfoHandler;

    @BeforeEach
    void setup() {
        authenticatedUser = new MockAuthUser(MockUserDataProvider.getUser(),
                MockUserDataProvider.getRoles());
        getUserInfoHandler = new GetUserInfoHandler(authenticatedUser);
    }

    @Test
    void givenNullAuthenticatedUser_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetUserInfoHandler(null));
        assertEquals("authenticated user cannot be null", thrown.getMessage());
    }

    @Test
    void givenValidAuthenticatedUser_whenHandling_thenReturnAsExpected() {
        User expectedUser = MockUserDataProvider.getUser();
        String[] expectedRoles = MockUserDataProvider.getRoles();
        GetUserInfoResponse response = getUserInfoHandler.handle();
        assertNotNull(response);
        assertEquals(expectedUser.getUsername(), response.getUsername());
        assertEquals(expectedUser.getFirstName(), response.getFirstName());
        assertEquals(expectedUser.getLastName(), response.getLastName());
        assertEquals(expectedUser.getEmail(), response.getEmail());
        assertEquals(expectedUser.getRating(), response.getRating());
        assertArrayEquals(expectedRoles, response.getRoles());
    }
}
