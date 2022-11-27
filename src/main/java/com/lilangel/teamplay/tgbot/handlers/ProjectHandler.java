package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.ProjectNotFoundException;
import com.lilangel.teamplay.models.Project;
import com.lilangel.teamplay.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class ProjectHandler extends AbstractHandler {
    /**
     * Справка
     */
    private final String HELP_MESSAGE = """
            /project Help:
                `/project help` - print this message
                `/project get_all` - get all projects
                `/project get_by_id id={id}` - get project by id
                `/project create name={name} description={description} team_id={team_id}` - create new project
                `/project delete_by_id id={id}` - delete project""";

    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try `/project help` to get available commands";

    private final ProjectService projectService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public ProjectHandler(ProjectService projectService) {
        this.projectService = projectService;
        handlers.put("help", this::helpMessage);
        handlers.put("get_all", this::getAll);
        handlers.put("create", this::create);
        handlers.put("get_by_id", this::getById);
        handlers.put("delete_by_id", this::deleteById);
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/project"
     *
     * @param request строка сообщения
     * @param args аргументы
     * @return строка ответа
     */
    @Override
    public String requestHandler(String request, Map<String, String> args) {
        String command;
        int indexOfSpace = request.indexOf(" ");
        if (indexOfSpace == -1) {
            command = "help";
        } else {
            var parsed = request.split(" ");
            command = parsed[1];
        }
        if (handlers.containsKey(command)) {
            return handlers.get(command).apply(args);
        }
        return WRONG_COMMAND_MESSAGE;
    }

    /**
     * @param args аргументы
     * @return строка справки
     */
    private String helpMessage(Map<String, String> args) {
        return HELP_MESSAGE;
    }

    /**
     * Возвращает информацию о всех проектах
     *
     * @param args аргументы
     * @return строка с информацией о всех проектах
     */
    private String getAll(Map<String, String> args) {
        String template = """
                \t\t\t\tID: %d
                \t\t\t\tName: %s
                \t\t\t\tTeam ID: %d
                \t\t\t\tDescription: %s
                
                """;
        StringBuilder response = new StringBuilder("Projects:\n");
        List<Project> projects = projectService.getAll();
        for (Project p : projects) {
            response.append(String.format(template, p.getId(), p.getName(), p.getTeamId(), p.getDescription()));
        }
        return response.toString();
    }

    /**
     * Возвращает информацию о проекте по его идентификатору
     *
     * @param args аргументы
     * @return строка с информацией о проекте
     */
    private String getById(Map<String, String> args) {
        String template = """
                Project:
                    ID: %d
                    Name: %s
                    Team ID: %d
                    Description: %s""";
        Project project;
        try {
            project = projectService.getById(Integer.parseInt(args.get("id")));
        } catch (ProjectNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, project.getId(), project.getName(), project.getTeamId(), project.getDescription());
    }

    /**
     * Создает новый проект
     *
     * @param args аргументы
     * @return строка с идентификатором созданного проекта
     */
    private String create(Map<String, String> args) {
        if (args.size() != 3) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew project ID: %s";
        String createdId = projectService.create(
                        args.get("name"),
                        args.get("description"),
                        Integer.parseInt(args.get("team_id")))
                .toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет проект по идентификатору
     *
     * @param args аргументы
     * @return строка с результатом
     */

    private String deleteById(Map<String, String> args) {
        try {
            projectService.deleteById(Integer.parseInt(args.get("id")));
        } catch (ProjectNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
