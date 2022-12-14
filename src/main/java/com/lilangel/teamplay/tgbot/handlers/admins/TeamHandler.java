package com.lilangel.teamplay.tgbot.handlers.admins;

import com.lilangel.teamplay.exception.TeamNotFoundException;
import com.lilangel.teamplay.models.Team;
import com.lilangel.teamplay.service.TeamService;
import com.lilangel.teamplay.tgbot.handlers.AbstractHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class TeamHandler extends AbstractHandler {
    /**
     * Справка
     */
    private final String HELP_MESSAGE;

    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE;
    /**
     * Количество аргументов, требуемое для создания команды
     */
    private final Integer ARGS_COUNT_TO_CREATE;
    private final TeamService teamService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public TeamHandler(TeamService teamService) {
        this.teamService = teamService;
        handlers.put("help", this::help);
        handlers.put("get_all", this::getAll);
        handlers.put("create", this::create);
        handlers.put("get_by_id", this::getById);
        handlers.put("delete_by_id", this::deleteById);
        HELP_MESSAGE = """
                /team Help:
                    `/team help` - print this message
                    `/team get_all` - get all teams
                    `/team get_by_id id={id}` - get team by id
                    `/team create name={name} lead_id={lead_id}` - create new team
                    `/team delete_by_id id={id}` - delete team""";
        WRONG_COMMAND_MESSAGE = "Wrong command, try `/team help` to get available commands";
        ARGS_COUNT_TO_CREATE = 2;
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/team"
     *
     * @param command команда
     * @param args    аргументы команды
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
    protected String getAll(Map<String, String> args) {
        String template = """
                \t\t\t\tID: %d
                \t\t\t\tName: %s
                \t\t\t\tLead ID: %d
                                
                """;
        StringBuilder response = new StringBuilder("Teams:\n");
        List<Team> teams = teamService.getAll();
        for (Team t : teams) {
            response.append(String.format(template, t.getId(), t.getName(), t.getLeadId()));
        }
        return response.toString();
    }

    /**
     * Возвращает информацию о команде по его идентификатору
     *
     * @param args аргументы
     * @return строка с информацией о команде
     */
    protected String getById(Map<String, String> args) {
        String template = """
                Team:
                    ID: %d
                    Name: %s
                    Lead ID: %d""";
        Team team;
        try {
            team = teamService.getById(Integer.parseInt(args.get("id")));
        } catch (TeamNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, team.getId(), team.getName(), team.getLeadId());
    }

    /**
     * Создает новую команду
     *
     * @param args аргументы
     * @return строка с идентификатором созданной команды
     */
    protected String create(Map<String, String> args) {
        if (args.size() != ARGS_COUNT_TO_CREATE) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew team ID: %s";
        String createdId = teamService.create(
                        args.get("name"),
                        Integer.parseInt(args.get("lead_id")))
                .toString();
        if (Integer.parseInt(createdId) == -1) {
            return "There is no employer with this id. Check the list of employers using `/employer get_all` and try again";
        }
        return String.format(template, createdId);
    }

    /**
     * Удаляет команду по идентификатору
     *
     * @param args аргументы
     * @return строка с результатом
     */
    protected String deleteById(Map<String, String> args) {
        try {
            teamService.deleteById(Integer.parseInt(args.get("id")));
        } catch (TeamNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
