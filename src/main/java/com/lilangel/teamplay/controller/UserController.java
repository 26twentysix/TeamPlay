package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.UserNotFoundException;
import com.lilangel.teamplay.models.User;
import com.lilangel.teamplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    /**
     * Параметр запроса "employerID"
     */
    private static final String EMPLOYER_ID = "employerId";
    /**
     * Параметр запроса "tgID"
     */
    private static final String TG_ID = "tgId";
    /**
     * Параметр запроса "isAdmin"
     */
    private static final String IS_ADMIN = "isAdmin";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Возвращает информацию о пользователе по id
     *
     * @param id идентификатор пользователя
     * @return ответ с информацией о пользователе и HTTP-статусом 200
     * @throws UserNotFoundException если пользователь с заданным идентификатором не был найден
     */
    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) throws UserNotFoundException {
        User user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Возвращает информацию о всех пользователях
     *
     * @return ответ с информацией о всех пользователях и HTTP-статусом 200
     */
    @Override
    @GetMapping(value = "/")
    public ResponseEntity<List<User>> getAll() {
        List<User> allUsers = userService.getAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);

    }

    /**
     * Создает нового пользователя
     *
     * @param employerID Идентификатор сотрудника
     * @param tgId       ссылка на телеграм
     * @param isAdmin    является ли данный пользователь админом или нет
     * @return ответ с информацией о всех пользователях и HTTP-статусом 201
     */
    @PostMapping(value = "/")
    public ResponseEntity<String> create(
            @RequestParam(EMPLOYER_ID) Long employerID,
            @RequestParam(TG_ID) Integer tgId,
            @RequestParam(IS_ADMIN) Boolean isAdmin) {
        Integer createdId = userService.create(employerID, tgId, isAdmin);
        return new ResponseEntity<>(createdId.toString(), HttpStatus.CREATED);
    }

    /**
     * Удаляет пользователя по его идентификатору
     *
     * @param id идентификатор пользователя
     * @return ответ с HTTP-статусом 200
     * @throws UserNotFoundException если пользователь с заданным идентификатором не был найден
     */
    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) throws UserNotFoundException {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
