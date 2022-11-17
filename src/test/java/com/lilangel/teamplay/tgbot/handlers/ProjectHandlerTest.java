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
class ProjectHandlerTest {
    /**
     * Запрос на создание проекта
     */
    private final String CREATE_REQUEST = "/project create name=NewProject team_id=2 description=New Project";

    private final ProjectHandler projectHandler;

    @Autowired
    public ProjectHandlerTest(ProjectHandler projectHandler) {
        this.projectHandler = projectHandler;
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "help"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "/project Help:"
     */
    @Test
    public void getHelpMessageTest() {
        String request = "/project help";
        String response = projectHandler.requestHandler(request, new HashMap<>());
        assertTrue(response.startsWith("/project Help:"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "wrongCommand"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String request = "/project wrongCommand";
        String response = projectHandler.requestHandler(request, new HashMap<>());
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "getAll"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Projects:"
     */
    @Test
    public void getAllTest() {
        String request = "/project getAll";
        projectHandler.requestHandler(CREATE_REQUEST, new HashMap<>());
        String response = projectHandler.requestHandler(request, new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Projects:"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "getById"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Project:"
     */
    @Test
    public void getByIdTest() {
        String createdId = projectHandler.requestHandler(CREATE_REQUEST, new HashMap<>());
        var parsedResp = createdId.split(" ");
        String request = "/project getById id=" + parsedResp[parsedResp.length - 1];
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = projectHandler.requestHandler(request, args);
        assertNotNull(response);
        assertTrue(response.startsWith("Project:"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "deleteById id={previouslyCreatedId}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void deleteByIdTest() {
        String createdId = projectHandler.requestHandler(CREATE_REQUEST, new HashMap<>());
        var parsedResp = createdId.split(" ");
        String request = "/project deleteById id=" + parsedResp[parsedResp.length - 1];
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = projectHandler.requestHandler(request, args);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "deleteById id=-1"
     * Метод проходит проверку, если возвращается непустой ответ, начинающийся с "Project with given ID"
     */
    @Test
    public void deleteByWrongIdTest() {
        String request = "/project deleteById id=-1";
        Map<String, String> args = new HashMap<>();
        args.put("id", "-1");
        String response = projectHandler.requestHandler(request, args);
        assertNotNull(response);
        assertTrue(response.startsWith("Project with given ID"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "create name=NewProject team_id=2 description=New Project"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void createTest() {
        Map<String, String> args = new HashMap<>();
        args.put("name", "NewProject");
        args.put("team_id", "2");
        args.put("description", "New Project");
        String response = projectHandler.requestHandler(CREATE_REQUEST, args);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "create"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Wrong args"
     */
    @Test
    public void createWithWrongArgsNum() {
        String response = projectHandler.requestHandler("/project create", new HashMap<>());
        assertNotNull(response);
        System.out.println(response);
        assertTrue(response.startsWith("Wrong args"));
    }

}