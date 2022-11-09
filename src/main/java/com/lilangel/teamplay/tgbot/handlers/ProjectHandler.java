package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.ProjectNotFoundException;
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
                `/project help` - print this message""";

    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try `/project help` to get available commands";

    private final ProjectService projectService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<List<String>, String>> handlers = new HashMap<>();

    @Autowired
    public ProjectHandler(ProjectService projectService) {
        this.projectService = projectService;
        handlers.put("help", this::helpMessage);
        handlers.put("getAll", this::getAll);
        handlers.put("create", this::create);
        handlers.put("getById", this::getById);
        handlers.put("deleteById", this::deleteById);
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/project"
     *
     * @param request строка сообщения
     * @return строка ответа
     */
    @Override
    public String requestHandler(String request) {
        String command;
        List<String> args = new ArrayList<>();
        int indexOfSpace = request.indexOf(" ");
        if (indexOfSpace == -1) {
            command = "help";
        } else {
            var parsed = request.split(" ");
            command = parsed[1];
            args.addAll(Arrays.asList(parsed).subList(2, parsed.length));
        }
        if (handlers.containsKey(command)) {
            return handlers.get(command).apply(args);
        }
        return WRONG_COMMAND_MESSAGE;
    }

    /**
     * @param args список аргументов
     * @return строка справки
     */
    private String helpMessage(List<String> args) {
        return HELP_MESSAGE;
    }

    /**
     * Возвращает информацию о всех проектах
     *
     * @param args список аргументов
     * @return строка с информацией о всех проектах
     */
    private String getAll(List<String> args) {
        String template = """
                \t\t\t\tID: %d
                \t\t\t\tName: %s
                \t\t\t\tTeam ID: %d
                \t\t\t\tDescription: %s
                
                """;
        StringBuilder response = new StringBuilder("Projects:\n");
        List<Project> projects = projectService.getAll();
        for (Project p : projects) {
            response.append(String.format(template, p.getId(), p.getName(), p.getTeamId(), p.getDescription));
        }
        return response.toString();
    }

    /**
     * Возвращает информацию о проекте по его идентификатору
     *
     * @param args список аргументов, args[0] - Integer id
     * @return строка с информацией о проекте
     */
    private String getById(List<String> args) {
        String template = """
                Project:
                    ID: %d
                    Name: %s
                    Team ID: %d
                    Description: %s""";
        Project project;
        try {
            project = projectService.getById(Integer.parseInt(args.get(0)));
        } catch (ProjectNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, project.getId(), project.getName(), project.getTeamId(), project.getDescription());
    }

    /**
     * Создает новый проект
     *
     * @param args список аргументов, args[0] - имя, args[1] - team id, args[2] - описание
     * @return строка с идентификатором созданного проекта
     */
    private String create(List<String> args) {
        if (args.size() != 3) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew project ID: %s";
        String createdId = projectService.saveNewProject(
                        args.get(0),
                        Integer.parseInt(args.get(1)),
                        args.get(2))
                .toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет проект по идентификатору
     *
     * @param args список аргументов, args[0] - Integer id
     * @return строка с результатом
     */

    private String deleteById(List<String> args) {
        try {
            projectService.deleteById(Integer.parseInt(args.get(0)));
        } catch (ProjectNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
