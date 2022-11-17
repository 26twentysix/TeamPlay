package com.lilangel.teamplay.service;

import com.lilangel.teamplay.exception.TeamNotFoundException;
import com.lilangel.teamplay.models.Team;

import java.util.List;

public interface TeamService {
    /**
     * Добавляет новую команду
     *
     * @param name   название команды
     * @param leadId идентификатор тимлида
     * @return ответ с идентификатором команды
     */
    Integer saveNewTeam(String name, Integer leadId);

    /**
     * Удаляет команду по идентификатору
     *
     * @param id идентификатор команды
     * @throws TeamNotFoundException если команда с заданным идентификатором не найден
     */
    void deleteById(Integer id) throws TeamNotFoundException;

    /**
     * Возвращает информацию о проекте по идентификатору
     *
     * @param id идентификатор команды
     * @return ответ с информацией о команде
     * @throws TeamNotFoundException если команда с заданным идентификатором не найден
     */
    Team getById(Integer id) throws TeamNotFoundException;

    /**
     * Позволяет получить информацию обо всех командах
     *
     * @return ответ с информацией обо всех командах
     */
    List<Team> getAll();

}