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
    private final Map<String, Function<List<String>, String>> handlers = new HashMap<>();

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
     * @param args список аргументов, args[0] - Integer id
     * @return строка с информацией о команде
     */
    private String getById(List<String> args) {
        String template = """
                Team:
                    ID: %d
                    Name: %s
                    Lead ID: %d""";
        Team team;
        try {
            team = teamService.getById(Integer.parseInt(args.get(0)));
        } catch (TeamNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, tean.getId(), team.getName(), team.getLeadId());
    }

    /**
     * Создает новую команду
     *
     * @param args список аргументов, args[0] - имя, args[1] - lead id
     * @return строка с идентификатором созданной команды
     */
    private String create(List<String> args) {
        if (args.size() != 2) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew team ID: %s";
        String createdId = teamService.saveNewTeam(
                        args.get(0),
                        Integer.parseInt(args.get(1)))
                .toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет команду по идентификатору
     *
     * @param args список аргументов, args[0] - Integer id
     * @return строка с результатом
     */

    private String deleteById(List<String> args) {
        try {
            teamService.deleteById(Integer.parseInt(args.get(0)));
        } catch (TeamNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
