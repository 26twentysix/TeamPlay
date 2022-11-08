package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.function.Function;


/**
 * Обработчик сообщений, начинающихся с "/employer"
 */
@Controller
public class EmployerHandler extends AbstractHandler {

    /**
     * Справка
     */
    private final String HELP_MESSAGE = """
            /employer Help:
            /employer help - print this message
            /employer getAll - get all employers
            /employer getById {id} - get employer by id
            /employer create {name} {surname} {email} {teamid} - create new employer
            /employer deleteById {id} - delete employer""";
    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try /employer help to get available commands";
    private final EmployerService employerService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<List<String>, String>> handlers = new HashMap<>();

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
     * Возвращает информацию о всех сотрудниках
     *
     * @param args список аргументов
     * @return строка с информацией о всех сотрудниках
     */
    private String getAll(List<String> args) {
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
     * @param args список аргументов, args[0] - Integer id
     * @return строка с информацией о сотруднике
     */
    private String getById(List<String> args) {
        String template = """
                Employer:
                    ID: %d
                    Name: %s
                    E-mail: %s
                    Team ID: %d""";
        Employer employer;
        try {
            employer = employerService.getById(Integer.parseInt(args.get(0)));
        } catch (EmployerNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, employer.getId(), employer.getName(), employer.getEmail(), employer.getTeamId());
    }

    /**
     * Создает нового сотрудника
     *
     * @param args список аргументов, args[0] - имя, args[1] - фамилия, args[2] - e-mail, args[3] - team id
     * @return строка с идентификатором созданного сотрудника
     */
    private String create(List<String> args) {
        if (args.size() != 4) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew employer ID: %s";
        String createdId = employerService.saveNewEmployer(
                        args.get(0) + " " + args.get(1),
                        args.get(2),
                        Integer.parseInt(args.get(3)))
                .toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет сотрудника по идентификатору
     *
     * @param args список аргументов, args[0] - Integer id
     * @return строка с результатом
     */

    private String deleteById(List<String> args) {
        try {
            employerService.deleteById(Integer.parseInt(args.get(0)));
        } catch (EmployerNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
