package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate-pass")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(value = "/admin-pass")
    public ResponseEntity<String> getAdminPass () {
        String password = authService.generatePass(true);
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    @GetMapping(value = "/user-pass")
    public ResponseEntity<String> getUserPass () {
        String password = authService.generatePass(false);
        return new ResponseEntity<>(password, HttpStatus.OK);
    }

}
