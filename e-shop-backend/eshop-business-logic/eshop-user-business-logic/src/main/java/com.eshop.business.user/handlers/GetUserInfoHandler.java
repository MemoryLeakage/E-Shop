package com.eshop.business.user.handlers;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import com.eshop.repositories.UserRepository;
import com.eshop.security.SecurityContext;

import static com.eshop.utilities.Validators.validateNotNull;

public class GetUserInfoHandler {

    private final SecurityContext securityContext;
    private final UserRepository userRepository;

    public GetUserInfoHandler(SecurityContext securityContext, UserRepository userRepository) {
        this.userRepository = userRepository;
        validateNotNull(securityContext, "security context");
        validateNotNull(userRepository, "user repository");
        this.securityContext = securityContext;
    }


    public GetUserInfoResponse handle() {
        User contextUser = securityContext.getUser();
        Float rating = userRepository.getRatingByUsername(contextUser.getUsername());
        return new GetUserInfoResponse.Builder()
                .username(contextUser.getUsername())
                .firstName(contextUser.getFirstName())
                .lastName(contextUser.getLastName())
                .email(contextUser.getEmail())
                .rate(rating)
                .roles(securityContext.getRoles())
                .build();
    }
}