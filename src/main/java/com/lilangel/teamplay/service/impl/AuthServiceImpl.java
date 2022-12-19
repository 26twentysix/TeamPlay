package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.service.AuthService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService {
    HashMap<String, Instant> passwords = new HashMap<>();

    @Override
    public String generatePass(boolean isAdmin) {
        int passwordLen = 15;

        if (isAdmin) {
            passwordLen = 8;
        }

        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < passwordLen; i++) {
            int rand = (int) (3 * Math.random());

            switch (rand) {
                case 0 -> builder.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
                case 1 -> builder.append(alphabet.toUpperCase().charAt((int) (Math.random() * alphabet.length())));
                case 2 -> builder.append((int) (Math.floor(Math.random() * 10)));
            }
        }

        String password = builder.toString();

        passwords.put(password, Instant.now().plus(Duration.ofMinutes(15)));

        return password;
    }

    @Override
    public Boolean isValid(String password) {
        Instant expiresAt = passwords.get(password);
        Boolean expirationStatus = Instant.now().isAfter(expiresAt);

        passwords.remove(password);

        return expirationStatus;
    }
}
