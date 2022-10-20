package com.lilangel.teamplay.service;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;

import java.io.IOException;
import java.util.HashMap;

public interface EmployerService {

    /**
     * Добавляет нового сотрудника
     *
     * @param name   имя и фамилия сотрудника
     * @param email  адрес электронной почты
     * @param teamId идентификатор команды
     * @return ответ с личным идентификатором сотрудника
     * @throws IOException в случае возникновения проблем при работе с файловой системой
     */
    Integer createNewEmployer(String name, String email, Integer teamId) throws IOException;

    /**
     * Удаляет сотрудника по идентификатору
     *
     * @param id идентификатор сотрудника
     */
    void deleteById(Integer id) throws EmployerNotFoundException, IOException;

    /**
     * Возвращает информацию о сотруднике по идентификатору
     *
     * @param id идентификатор сотрудника
     * @return ответ с информацией о сротруднике
     * @throws EmployerNotFoundException если пользователя с таким id не существует
     * @throws IOException               в случае возникновения проблем при работе с файловой системой
     */
    Employer getById(Integer id) throws EmployerNotFoundException, IOException;

    /**
     * Позволяет получить информацию обо всех сотрудниках
     *
     * @return ответ с информацией обо всех сотрудниках
     * @throws IOException в случае возникновения проблем при работе с файловой системой
     */
    HashMap<Integer, Employer> getAll() throws IOException;

}