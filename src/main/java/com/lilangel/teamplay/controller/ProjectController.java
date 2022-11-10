package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.ProjectNotFoundException;
import com.lilangel.teamplay.models.Project;
import com.lilangel.teamplay.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    /**
     * Параметр запроса "id"
     */
    private static final String ID = "id";
    /**
     * Параметр запроса "name"
     */
    private static final String NAME = "name";

    /**
     * Параметр запроса "team_id"
     */
    private static final String TEAM_ID = "team_id";
    /**
     * Параметр запроса "description"
     */
    private static final String DESCRIPTION = "description";

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Возвращает информацию о проекте по id
     *
     * @param id идентификатор проекта
     * @return ответ с информацией о проекте и HTTP-статусом 200
     * @throws ProjectNotFoundException если проект с заданным идентификатором не был найден
     */
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Project> getById(@PathVariable Integer id) throws ProjectNotFoundException {
        Project project = projectService.getById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    /**
     * Возвращает информацию о всех проектах
     *
     * @return ответ с информацией о всех проектах и HTTP-статусом 200
     */
    @GetMapping(value = "/getall")
    public ResponseEntity<List<Project>> getAll() {
        List<Project> allProjects = projectService.getAll();
        return new ResponseEntity<>(allProjects, HttpStatus.OK);
    }

    /**
     * Создает новый проект
     *
     * @param name        название проекта
     * @param teamId      Идентификатор команды
     * @param description описание проекта
     * @return ответ с информацией о всех проектах и HTTP-статусом 201
     */
    @PostMapping(value = "/create")
    public ResponseEntity<String> create(
            @RequestParam(NAME) String name,
            @RequestParam(TEAM_ID) Integer teamId,
            @RequestParam(DESCRIPTION) String description) {
        Integer createdId = projectService.saveNewProject(name, teamId, description);
        return new ResponseEntity<>(createdId.toString(), HttpStatus.CREATED);
    }

    /**
     * Удаляет проект по его идентификатору
     *
     * @param id идентификатор проекта
     * @return ответ с HTTP-статусом 200
     * @throws ProjectNotFoundException если проект с заданным идентификатором не был найден
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) throws ProjectNotFoundException {
        ProjectService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
