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
class TeamHandlerTest {
    /**
     * Запрос на создание проекта
     */
    private final String CREATE_REQUEST = "/team create name=NewTeam lead_id=2";

    private final TeamHandler teamHandler;

    @Autowired
    public TeamHandlerTest(TeamHandler teamHandler) {
        this.teamHandler = teamHandler;
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "help"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "/team Help:"
     */
    @Test
    public void getHelpMessageTest() {
        String request = "/team help";
        String response = teamHandler.requestHandler(request, new HashMap<>());
        assertTrue(response.startsWith("/team Help:"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "wrongCommand"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String request = "/team wrong";
        String response = teamHandler.requestHandler(request, new HashMap<>());
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_all"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Teams:"
     */
    @Test
    public void getAllTest() {
        String request = "/team get_all";
        teamHandler.requestHandler(CREATE_REQUEST, new HashMap<>());
        String response = teamHandler.requestHandler(request, new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Teams:"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_by_id"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Team:"
     */
    @Test
    public void getByIdTest() {
        String createdId = teamHandler.requestHandler(CREATE_REQUEST, Bot.parseArgs(CREATE_REQUEST));
        var parsedResp = createdId.split(" ");
        String request = "/team get_by_id id=" + parsedResp[parsedResp.length - 1];
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = teamHandler.requestHandler(request, args);
        assertNotNull(response);
        assertTrue(response.startsWith("Team:"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "delete_by_id id={previouslyCreatedId}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void deleteByIdTest() {
        String createdId = teamHandler.requestHandler(CREATE_REQUEST, Bot.parseArgs(CREATE_REQUEST));
        var parsedResp = createdId.split(" ");
        String request = "/team delete_by_id id=" + parsedResp[parsedResp.length - 1];
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = teamHandler.requestHandler(request, args);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "deleteById id=-1"
     * Метод проходит проверку, если возвращается непустой ответ, начинающийся с "Team with given ID"
     */
    @Test
    public void deleteByWrongIdTest() {
        String request = "/team delete_by_id id=-1";
        Map<String, String> args = new HashMap<>();
        args.put("id", "-1");
        String response = teamHandler.requestHandler(request, args);
        assertNotNull(response);
        assertTrue(response.startsWith("Team with given ID"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "create name=NewTeam lead_id=2"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void createTest() {
        Map<String, String> args = new HashMap<>();
        args.put("name", "NewTeam");
        args.put("lead_id", "2");
        String response = teamHandler.requestHandler(CREATE_REQUEST, args);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "create"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Wrong args"
     */
    @Test
    public void createWithWrongArgsNum() {
        String response = teamHandler.requestHandler("/team create", new HashMap<>());
        assertNotNull(response);
        System.out.println(response);
        assertTrue(response.startsWith("Wrong args"));
    }

}