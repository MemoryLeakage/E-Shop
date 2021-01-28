package com.eshop.security.keycloak;

import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;


//TODO change this class name as it conflicts with KeycloakSecurityContext from keycloak packages.
@Component
public class KeyCloakSecurityContext implements SecurityContext {

    private final static Logger logger = LoggerFactory.getLogger(KeyCloakSecurityContext.class);
    private final KeycloakDeployment keycloakDeployment;

    @Autowired
    public KeyCloakSecurityContext(AdapterDeploymentContext adapterDeploymentContext) {
        logger.debug("constructing keycloak security context");
        if(adapterDeploymentContext == null)
            throw new IllegalArgumentException("null adapter deployment context");
        this.keycloakDeployment = adapterDeploymentContext.resolveDeployment(null);
    }

    @Override
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (isNotKeycloakAuthentication(authentication))
            return null;
        AccessToken accessToken = getAccessToken(authentication);
        return new User.Builder()
                .firstName(accessToken.getGivenName())
                .lastName(accessToken.getFamilyName())
                .email(accessToken.getEmail())
                .username(accessToken.getPreferredUsername())
                .build();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isNotKeycloakAuthentication(Authentication authentication) {
        boolean isNotKeycloakAuth = !(authentication instanceof KeycloakAuthenticationToken);
        if(isNotKeycloakAuth)
            logger.debug("Current security context is not a keycloak context or user is not authenticated.");
        return isNotKeycloakAuth;
    }

    private AccessToken getAccessToken(Authentication authentication) {
        KeycloakSecurityContext keycloakSecurityContext = getKeycloakSecurityContext(authentication);
        return keycloakSecurityContext.getToken();
    }

    private KeycloakSecurityContext getKeycloakSecurityContext(Authentication authentication) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) authentication;
        return keycloakAuthenticationToken
                .getAccount()
                .getKeycloakSecurityContext();
    }

    @Override
    public Set<String> getRoles() {
        Authentication authentication = getAuthentication();
        if (isNotKeycloakAuthentication(authentication))
            return null;
        AccessToken accessToken = getAccessToken(authentication);
        return accessToken.getResourceAccess().get(keycloakDeployment.getResourceName()).getRoles();
    }
}
