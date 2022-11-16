package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.TeamNotFoundException;
import com.lilangel.teamplay.models.Team;
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
public class TeamControllerTest {

    private final TeamController teamController;

    @Autowired
    public TeamControllerTest(TeamController teamController) {
        this.teamController = teamController;
    }

    /**
     * Тестирует метод {@link TeamController#getById(Integer)}.
     * Метод проходит проверку, если запрос на получение команды возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getByIdTest() throws TeamNotFoundException {
        Integer createdId = Integer.parseInt(teamController.create("Entity", 1).getBody());
        ResponseEntity<Team> response = teamController.getById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link TeamController#getAll()}.
     * Метод проходит проверку, если запрос на получение всех команд возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getAllTest() {
        teamController.create("Entity", 1);
        teamController.create("Entity", 1);
        ResponseEntity<List<Team>> response = teamController.getAll();
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link TeamController#create(String, Integer)}.
     * Метод проходит проверку, если запрос на создание команды возвращает ответ с HTTP-статусом 201.
     */
    @Test
    public void createTest() {
        HttpStatus actual = teamController.create("Entity", 1).getStatusCode();
        HttpStatus expected = HttpStatus.CREATED;
        assertEquals(expected, actual);
    }

    /**
     * Тестирует метод {@link TeamController#deleteById(Integer)}.
     * Метод проходит проверку, если запрос на удаление команды возвращает ответ с HTTP-статусом 200.
     */
    @Test
    public void deleteTest() throws TeamNotFoundException {
        Integer createdId = Integer.parseInt(teamController.create("Entity", 1).getBody());
        ResponseEntity<?> response = teamController.deleteById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
    }
}
