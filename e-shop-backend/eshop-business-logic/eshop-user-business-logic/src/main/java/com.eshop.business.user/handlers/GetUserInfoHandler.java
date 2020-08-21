package com.eshop.business.user.handlers;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;

public class GetUserInfoHandler {

    private final SecurityContext securityContext;

    public GetUserInfoHandler(SecurityContext securityContext) {
        validateNotNull(securityContext, "authenticated user cannot be null");
        this.securityContext = securityContext;
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