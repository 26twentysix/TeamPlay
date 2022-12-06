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
class EmployerHandlerTest {
    /**
     * Аргументы для создания сотрудника
     */
    private final Map<String, String> CREATE_ARGS = Map.of(
            "name", "John Doe",
            "email", "johndoe@test.com",
            "team_id", "1");

    private final EmployerHandler employerHandler;

    @Autowired
    public EmployerHandlerTest(EmployerHandler employerHandler) {
        this.employerHandler = employerHandler;
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "help"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "/employer Help:"
     */
    @Test
    public void getHelpMessageTest() {
        String response = employerHandler.requestHandler("help", new HashMap<>());
        assertTrue(response.startsWith("/employer Help:"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "wrong"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String response = employerHandler.requestHandler("wrong", new HashMap<>());
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_all"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Employers:"
     */
    @Test
    public void getAllTest() {
        String response = employerHandler.requestHandler("get_all", new HashMap<>());
        assertNotNull(response);
        assertTrue(response.startsWith("Employers:"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "get_by_id id={previouslyCreatedId}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Employer:"
     */
    @Test
    public void getByIdTest() {
        String createdId = employerHandler.requestHandler("create", CREATE_ARGS);
        var parsedResp = createdId.split(" ");
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = employerHandler.requestHandler("get_by_id", args);
        assertNotNull(response);
        assertTrue(response.startsWith("Employer:"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "delete_by_id id={previouslyCreatedId}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void deleteByIdTest() {
        String createdId = employerHandler.requestHandler("create", CREATE_ARGS);
        var parsedResp = createdId.split(" ");
        Map<String, String> args = new HashMap<>();
        args.put("id", parsedResp[parsedResp.length - 1]);
        String response = employerHandler.requestHandler("delete_by_id", args);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "delete_by_id id=-1"
     * Метод проходит проверку, если возвращается непустой ответ, начинающийся с "Employer with given ID"
     */
    @Test
    public void deleteByWrongIdTest() {
        Map<String, String> args = new HashMap<>();
        args.put("id", "-1");
        String response = employerHandler.requestHandler("delete_by_id", args);
        assertNotNull(response);
        assertTrue(response.startsWith("Employer with given ID"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "create name=John Doe email=johndoe@test.com team_id=1"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void createTest() {
        String response = employerHandler.requestHandler("create", CREATE_ARGS);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String, java.util.Map)}
     * с запросом, содержащим команду "create"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Wrong args"
     */
    @Test
    public void createWithWrongArgsNum() {
        String response = employerHandler.requestHandler("create", new HashMap<>());
        assertNotNull(response);
        System.out.println(response);
        assertTrue(response.startsWith("Wrong args"));
    }

}