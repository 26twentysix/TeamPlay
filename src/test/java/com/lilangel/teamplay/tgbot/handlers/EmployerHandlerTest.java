package com.lilangel.teamplay.tgbot.handlers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс юнит-тестов для {@link EmployerHandler}
 */
@SpringBootTest
@Transactional
class EmployerHandlerTest {
    /**
     * Запрос на создание сотрудника
     */
    private final String CREATE_REQUEST = "/employers create John Doe johndoe@test.com 1";

    private final EmployerHandler employerHandler;

    @Autowired
    public EmployerHandlerTest(EmployerHandler employerHandler) {
        this.employerHandler = employerHandler;
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String)} с запросом, содержащим команду "help"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "/employer Help:"
     */
    @Test
    public void getHelpMessageTest() {
        String request = "/employers help";
        String response = employerHandler.requestHandler(request);
        assertTrue(response.startsWith("/employer Help:"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String)} с запросом, содержащим команду "wrongCommand"
     * Метод проходит проверку, если возрвращается ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String request = "/employers wrongCommand";
        String response = employerHandler.requestHandler(request);
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String)} с запросом, содержащим команду "getAll"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Employers:"
     */
    @Test
    public void getAllTest() {
        String request = "/employers getAll";
        employerHandler.requestHandler(CREATE_REQUEST);
        String response = employerHandler.requestHandler(request);
        assertNotNull(response);
        assertTrue(response.startsWith("Employers:"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String)} с запросом, содержащим команду "getById"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Employer:"
     */
    @Test
    public void getByIdTest() {
        String createdId = employerHandler.requestHandler(CREATE_REQUEST);
        var parsedResp = createdId.split(" ");
        String request = "/employers getById " + parsedResp[parsedResp.length - 1];
        String response = employerHandler.requestHandler(request);
        assertNotNull(response);
        assertTrue(response.startsWith("Employer:"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String)}
     * с запросом, содержащим команду "deleteById {previouslyCreatedId}"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void deleteByIdTest() {
        String createdId = employerHandler.requestHandler(CREATE_REQUEST);
        var parsedResp = createdId.split(" ");
        String request = "/employers deleteById " + parsedResp[parsedResp.length - 1];
        String response = employerHandler.requestHandler(request);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String)}
     * с запросом, содержащим команду "deleteById -1"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void deleteByWrongIdTest() {
        String request = "/employers deleteById " + "-1";
        String response = employerHandler.requestHandler(request);
        assertNotNull(response);
        assertTrue(response.startsWith("Employer with given ID"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String)}
     * с запросом, содержащим команду "create John Doe johndoe@test.com 1"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Successfully"
     */
    @Test
    public void createTest() {
        String response = employerHandler.requestHandler(CREATE_REQUEST);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    /**
     * Тестирует метод {@link EmployerHandler#requestHandler(String)}
     * с запросом, содержащим команду "create Doe johndoe@test.com 1"
     * Метод проходит проверку, если возрвращается непустой ответ, начинающийся с "Wrong args"
     */
    @Test
    public void createWithWrongArgsNum() {
        String response = employerHandler.requestHandler("/employers create Doe johndoe@test.com 1");
        assertNotNull(response);
        System.out.println(response);
        assertTrue(response.startsWith("Wrong args"));
    }

}