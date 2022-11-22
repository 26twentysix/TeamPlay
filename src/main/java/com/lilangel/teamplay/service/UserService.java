package com.lilangel.teamplay.service;

import com.lilangel.teamplay.exception.UserNotFoundException;
import com.lilangel.teamplay.models.User;

import java.util.List;

public interface UserService {
    /**
     * Добавляет нового пользователя
     *
     * @param tgId       описание
     * @param employerId идентификатор сотрудника
     * @param isAdmin    является ли пользователь админом
     * @return ответ с идентификатором пользователя
     */
    Integer saveNewUser(Integer tgId, Integer employerId, Boolean isAdmin);

    /**
     * Удаляет пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     * @throws UserNotFoundException если пользователь с заданным идентификатором не найден
     */
    void deleteById(Integer id) throws UserNotFoundException;

    /**
     * Возвращает информацию о пользователе по идентификатору
     *
     * @param id идентификатор пользователя
     * @return ответ с информацией о пользователе
     * @throws UserNotFoundException если пользователь с заданным идентификатором не найден
     */
    User getById(Integer id) throws UserNotFoundException;

    /**
     * Позволяет получить информацию обо всех пользователях
     *
     * @return ответ с информацией обо всех пользователях
     */
    List<User> getAll();

}
