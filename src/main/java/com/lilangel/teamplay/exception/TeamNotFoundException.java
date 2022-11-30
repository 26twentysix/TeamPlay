package com.lilangel.teamplay.exception;

public class TeamNotFoundException extends OurNotFoundException {
    public TeamNotFoundException() {
        super("Team with given ID was not found.");
    }
}
