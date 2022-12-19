package com.lilangel.teamplay.service;

public interface AuthService {
    /**
     * Генерирует пароль из 15 символов со временем жизни 15 минут
     * и помещает его и время истечения в хранилище паролей.
     *
     * @param isAdmin является ли пользователь админом
     * @return сгенерированный пароль
     */
    String generatePass(boolean isAdmin);

    /**
     * Проверяет, является пароль валидным.
     * true - пароль действителен
     * false - пароль недействителен
     * После проверки пароль считается использованным и удаляется из хранилища.
     *
     * @param password пароль
     * @return ответ со статусом пароля (истёк или нет)
     */
    Boolean isValid(String password);
}
