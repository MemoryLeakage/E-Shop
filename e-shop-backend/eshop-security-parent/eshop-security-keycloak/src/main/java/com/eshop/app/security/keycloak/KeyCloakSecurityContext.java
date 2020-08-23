package com.eshop.app.security.keycloak;

import com.eshop.models.entities.User;
import com.eshop.repositories.UserRepository;
import com.eshop.security.SecurityContext;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class KeyCloakSecurityContext implements SecurityContext {

    private final UserRepository userRepository;
    private final KeycloakDeployment keycloakDeployment;
    private Authentication authentication;

    @Autowired
    public KeyCloakSecurityContext(UserRepository userRepository, AdapterDeploymentContext adapterDeploymentContext) {
        this.userRepository = userRepository;
        this.keycloakDeployment = adapterDeploymentContext.resolveDeployment(null);
    }

    @Override
    public User getUser() {
        initializeAuthentication();
        if (isNotKeycloakAuthentication())
            return null;
        AccessToken accessToken = getAccessToken();
        User user = new User.Builder()
                .firstName(accessToken.getGivenName())
                .lastName(accessToken.getFamilyName())
                .email(accessToken.getEmail())
                .username(accessToken.getPreferredUsername())
                .build();
        if (!userRepository.existsByUserName(user.getUsername())) {
            userRepository.addUser(user);
        }
        return user;
    }

    private void initializeAuthentication() {
        if (authentication == null)
            authentication = SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isNotKeycloakAuthentication() {
        return !(authentication instanceof KeycloakAuthenticationToken);
    }

    private AccessToken getAccessToken() {
        KeycloakSecurityContext keycloakSecurityContext = getKeycloakSecurityContext();
        return keycloakSecurityContext.getToken();
    }

    private KeycloakSecurityContext getKeycloakSecurityContext() {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) authentication;
        return keycloakAuthenticationToken
                .getAccount()
                .getKeycloakSecurityContext();
    }

    @Override
    public String[] getRoles() {
        initializeAuthentication();
        if (isNotKeycloakAuthentication())
            return null;
        AccessToken accessToken = getAccessToken();
        return (String[]) accessToken.getResourceAccess(keycloakDeployment.getResourceName()).getRoles().toArray();
    }
}
