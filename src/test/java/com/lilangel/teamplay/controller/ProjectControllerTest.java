package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.ProjectNotFoundException;
import com.lilangel.teamplay.models.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ProjectControllerTest {

    @Autowired
    public ProjectControllerTest(ProjectController projectController) {
        this.projectController = projectController;
    }

    /**
     * Тестирует метод {@link ProjectController#getById(Integer)}.
     * Метод проходит проверку, если запрос на получение проекта возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getByIdTest() throws ProjectNotFoundException {
        Integer createdId = Integer.parseInt(projectController.create("BettaTest", 1, "нужно придумать задачу").getBody());
        ResponseEntity<Project> response = projectController.getById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link ProjectController#getAll()}.
     * Метод проходит проверку, если запрос на получение всех проектов возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getAllTest() {
        projectController.create("BettaTest", 1, "первая заметка");
        projectController.create("GammaTest", 2, "вторая заметка");
        ResponseEntity<List<Project>> response = projectController.getAll();
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link ProjectController#create(String, Integer, String)}.
     * Метод проходит проверку, если запрос на создание проекта возвращает ответ с HTTP-статусом 201.
     */
    @Test
    public void createTest() {
        HttpStatus actual = projectController.create("BettaTest", 1, "первая заметка").getStatusCode();
        HttpStatus expected = HttpStatus.CREATED;
        assertEquals(expected, actual);
    }

    /**
     * Тестирует метод {@link ProjectController#deleteById(Integer)}.
     * Метод проходит проверку, если запрос на удаление проекта возвращает ответ с HTTP-статусом 200.
     */
    @Test
    public void deleteTest() throws ProjectNotFoundException {
        Integer createdId = Integer.parseInt(projectController.create("BettaTest", 1, "первая заметка").getBody());
        ResponseEntity<?> response = projectController.deleteById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
    }
}
