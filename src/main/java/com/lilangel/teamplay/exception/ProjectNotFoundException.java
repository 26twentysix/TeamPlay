package com.lilangel.teamplay.exception;

public class ProjectNotFoundException extends OurNotFoundException {
    public ProjectNotFoundException() {
        super("Project with given ID was not found.");
    }
}
