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
     * Аргументы для создания проекта
     */
    private final Map<String, String> CREATE_ARGS = Map.of(
            "name", "New Project",
            "team_id", "2",
            "description", "New Description");

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
        String response = projectHandler.requestHandler("help", new HashMap<>());
        assertTrue(response.startsWith("/project Help:"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "wrong"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String response = projectHandler.requestHandler("wrong", new HashMap<>());
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_all"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Projects:"
     */
    @Test
    public void getAllTest() {
        String response = projectHandler.requestHandler("get_all", new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Projects:"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_by_id id={previously_created_id}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Project:"
     */
    @Test
    public void getByIdTest() {
        String createdId = projectHandler.requestHandler("create", CREATE_ARGS);
        var parsedResp = createdId.split(" ");
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = projectHandler.requestHandler("get_by_id", args);
        assertNotNull(response);
        assertTrue(response.startsWith("Project:"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "delete_by_id id={previouslyCreatedId}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void deleteByIdTest() {
        String createdId = projectHandler.requestHandler("create", CREATE_ARGS);
        var parsedResp = createdId.split(" ");
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = projectHandler.requestHandler("delete_by_id", args);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link ProjectHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "delete_by_id id=-1"
     * Метод проходит проверку, если возвращается непустой ответ, начинающийся с "Project with given ID"
     */
    @Test
    public void deleteByWrongIdTest() {
        Map<String, String> args = new HashMap<>();
        args.put("id", "-1");
        String response = projectHandler.requestHandler("delete_by_id", args);
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
        String response = projectHandler.requestHandler("create", CREATE_ARGS);
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
        String response = projectHandler.requestHandler("create", new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Wrong args"));
    }

}