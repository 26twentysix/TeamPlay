package com.lilangel.teamplay.exception;

/**
 * Сообщает о том, что не существует пользователя,
 * соответствующего переданному идентификатору
 */
public class EmployerNotFoundException extends OurNotFoundException {

    public EmployerNotFoundException() {
        super("Employer with given ID was not found.");
    }
}