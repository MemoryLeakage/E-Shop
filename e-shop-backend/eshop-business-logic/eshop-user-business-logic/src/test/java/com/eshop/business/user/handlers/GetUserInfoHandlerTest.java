package com.eshop.business.user.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class GetUserInfoHandlerTest {

    @Mock
    private SecurityContext authenticatedUser;

    @Test
    void givenNullAuthenticatedUser_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetUserInfoHandler(null));
        assertEquals("security context cannot be null", thrown.getMessage());
    }

    @Test
    void givenValidAuthenticatedUser_whenHandling_thenReturnAsExpected() {
        Mockito.when(authenticatedUser.getUser()).thenReturn(getUser());
        Mockito.when(authenticatedUser.getRoles()).thenReturn(getRoles());
        GetUserInfoHandler getUserInfoHandler = new GetUserInfoHandler(authenticatedUser);

        GetUserInfoResponse response = getUserInfoHandler.handle();
        Mockito.verify(authenticatedUser,Mockito.times(1)).getRoles();
        Mockito.verify(authenticatedUser, Mockito.times(1)).getUser();
        assertNotNull(response);
        User expectedUser = authenticatedUser.getUser();
        Set<String> expectedRoles = authenticatedUser.getRoles();
        assertEquals(expectedUser.getUsername(), response.getUsername());
        assertEquals(expectedUser.getFirstName(), response.getFirstName());
        assertEquals(expectedUser.getLastName(), response.getLastName());
        assertEquals(expectedUser.getEmail(), response.getEmail());
        assertEquals(expectedUser.getRating(), response.getRating());
        assertEquals(expectedRoles, response.getRoles());
    }

    public User getUser() {
        return new User.Builder()
                .email("admin@eshop.com")
                .firstName("first-name")
                .lastName("last-name")
                .username("test.user")
                .rating(4.3f)
                .build();
    }

    public Set<String> getRoles() {
        return Set.of("ADMIN", "MERCHANT");
    }
}
