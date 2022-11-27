package com.lilangel.teamplay.service;

import com.lilangel.teamplay.exception.ProjectNotFoundException;
import com.lilangel.teamplay.models.Project;

import java.util.List;

public interface ProjectService {
    /**
     * Добавляет новый проект
     *
     * @param name        название проекта
     * @param description описание
     * @param teamId      идентификатор команды
     * @return ответ с идентификатором проекта
     */
    Integer create(String name, String description, Integer teamId);

    /**
     * Удаляет проект по идентификатору
     *
     * @param id идентификатор проекта
     * @throws ProjectNotFoundException если проект с заданным идентификатором не найден
     */
    void deleteById(Integer id) throws ProjectNotFoundException;

    /**
     * Возвращает информацию о проекте по идентификатору
     *
     * @param id идентификатор проекта
     * @return ответ с информацией о проекте
     * @throws ProjectNotFoundException если проект с заданным идентификатором не найден
     */
    Project getById(Integer id) throws ProjectNotFoundException;

    /**
     * Позволяет получить информацию обо всех проектах
     *
     * @return ответ с информацией обо всех проектах
     */
    List<Project> getAll();

}