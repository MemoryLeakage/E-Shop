package com.eshop.services.spring;

import com.eshop.business.user.handlers.GetUserInfoHandler;
import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    private final GetUserInfoHandler getUserInfoHandler;

    @Autowired
    public UserServiceImpl(GetUserInfoHandler getUserInfoHandler) {
        this.getUserInfoHandler = getUserInfoHandler;
    }

    @Override
    public GetUserInfoResponse getUserInfo() {
        return getUserInfoHandler.handle();
    }
}
