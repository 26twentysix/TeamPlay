package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.models.Employer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Класс юнит-тестов для {@link EmployerController}
 */
@SpringBootTest
public class EmployerControllerTest {

    private final EmployerController employerController;

    @Autowired
    public EmployerControllerTest(EmployerController employerController) {
        this.employerController = employerController;
    }

    /**
     * Тестирует метод {@link EmployerController#getById(Integer)}.
     * Метод проходит проверку, если запрос на получение сотрудника возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getByIdTest() {
        Integer createdId = Integer.parseInt(employerController.create("John Doe", "johndoe@test.com", 1).getBody());
        ResponseEntity<Employer> response = employerController.getById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link EmployerController#getAll()}.
     * Метод проходит проверку, если запрос на получение всех сотрудников возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getAllTest() {
        employerController.create("John Doe", "johndoe@test.com", 1);
        employerController.create("Jane Doe", "janedoe@test.com", 1);
        ResponseEntity<HashMap<Integer, Employer>> response = employerController.getAll();
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link EmployerController#create(String, String, Integer)}.
     * Метод проходит проверку, если запрос на создание сотрудника возвращает ответ с HTTP-статусом 201.
     */
    @Test
    public void createTest() {
        HttpStatus actual = employerController.create("John Doe", "johndoe@test.com", 1).getStatusCode();
        HttpStatus expected = HttpStatus.CREATED;
        assertEquals(expected, actual);
    }

    /**
     * Тестирует метод {@link EmployerController#deleteById(Integer)}.
     * Метод проходит проверку, если запрос на удаление сотрудника возвращает ответ с HTTP-статусом 200.
     */
    @Test
    public void deleteTest() {
        Integer createdId = Integer.parseInt(employerController.create("John Doe", "johndoe@test.com", 1).getBody());
        ResponseEntity<?> response = employerController.deleteById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
    }
}
