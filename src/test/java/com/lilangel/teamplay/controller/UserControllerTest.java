package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.UserNotFoundException;
import com.lilangel.teamplay.models.User;
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
public class UserControllerTest {
    private final UserController userController;

    @Autowired
    public UserControllerTest(UserController userController) {
        this.userController = userController;
    }

    /**
     * Тестирует метод {@link UserController#getById(Integer)}.
     * Метод проходит проверку, если запрос на получение пользователя возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getByIdTest() throws UserNotFoundException {
        Integer createdId = Integer.parseInt(userController.create(1, "@Daxak", true).getBody());
        ResponseEntity<User> response = userController.getById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link UserController#getAll()}.
     * Метод проходит проверку, если запрос на получение всех пользователей возвращает ответ с HTTP-статусом 200
     * и ненулевым телом ответа.
     */
    @Test
    public void getAllTest() {
        userController.create(1, "@Daxak", true);
        userController.create(1, "@Daxak", true);
        ResponseEntity<List<User>> response = userController.getAll();
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
        assertNotNull(response.getBody());
    }

    /**
     * Тестирует метод {@link UserController#create(Integer, String, Boolean)}.
     * Метод проходит проверку, если запрос на создание пользователя возвращает ответ с HTTP-статусом 201.
     */
    @Test
    public void createTest() {
        HttpStatus actual = userController.create(1, "@Daxak", true).getStatusCode();
        HttpStatus expected = HttpStatus.CREATED;
        assertEquals(expected, actual);
    }

    /**
     * Тестирует метод {@link UserController#deleteById(Integer)}.
     * Метод проходит проверку, если запрос на удаление пользователя возвращает ответ с HTTP-статусом 200.
     */
    @Test
    public void deleteTest() throws UserNotFoundException {
        Integer createdId = Integer.parseInt(userController.create(1, "@Daxak", true).getBody());
        ResponseEntity<?> response = userController.deleteById(createdId);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = response.getStatusCode();
        assertEquals(expected, actual);
    }
}
