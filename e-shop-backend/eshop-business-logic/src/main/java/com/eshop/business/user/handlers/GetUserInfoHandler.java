package com.eshop.business.user.handlers;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.eshop.repositories.UserRepository;

import static com.eshop.utilities.Validators.validateNotNullArgument;

public class GetUserInfoHandler {

    private final SecurityContext securityContext;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(GetUserInfoHandler.class);

    public GetUserInfoHandler(SecurityContext securityContext, UserRepository userRepository) {
        logger.debug("Constructing GetUserInfoHandler");
        validateNotNullArgument(securityContext, "security context");
        validateNotNullArgument(userRepository, "user repository");
        this.securityContext = securityContext;
        this.userRepository = userRepository;
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