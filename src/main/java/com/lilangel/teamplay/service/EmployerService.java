package com.lilangel.teamplay.service;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;

import java.util.List;

public interface EmployerService {

    /**
     * Добавляет нового сотрудника
     *
     * @param name   имя и фамилия сотрудника
     * @param email  адрес электронной почты
     * @param teamId идентификатор команды
     * @return ответ с личным идентификатором сотрудника
     */
    Integer saveNewEmployer(String name, String email, Integer teamId);

    /**
     * Удаляет сотрудника по идентификатору
     *
     * @param id идентификатор сотрудника
     * @throws EmployerNotFoundException если сотрудник с заданным идентификатором не найден
     */
    void deleteById(Integer id) throws EmployerNotFoundException;

    /**
     * Возвращает информацию о сотруднике по идентификатору
     *
     * @param id идентификатор сотрудника
     * @return ответ с информацией о сротруднике
     * @throws EmployerNotFoundException если сотрудник с заданным идентификатором не найден
     */
    Employer getById(Integer id) throws EmployerNotFoundException;

    /**
     * Позволяет получить информацию обо всех сотрудниках
     *
     * @return ответ с информацией обо всех сотрудниках
     */
    List<Employer> getAll();

}