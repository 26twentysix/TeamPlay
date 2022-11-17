package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.TeamNotFoundException;
import com.lilangel.teamplay.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class TeamHandler extends AbstractHandler{
    /**
     * Справка
     */
    private final String HELP_MESSAGE = """
            /team Help:
                `/team help` - print this message""";

    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try `/team help` to get available commands";

    private final TeamService teamService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public TeamHandler(TeamService teamService) {
        this.teamService = teamService;
        handlers.put("help", this::helpMessage);
        handlers.put("getAll", this::getAll);
        handlers.put("create", this::create);
        handlers.put("getById", this::getById);
        handlers.put("deleteById", this::deleteById);
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/team"
     *
     * @param request строка сообщения
     * @param args аргументы команды
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
    private String getById(Map<String, String> args) {
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
        return String.format(template, tean.getId(), team.getName(), team.getLeadId());
    }

    /**
     * Создает новую команду
     *
     * @param args аргументы
     * @return строка с идентификатором созданной команды
     */
    private String create(Map<String, String> args) {
        if (args.size() != 2) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew team ID: %s";
        String createdId = teamService.saveNewTeam(
                        args.get("name"),
                        Integer.parseInt(args.get("lead_id")))
                .toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет команду по идентификатору
     *
     * @param args аргументы
     * @return строка с результатом
     */

    private String deleteById(Map<String, String> args) {
        try {
            teamService.deleteById(Integer.parseInt(args.get("id")));
        } catch (TeamNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
