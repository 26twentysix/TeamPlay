package com.lilangel.teamplay.exception;

/**
 * Сообщает о том, что не существует пользователя,
 * соответствующего переданному идентификатору
 */
public class EmployerNotFoundException extends Exception {

    public EmployerNotFoundException(String message) {
        super(message);
    }
}