package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.tgbot.Bot;
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
     * Запрос на создание проекта
     */
    private final String CREATE_REQUEST = "/ticket create project_id=1 priority=High " +
            "status=Open short_description=Minor bug full_description=Highly important minor bug employer_id=1";

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
        String request = "/ticket help";
        String response = ticketHandler.requestHandler(request, new HashMap<>());
        assertTrue(response.startsWith("/ticket Help:"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "wrong"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String request = "/ticket wrong";
        String response = ticketHandler.requestHandler(request, new HashMap<>());
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_all"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Tickets:"
     */
    @Test
    public void getAllTest() {
        String request = "/ticket get_all";
        ticketHandler.requestHandler(CREATE_REQUEST, new HashMap<>());
        String response = ticketHandler.requestHandler(request, new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Tickets:"));
    }

    /**
     * Тестирует метод {@link TicketHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_by_id"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Ticket:"
     */
    @Test
    public void getByIdTest() {
        String createdId = ticketHandler.requestHandler(CREATE_REQUEST, Bot.parseArgs(CREATE_REQUEST));
        var parsedResp = createdId.split(" ");
        String request = "/ticket get_by_id id=" + parsedResp[parsedResp.length - 1];
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = ticketHandler.requestHandler(request, args);
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
        String createdId = ticketHandler.requestHandler(CREATE_REQUEST, Bot.parseArgs(CREATE_REQUEST));
        var parsedResp = createdId.split(" ");
        String request = "/ticket delete_by_id id=" + parsedResp[parsedResp.length - 1];
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = ticketHandler.requestHandler(request, args);
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
        String request = "/ticket delete_by_id id=-1";
        Map<String, String> args = new HashMap<>();
        args.put("id", "-1");
        String response = ticketHandler.requestHandler(request, args);
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
        Map<String, String> args = new HashMap<>();
        args.put("project_id", "1");
        args.put("priority", "High");
        args.put("status","Open");
        args.put("short_description", "Minor bug");
        args.put("full_description", "Highly important minor bug");
        args.put("employer_id", "1");
        String response = ticketHandler.requestHandler(CREATE_REQUEST, args);
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
        String response = ticketHandler.requestHandler("/team create", new HashMap<>());
        assertNotNull(response);
        System.out.println(response);
        assertTrue(response.startsWith("Wrong args"));
    }

}