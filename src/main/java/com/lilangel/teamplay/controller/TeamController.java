package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.TeamNotFoundException;
import com.lilangel.teamplay.models.Team;
import com.lilangel.teamplay.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController extends AbstractController {

    /**
     * Параметр запроса "id"
     */
    private static final String ID = "id";
    /**
     * Параметр запроса "name"
     */
    private static final String NAME = "name";
    /**
     * Параметр запроса "lead_id"
     */
    private static final String LEAD_ID = "lead_id";

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Возвращает информацию о команде по id
     *
     * @param id идентификатор команды
     * @return ответ с информацией о команде и HTTP-статусом 200
     * @throws TeamNotFoundException если команда с заданным идентификатором не была найдена
     */
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Team> getById(@PathVariable Integer id) throws TeamNotFoundException {
        Team team = teamService.getById(id);
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    /**
     * Возвращает информацию о всех командах
     *
     * @return ответ с информацией о всех командах и HTTP-статусом 200
     */
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Team>> getAll() {
        List<Team> allTeams = teamService.getAll();
        return new ResponseEntity<>(allTeams, HttpStatus.OK);
    }

    /**
     * Создает новую команду
     *
     * @param name   название команды
     * @param leadID Идентификатор лидера команды
     * @return ответ с информацией о всех командах и HTTP-статусом 201
     */
    @PostMapping(value = "/create")
    public ResponseEntity<String> create(
            @RequestParam(NAME) String name,
            @RequestParam(LEAD_ID) Integer leadID) {
        Integer createdId = teamService.saveNewTeam(name, leadID);
        return new ResponseEntity<>(createdId.toString(), HttpStatus.CREATED);
    }

    /**
     * Удаляет команду по его идентификатору
     *
     * @param id идентификатор команды
     * @return ответ с HTTP-статусом 200
     * @throws TeamNotFoundException если команда с заданным идентификатором не была найдена
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) throws TeamNotFoundException {
        teamService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
