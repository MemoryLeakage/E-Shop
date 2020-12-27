package com.eshop.business.user.handlers;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import com.eshop.repositories.UserRepository;
import com.eshop.security.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class GetUserInfoHandlerTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenNullAuthenticatedUser_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetUserInfoHandler(null, userRepository));
        assertEquals("security context can not be null", thrown.getMessage());
    }

    @Test
    void givenNullUserRepository_whenConstructing_thenThrowException(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetUserInfoHandler(securityContext, null));
        assertEquals("user repository can not be null", thrown.getMessage());
    }

    @Test
    void givenValidAuthenticatedUser_whenHandling_thenReturnAsExpected() {
        User user = getUserBuilder().build();
        float expectedRating = 4.3f;
        Mockito.when(securityContext.getUser()).thenReturn(user);
        Mockito.when(securityContext.getRoles()).thenReturn(getRoles());
        Mockito.when(userRepository.getRatingByUsername(user.getUsername())).thenReturn(expectedRating);
        GetUserInfoHandler getUserInfoHandler = new GetUserInfoHandler(securityContext, userRepository);

        GetUserInfoResponse response = getUserInfoHandler.handle();
        Mockito.verify(securityContext,Mockito.times(1)).getRoles();
        Mockito.verify(securityContext, Mockito.times(1)).getUser();
        assertNotNull(response);
        User expectedUser = securityContext.getUser();
        Set<String> expectedRoles = securityContext.getRoles();
        assertEquals(expectedUser.getUsername(), response.getUsername());
        assertEquals(expectedUser.getFirstName(), response.getFirstName());
        assertEquals(expectedUser.getLastName(), response.getLastName());
        assertEquals(expectedUser.getEmail(), response.getEmail());
        assertEquals(expectedRating, response.getRating());
        assertEquals(expectedRoles, response.getRoles());
    }

    public User.Builder getUserBuilder() {
        return new User.Builder()
                .email("admin@eshop.com")
                .firstName("first-name")
                .lastName("last-name")
                .username("test.user");
    }

    public Set<String> getRoles() {
        return Set.of("ADMIN", "MERCHANT");
    }
}
