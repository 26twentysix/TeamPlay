package com.lilangel.teamplay.tgbot.handlers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void getHelpMessageTest() {
        String request = "/employers help";
        String response = employerHandler.requestHandler(request);
        assertTrue(response.startsWith("/employer Help:"));
    }

    @Test
    public void wrongCommandTest() {
        String request = "/employers wrongCommand";
        String response = employerHandler.requestHandler(request);
        assertTrue(response.startsWith("Wrong command"));
    }

    @Test
    public void getAllTest() {
        String request = "/employers getAll";
        employerHandler.requestHandler(CREATE_REQUEST);
        String response = employerHandler.requestHandler(request);
        assertNotNull(response);
        assertTrue(response.startsWith("Employers:"));
    }

    @Test
    public void getByIdTest() {
        String createdId = employerHandler.requestHandler(CREATE_REQUEST);
        var parsedResp = createdId.split(" ");
        String request = "/employers getById " + parsedResp[parsedResp.length - 1];
        String response = employerHandler.requestHandler(request);
        assertNotNull(response);
        assertTrue(response.startsWith("Employer:"));
    }

    @Test
    public void deleteByIdTest() {
        String createdId = employerHandler.requestHandler(CREATE_REQUEST);
        var parsedResp = createdId.split(" ");
        String request = "/employers deleteById " + parsedResp[parsedResp.length - 1];
        String response = employerHandler.requestHandler(request);
        assertNotNull(response);
        assertTrue(response.startsWith("Successfully"));
    }

    @Test
    public void deleteByWrongIdTest() {
        String request = "/employers deleteById " + "-1";
        String response = employerHandler.requestHandler(request);
        assertNotNull(response);
        assertTrue(response.startsWith("Employer with given ID"));
    }

    @Test
    public void createTest() {
        String response = employerHandler.requestHandler(CREATE_REQUEST);
        assertNotNull(response);
    }


}