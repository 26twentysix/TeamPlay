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
class TeamHandlerTest {
    /**
     * Аргументы для создания команды
     */
    private final Map<String, String> CREATE_ARGS = Map.of(
            "name", "New Team",
            "lead_id", "2");

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
        String response = teamHandler.requestHandler("help", new HashMap<>());
        assertTrue(response.startsWith("/team Help:"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "wrongCommand"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String response = teamHandler.requestHandler("wrong", new HashMap<>());
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_all"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Teams:"
     */
    @Test
    public void getAllTest() {
        teamHandler.requestHandler("create", CREATE_ARGS);
        String response = teamHandler.requestHandler("get_all", new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Teams:"));
    }

    /**
     * Тестирует метод {@link TeamHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_by_id id={previously_created_id}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Team:"
     */
    @Test
    public void getByIdTest() {
        String createdId = teamHandler.requestHandler("create", CREATE_ARGS);
        var parsedResp = createdId.split(" ");
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = teamHandler.requestHandler("get_by_id", args);
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
        String createdId = teamHandler.requestHandler("create", CREATE_ARGS);
        var parsedResp = createdId.split(" ");
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = teamHandler.requestHandler("delete_by_id", args);
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
        Map<String, String> args = new HashMap<>();
        args.put("id", "-1");
        String response = teamHandler.requestHandler("delete_by_id", args);
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
        String response = teamHandler.requestHandler("create", CREATE_ARGS);
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
        String response = teamHandler.requestHandler("create", new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Wrong args"));
    }

}