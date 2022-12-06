package com.lilangel.teamplay.exception;

public class UserNotFoundException extends OurNotFoundException {
    public UserNotFoundException() {
        super("User with given ID was not found.");
    }
}
