package com.lilangel.teamplay.tgbot.handlers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс юнит-тестов для {@link EmployerHandler}
 */
@SpringBootTest
@Transactional
class TicketHandlerTest {
    /**
     * Аргументы для создания тикета
     */
    private final Map<String, String> CREATE_ARGS = Map.of(
            "project_id", "1",
            "priority", "high",
            "status", "open",
            "short_description", "minor bug",
            "full_description", "highly important minor bug",
            "employer_id", "1");

    private final TicketHandler ticketHandler;

    @Autowired
    public TicketHandlerTest(TicketHandler ticketHandler) {
        this.ticketHandler = ticketHandler;
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "help"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "/ticket Help:"
     */
    @Test
    public void getHelpMessageTest() {
        String response = ticketHandler.requestHandler("help", new HashMap<>());
        assertTrue(response.startsWith("/ticket Help:"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "wrong"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String response = ticketHandler.requestHandler("wrong", new HashMap<>());
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_all"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Tickets:"
     */
    @Test
    public void getAllTest() {
        ticketHandler.requestHandler("create", CREATE_ARGS);
        String response = ticketHandler.requestHandler("get_all", new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Tickets:"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_by_id id={previously_created_id}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Ticket:"
     */
    @Test
    public void getByIdTest() {
        String createdId = ticketHandler.requestHandler("create", CREATE_ARGS);
        var parsedResp = createdId.split(" ");
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = ticketHandler.requestHandler("get_by_id", args);
        assertNotNull(response);
        assertTrue(response.startsWith("Ticket:"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "delete_by_id id={previouslyCreatedId}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void deleteByIdTest() {
        String createdId = ticketHandler.requestHandler("create", CREATE_ARGS);
        var parsedResp = createdId.split(" ");
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = ticketHandler.requestHandler("delete_by_id", args);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "delete_by_id id=-1"
     * Метод проходит проверку, если возвращается непустой ответ, начинающийся с "Ticket with given ID"
     */
    @Test
    public void deleteByWrongIdTest() {
        Map<String, String> args = new HashMap<>();
        args.put("id", "-1");
        String response = ticketHandler.requestHandler("delete_by_id", args);
        assertNotNull(response);
        assertTrue(response.startsWith("Ticket with given ID"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "create project_id=1 priority=High status=Open
     * short_description=Minor bug full_description=Highly important minor bug" employer_id=1
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void createTest() {
        String response = ticketHandler.requestHandler("create", CREATE_ARGS);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "create"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Wrong args"
     */
    @Test
    public void createWithWrongArgsNum() {
        String response = ticketHandler.requestHandler("create", new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Wrong args"));
    }

}