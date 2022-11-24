package com.lilangel.teamplay.service;

import com.lilangel.teamplay.exception.TicketNotFoundException;
import com.lilangel.teamplay.models.Ticket;
import com.lilangel.teamplay.models.User;

import java.util.List;

public interface TicketService {
    /**
     * Добавляет нового пользователя
     *
     * @param projectId        идентификатор проекта
     * @param priority         приоритет тикета
     * @param status           текущий статус тикета
     * @param shortDescription короткое описание тикета
     * @param fullDescription  полное описание тикета
     * @param employerId       идентификатор админа
     * @return ответ с идентификатором тикета
     */
    Integer saveNewTicket(Integer projectId, String priority, String status, String shortDescription, String fullDescription, Integer employerId);

    /**
     * Удаляет тикет по идентификатору
     *
     * @param id идентификатор тикета
     * @throws TicketNotFoundException если тикет с заданным идентификатором не найден
     */
    void deleteById(Integer id) throws TicketNotFoundException;

    /**
     * Возвращает информацию о тикете по идентификатору
     *
     * @param id идентификатор тикета
     * @return ответ с информацией о тикете
     * @throws TicketNotFoundException если тикет с заданным идентификатором не найден
     */
    Ticket getById(Integer id) throws TicketNotFoundException;

    /**
     * Позволяет получить информацию обо всех тикетах
     *
     * @return ответ с информацией обо всех тикетах
     */
    List<Ticket> getAll();
}

