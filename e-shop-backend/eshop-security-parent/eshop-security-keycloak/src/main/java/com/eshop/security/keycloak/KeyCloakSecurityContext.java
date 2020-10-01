package com.eshop.security.keycloak;

import com.eshop.models.entities.User;
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

import java.util.Set;


//TODO change this class name as it conflicts with KeycloakSecurityContext from keycloak packages.
@Component
public class KeyCloakSecurityContext implements SecurityContext {

    private final KeycloakDeployment keycloakDeployment;
    private Authentication authentication;

    @Autowired
    public KeyCloakSecurityContext(AdapterDeploymentContext adapterDeploymentContext) {
        if(adapterDeploymentContext == null)
            throw new IllegalArgumentException("null adapter deployment context");
        this.keycloakDeployment = adapterDeploymentContext.resolveDeployment(null);
    }

    @Override
    public User getUser() {
        initializeAuthentication();
        if (isNotKeycloakAuthentication())
            return null;
        AccessToken accessToken = getAccessToken();
        return new User.Builder()
                .firstName(accessToken.getGivenName())
                .lastName(accessToken.getFamilyName())
                .email(accessToken.getEmail())
                .username(accessToken.getPreferredUsername())
                .build();
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
    public Set<String> getRoles() {
        initializeAuthentication();
        if (isNotKeycloakAuthentication())
            return null;
        AccessToken accessToken = getAccessToken();
        return accessToken.getResourceAccess().get(keycloakDeployment.getResourceName()).getRoles();
    }
}
