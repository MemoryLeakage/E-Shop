package com.eshop.controllers;

import com.eshop.business.user.responses.GetUserInfoResponse;
import com.eshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<GetUserInfoResponse> getUserInfo() {
        GetUserInfoResponse userInfo = userService.getUserInfo();
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }
}
