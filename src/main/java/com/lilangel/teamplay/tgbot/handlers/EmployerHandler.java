package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;


/**
 * Обработчик сообщений, начинающихся с "/employer"
 */
@Component
public class EmployerHandler extends AbstractHandler {

    /**
     * Справка
     */
    private final String HELP_MESSAGE = """
            /employer Help:
                `/employer help` - print this message
                `/employer getAll` - get all employers
                `/employer getById id={id}` - get employer by id
                `/employer create name={name} email={email} teamid={teamid}` - create new employer
                `/employer deleteById id={id}` - delete employer""";
    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try `/employer help` to get available commands";
    private final EmployerService employerService;

    /**
     * Словарь, хранящий обработчики различных команд
     */
    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public EmployerHandler(EmployerService employerService) {
        this.employerService = employerService;
        handlers.put("help", this::helpMessage);
        handlers.put("getAll", this::getAll);
        handlers.put("create", this::create);
        handlers.put("getById", this::getById);
        handlers.put("deleteById", this::deleteById);
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/employer"
     *
     * @param request строка сообщения
     * @param args
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
     * @param args список аргументов
     * @return строка справки
     */
    private String helpMessage(Map<String, String> args) {
        return HELP_MESSAGE;
    }

    /**
     * Возвращает информацию о всех сотрудниках
     *
     * @param args список аргументов
     * @return строка с информацией о всех сотрудниках
     */
    private String getAll(Map<String, String> args) {
        String template = """
                \t\t\t\tID: %d
                \t\t\t\tName: %s
                \t\t\t\tE-mail: %s
                \t\t\t\tTeam ID: %d
                
                """;
        StringBuilder response = new StringBuilder("Employers:\n");
        List<Employer> employers = employerService.getAll();
        for (Employer e : employers) {
            response.append(String.format(template, e.getId(), e.getName(), e.getEmail(), e.getTeamId()));
        }
        return response.toString();
    }

    /**
     * Возвращает информацию о сотруднике по его идентификатору
     *
     * @param args список аргументов
     * @return строка с информацией о сотруднике
     */
    private String getById(Map<String, String> args) {
        String template = """
                Employer:
                    ID: %d
                    Name: %s
                    E-mail: %s
                    Team ID: %d""";
        Employer employer;
        try {
            employer = employerService.getById(Integer.parseInt(args.get("id")));
        } catch (EmployerNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, employer.getId(), employer.getName(), employer.getEmail(), employer.getTeamId());
    }

    /**
     * Создает нового сотрудника
     *
     * @param args список аргументов
     * @return строка с идентификатором созданного сотрудника
     */
    private String create(Map<String, String> args) {
        if (args.size() != 3) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew employer ID: %s";
        String createdId = employerService.saveNewEmployer(
                        args.get("name"),
                        args.get("email"),
                        Integer.parseInt(args.get("teamid")))
                .toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет сотрудника по идентификатору
     *
     * @param args список аргументов
     * @return строка с результатом
     */

    private String deleteById(Map<String, String> args) {
        try {
            employerService.deleteById(Integer.parseInt(args.get("id")));
        } catch (EmployerNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
