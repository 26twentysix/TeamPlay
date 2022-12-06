package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.TicketNotFoundException;
import com.lilangel.teamplay.models.Ticket;
import com.lilangel.teamplay.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class TicketHandler extends AbstractHandler {
    /**
     * Справка
     */
    private final String HELP_MESSAGE;

    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE;
    /**
     * Количество аргументов, требуемое для создания тикета
     */
    private final Integer ARGS_COUNT_TO_CREATE;
    private final TicketService ticketService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public TicketHandler(TicketService ticketService) {
        this.ticketService = ticketService;
        handlers.put("help", this::help);
        handlers.put("get_all", this::getAll);
        handlers.put("create", this::create);
        handlers.put("get_by_id", this::getById);
        handlers.put("delete_by_id", this::deleteById);
        HELP_MESSAGE = """
                /ticket Help:
                    `/ticket help` - print this message
                    `/ticket get_all` - get all tickets
                    `/ticket get_by_id id={id}` - get ticket by id
                    `/ticket create project_id={project_id} priority={priority} status={status} short_description={short_description} full_description={full_description} employer_id={employer_id}` - create new employer
                    `/ticket delete_by_id id={id}` - delete ticket""";
        WRONG_COMMAND_MESSAGE = "Wrong command, try `/ticket help` to get available commands";
        ARGS_COUNT_TO_CREATE = 6;
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/ticket"
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
     * Возвращает информацию о всех тикетах
     *
     * @param args список аргументов
     * @return строка с информацией о всех тикетах
     */
    @Override
    protected String getAll(Map<String, String> args) {
        String template = """
                \t\t\t\tID: %d
                \t\t\t\tProject ID: %d
                \t\t\t\tImportance: %s
                \t\t\t\tStatus: %s
                \t\t\t\tShort Description: %s
                \t\t\t\tFull Description: %s
                \t\t\t\tEmployer ID: %d
                                
                """;
        StringBuilder response = new StringBuilder("Tickets:\n");
        List<Ticket> tickets = ticketService.getAll();
        for (Ticket t : tickets) {
            response.append(String.format(template, t.getId(), t.getProjectId(), t.getPriority(), t.getStatus(), t.getShortDescription(), t.getFullDescription(), t.getEmployerId()));
        }
        return response.toString();
    }

    /**
     * Возвращает информацию о тикету по eго идентификатору
     *
     * @param args список аргументов
     * @return строка с информацией о тикете
     */
    @Override
    protected String getById(Map<String, String> args) {
        String template = """
                Ticket:
                    ID: %d
                    Project ID: %d
                    Importance: %s
                    Status: %s
                    Short Description: %s
                    Full Description: %s
                    Employer ID: %d""";
        Ticket ticket;
        try {
            ticket = ticketService.getById(Integer.parseInt(args.get("id")));
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, ticket.getId(), ticket.getProjectId(), ticket.getPriority(), ticket.getStatus(), ticket.getShortDescription(), ticket.getFullDescription(), ticket.getEmployerId());
    }

    /**
     * Создает новый тикет
     *
     * @param args список аргументов
     * @return строка с идентификатором созданного тикета
     */
    @Override
    protected String create(Map<String, String> args) {
        if (args.size() != ARGS_COUNT_TO_CREATE) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew ticket ID: %s";
        String createdId = ticketService.create(Integer.parseInt(args.get("project_id")), args.get("priority"), args.get("status"), args.get("short_description"), args.get("full_description"), Integer.parseInt(args.get("employer_id"))).toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет тикет по идентификатору
     *
     * @param args список аргументов
     * @return строка с результатом
     */
    @Override
    protected String deleteById(Map<String, String> args) {
        try {
            ticketService.deleteById(Integer.parseInt(args.get("id")));
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
