package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.TicketNotFoundException;
import com.lilangel.teamplay.models.Ticket;
import com.lilangel.teamplay.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController extends AbstractController<Ticket> {

    /**
     * Параметр запроса "projectID"
     */
    private static final String PROJECT_ID = "projectID";

    /**
     * Параметр запроса "importance"
     */
    private static final String PRIORITY = "priority";
    /**
     * Параметр запроса "shortDescription"
     */
    private static final String STATUS = "status";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    /**
     * Параметр запроса "fullDescription"
     */
    private static final String FULL_DESCRIPTION = "fullDescription";
    /**
     * Параметр запроса "employerID"
     */
    private static final String EMPLOYER_ID = "employerID";

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Возвращает информацию о тикете по id
     *
     * @param id идентификатор тикета
     * @return ответ с информацией о тикете и HTTP-статусом 200
     * @throws TicketNotFoundException если тикет с заданным идентификатором не был найден
     */
    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable Integer id) throws TicketNotFoundException {
        Ticket ticket = ticketService.getById(id);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    /**
     * Возвращает информацию о всех тикетах
     *
     * @return ответ с информацией о всех тикетах и HTTP-статусом 200
     */
    @Override
    @GetMapping(value = "/")
    public ResponseEntity<List<Ticket>> getAll() {
        List<Ticket> allTickets = ticketService.getAll();
        return new ResponseEntity<>(allTickets, HttpStatus.OK);
    }

    /**
     * Создает новый тикет
     *
     * @param projectId        идентификатор проекта
     * @param priority         важность тикета
     * @param status           текущий статус тикета
     * @param shortDescription краткое описание
     * @param fullDescription  полное оаписание
     * @param employerId       идентификатор сотрудника
     * @return ответ с информацией о всех тикетах и HTTP-статусом 201
     */
    @PostMapping(value = "/")
    public ResponseEntity<String> create(
            @RequestParam(PROJECT_ID) Integer projectId,
            @RequestParam(PRIORITY) String priority,
            @RequestParam(STATUS) String status,
            @RequestParam(SHORT_DESCRIPTION) String shortDescription,
            @RequestParam(FULL_DESCRIPTION) String fullDescription,
            @RequestParam(EMPLOYER_ID) Integer employerId) {
        Integer createdId = ticketService.create(projectId, priority, status,
                shortDescription, fullDescription, employerId);
        return new ResponseEntity<>(createdId.toString(), HttpStatus.CREATED);
    }

    /**
     * Удаляет тикет по его идентификатору
     *
     * @param id идентификатор тикета
     * @return ответ с HTTP-статусом 200
     * @throws TicketNotFoundException если сотрудник с заданным идентификатором не был найден
     */
    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) throws TicketNotFoundException {
        ticketService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
