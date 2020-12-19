package com.eshop.security.keycloak;

import com.eshop.models.entities.User;
import com.eshop.repositories.UserRepository;
import com.eshop.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserContextProvider implements SecurityContext {

    private final UserRepository userRepository;
    private final SecurityContext securityContext;

    @Autowired
    public UserContextProvider(UserRepository userRepository,
                               @Qualifier("keyCloakSecurityContext") SecurityContext securityContext){
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    @Override
    public User getUser() {
        User user = securityContext.getUser();
        user = userRepository.getUserByUsername(user.getUsername());
        return user;
    }

    @Override
    public Set<String> getRoles() {
        return securityContext.getRoles();
    }
}
