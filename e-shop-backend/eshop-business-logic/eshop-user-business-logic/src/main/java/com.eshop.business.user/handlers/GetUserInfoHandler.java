package com.eshop.business.user.handlers;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUserInfoHandler {

    private final SecurityContext securityContext;

    private static final Logger logger = LoggerFactory.getLogger(GetUserInfoHandler.class);

    public GetUserInfoHandler(SecurityContext securityContext) {
        logger.debug("Constructing GetUserInfoHandler");
        validateNotNull(securityContext, "security context cannot be null");
        this.securityContext = securityContext;
        logger.debug("Successfully constructed GetUserInfoHandler");
    }

    private void validateNotNull(SecurityContext securityContext, String message) {
        if (securityContext == null)
            throw new IllegalArgumentException(message);
    }

    public GetUserInfoResponse handle() {
        User user = securityContext.getUser();
        return new GetUserInfoResponse.Builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .rate(user.getRating())
                .roles(securityContext.getRoles())
                .build();
    }
}