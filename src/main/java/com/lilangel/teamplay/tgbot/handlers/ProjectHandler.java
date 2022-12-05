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
    private final String HELP_MESSAGE;

    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE;
    /**
     * Количество аргументов, требуемое для создания проекта
     */
    private final Integer ARGS_COUNT_TO_CREATE;
    private final ProjectService projectService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public ProjectHandler(ProjectService projectService) {
        this.projectService = projectService;
        handlers.put("help", this::help);
        handlers.put("get_all", this::getAll);
        handlers.put("create", this::create);
        handlers.put("get_by_id", this::getById);
        handlers.put("delete_by_id", this::deleteById);
        HELP_MESSAGE = """
                /project Help:
                    `/project help` - print this message
                    `/project get_all` - get all projects
                    `/project get_by_id id={id}` - get project by id
                    `/project create name={name} description={description} team_id={team_id}` - create new project
                    `/project delete_by_id id={id}` - delete project""";
        WRONG_COMMAND_MESSAGE = "Wrong command, try `/project help` to get available commands";
        ARGS_COUNT_TO_CREATE = 3;
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/project"
     *
     * @param command команда
     * @param args    аргументы
     * @return строка ответа
     */
    @Override
    public String requestHandler(String command, Map<String, String> args) {
        if (handlers.containsKey(command)) {
            return handlers.get(command).apply(args);
        }
        return getWrongCommandMessage();
    }

    @Override
    protected String getHelpMessage() {
        return HELP_MESSAGE;
    }

    /**
     * @return сообщение о неверной команде
     */
    @Override
    protected String getWrongCommandMessage() {
        return WRONG_COMMAND_MESSAGE;
    }

    @Override
    protected String help(Map<String, String> args) {
        return getHelpMessage();
    }

    /**
     * Возвращает информацию о всех проектах
     *
     * @param args аргументы
     * @return строка с информацией о всех проектах
     */
    @Override
    protected String getAll(Map<String, String> args) {
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
    @Override
    protected String getById(Map<String, String> args) {
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
    @Override
    protected String create(Map<String, String> args) {
        if (args.size() != ARGS_COUNT_TO_CREATE) {
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
    @Override
    protected String deleteById(Map<String, String> args) {
        try {
            projectService.deleteById(Integer.parseInt(args.get("id")));
        } catch (ProjectNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
