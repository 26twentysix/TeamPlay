package com.lilangel.teamplay.tgbot.handlers.admins;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.service.EmployerService;
import com.lilangel.teamplay.tgbot.handlers.AbstractHandler;
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
    private final String HELP_MESSAGE;
    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE;
    /**
     * Количество аргументов, требуемое для создания работника
     */
    private final Integer ARGS_COUNT_TO_CREATE;
    private final EmployerService employerService;

    /**
     * Словарь, хранящий обработчики различных команд
     */
    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public EmployerHandler(EmployerService employerService) {
        this.employerService = employerService;
        handlers.put("help", this::help);
        handlers.put("get_all", this::getAll);
        handlers.put("create", this::create);
        handlers.put("get_by_id", this::getById);
        handlers.put("delete_by_id", this::deleteById);
        WRONG_COMMAND_MESSAGE = "Wrong command, try `/employer help` to get available commands";
        ARGS_COUNT_TO_CREATE = 3;
        HELP_MESSAGE = """
                /employer Help:
                    `/employer help` - print this message
                    `/employer get_all` - get all employers
                    `/employer get_by_id id={id}` - get employer by id
                    `/employer create name={name} email={email} team_id={team_id}` - create new employer
                    `/employer delete_by_id id={id}` - delete employer""";
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/employer"
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

    /**
     * @return сообщение с справкой
     */
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
     * Возвращает информацию о всех сотрудниках
     *
     * @param args список аргументов
     * @return строка с информацией о всех сотрудниках
     */
    protected String getAll(Map<String, String> args) {
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
    protected String getById(Map<String, String> args) {
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
    protected String create(Map<String, String> args) {
        if (args.size() != ARGS_COUNT_TO_CREATE) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew employer ID: %s";
        String createdId = employerService.create(
                        args.get("name"),
                        args.get("email"),
                        Integer.parseInt(args.get("team_id")))
                .toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет сотрудника по идентификатору
     *
     * @param args список аргументов
     * @return строка с результатом
     */
    protected String deleteById(Map<String, String> args) {
        try {
            employerService.deleteById(Integer.parseInt(args.get("id")));
        } catch (EmployerNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
