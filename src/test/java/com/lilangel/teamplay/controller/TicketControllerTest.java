package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.TicketNotFoundException;
import com.lilangel.teamplay.models.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class TicketControllerTest {

    private final TicketController ticketController;

    @Autowired
    public TicketControllerTest(TicketController ticketController) {
        this.ticketController = ticketController;
    }

    /**
     * Тестирует метод {@link TicketController#getById(Integer)}.
     * Метод проходит проверку, если запрос на получение тикета возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getByIdTest() throws TicketNotFoundException {
        Integer createdId = Integer.parseInt(ticketController.create(1, "high priority",
                "first task", "todo many tasks", 2).getBody());
        ResponseEntity<Ticket> response = ticketController.getById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link TicketController#getAll()}.
     * Метод проходит проверку, если запрос на получение всех тикетов возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getAllTest() {
        ticketController.create(1, "high priority",
                "first task", "todo many tasks", 2);
        ticketController.create(1, "high priority",
                "first task", "todo many tasks", 2);
        ResponseEntity<List<Ticket>> response = ticketController.getAll();
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link TicketController#create(Integer, String, String, String, Integer)}.
     * Метод проходит проверку, если запрос на создание тикета возвращает ответ с HTTP-статусом 201.
     */
    @Test
    public void createTest() {
        HttpStatus actual = ticketController.create(1, "high priority",
                "first task", "todo many tasks", 2).getStatusCode();
        HttpStatus expected = HttpStatus.CREATED;
        assertEquals(expected, actual);
    }

    /**
     * Тестирует метод {@link TicketController#deleteById(Integer)}.
     * Метод проходит проверку, если запрос на удаление тикета возвращает ответ с HTTP-статусом 200.
     */
    @Test
    public void deleteTest() throws TicketNotFoundException {
        Integer createdId = Integer.parseInt(ticketController.create(1, "high priority",
                "first task", "todo many tasks", 2).getBody());
        ResponseEntity<?> response = ticketController.deleteById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
    }
}
