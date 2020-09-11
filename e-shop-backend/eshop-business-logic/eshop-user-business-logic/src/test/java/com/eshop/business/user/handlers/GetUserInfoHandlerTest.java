package com.eshop.business.user.handlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import com.eshop.repositories.UserRepository;
import com.eshop.security.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetUserInfoHandlerTest {

    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserRepository userRepository;

    @Test
    void givenNullSecurityContext_whenConstructing_thenThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new GetUserInfoHandler(null, userRepository));
        assertEquals("security context can not be null", thrown.getMessage());
    }

    @Test
    void givenNullUserRepository_whenConstructing_thenThrowException(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                ()->new GetUserInfoHandler(securityContext,null));
        assertEquals("user repository can not be null", thrown.getMessage());
    }

    @Test
    void givenValidAuthenticatedUser_whenHandling_thenReturnAsExpected() {
        User securityContextUser = getUserBuilder();
        float expectedRating = 4.3f;
        Mockito.when(securityContext.getUser()).thenReturn(securityContextUser);
        Mockito.when(securityContext.getRoles()).thenReturn(getRoles());
        Mockito.when(userRepository.getRatingByUsername(securityContextUser.getUsername())).thenReturn(expectedRating);
        GetUserInfoHandler getUserInfoHandler = new GetUserInfoHandler(securityContext, userRepository);
        GetUserInfoResponse response = getUserInfoHandler.handle();
        Mockito.verify(securityContext, times(1)).getRoles();
        Mockito.verify(securityContext, times(1)).getUser();
        Mockito.verify(userRepository, times(1))
                .getRatingByUsername(securityContextUser.getUsername());
        assertNotNull(response);
        User expectedUser = securityContext.getUser();
        String[] expectedRoles = securityContext.getRoles();
        assertEquals(expectedUser.getUsername(), response.getUsername());
        assertEquals(expectedUser.getFirstName(), response.getFirstName());
        assertEquals(expectedUser.getLastName(), response.getLastName());
        assertEquals(expectedUser.getEmail(), response.getEmail());
        assertEquals(expectedRating, response.getRating());
        assertArrayEquals(expectedRoles, response.getRoles());
    }

    public User getUserBuilder() {
        return new User.Builder()
                .email("admin@eshop.com")
                .firstName("first-name")
                .lastName("last-name")
                .username("test.user")
                .build();
    }

    public String[] getRoles() {
        return new String[]{"ADMIN", "MERCHANT"};
    }
}
