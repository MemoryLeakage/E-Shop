package com.eshop.business.user.handlers;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import com.eshop.security.AuthenticatedUser;

public class GetUserInfoHandler {

    private final AuthenticatedUser authenticatedUser;

    public GetUserInfoHandler(AuthenticatedUser authenticatedUser) {
        validateNotNull(authenticatedUser, "authenticated user cannot be null");
        this.authenticatedUser = authenticatedUser;
    }

    private void validateNotNull(AuthenticatedUser authenticatedUser, String message) {
        if (authenticatedUser == null)
            throw new IllegalArgumentException(message);
    }

    public GetUserInfoResponse handle() {
        User user = authenticatedUser.getUser();
        return new GetUserInfoResponse.Builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .rate(user.getRating())
                .roles(authenticatedUser.getRoles())
                .build();
    }
}