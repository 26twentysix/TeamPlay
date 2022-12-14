package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employers")
public class EmployerController extends AbstractController<Employer> {

    /**
     * Параметр запроса "name"
     */
    private static final String NAME = "name";
    /**
     * Параметр запроса "email"
     */
    private static final String EMAIL = "email";
    /**
     * Параметр запроса "team_id"
     */
    private static final String TEAM_ID = "team_id";

    private final EmployerService employerService;

    @Autowired
    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    /**
     * Возвращает информацию о сотруднике по id
     *
     * @param id идентификатор сотрудника
     * @return ответ с информацией о сотруднике и HTTP-статусом 200
     * @throws EmployerNotFoundException если сотрудник с заданным идентификатором не был найден
     */
    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<Employer> getById(@PathVariable Integer id) throws EmployerNotFoundException {
        Employer employer = employerService.getById(id);
        return new ResponseEntity<>(employer, HttpStatus.OK);
    }

    /**
     * Возвращает информацию о всех сотрудниках
     *
     * @return ответ с информацией о всех сотрудниках и HTTP-статусом 200
     */
    @Override
    @GetMapping(value = "/")
    public ResponseEntity<List<Employer>> getAll() {
        List<Employer> allEmployers = employerService.getAll();
        return new ResponseEntity<>(allEmployers, HttpStatus.OK);

    }

    /**
     * Создает нового сотрудника
     *
     * @param name   Имя Фамилия сотрудника
     * @param email  Адрес электронной почты
     * @param teamId Идентификатор команды
     * @return ответ с информацией о всех сотрудниках и HTTP-статусом 201
     */
    @PostMapping(value = "/")
    public ResponseEntity<String> create(
            @RequestParam(NAME) String name,
            @RequestParam(EMAIL) String email,
            @RequestParam(TEAM_ID) Integer teamId) {
        Integer createdId = employerService.create(name, email, teamId);
        return new ResponseEntity<>(createdId.toString(), HttpStatus.CREATED);
    }

    /**
     * Удаляет сотрудника по его идентификатору
     *
     * @param id идентификатор сотрудника
     * @return ответ с HTTP-статусом 200
     * @throws EmployerNotFoundException если сотрудник с заданным идентификатором не был найден
     */
    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) throws EmployerNotFoundException {
        employerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}