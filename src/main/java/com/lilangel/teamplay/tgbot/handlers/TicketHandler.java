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
    private final String HELP_MESSAGE = """
            /ticket Help:
                `/ticket help` - print this message
                `/ticket get_all` - get all employers
                `/ticket get_by_id id={id}` - get employer by id
                `/ticket create project_id={project_id} priority={priority} status={status} short_description={short_description} full_description={full_description} employer_id={employer_id}` - create new employer
                `/ticket delete_by_id id={id}` - delete employer""";

    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try `/ticket help` to get available commands";

    private final TicketService ticketService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public TicketHandler(TicketService ticketService) {
        this.ticketService = ticketService;
        handlers.put("help", this::helpMessage);
        handlers.put("get_all", this::getAll);
        handlers.put("create", this::create);
        handlers.put("get_by_id", this::getById);
        handlers.put("delete_by_id", this::deleteById);
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/ticket"
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
     * Возвращает информацию о всех тикетах
     *
     * @param args список аргументов
     * @return строка с информацией о всех тикетах
     */
    private String getAll(Map<String, String> args) {
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
    private String getById(Map<String, String> args) {
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
    private String create(Map<String, String> args) {
        if (args.size() != 6) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew ticket ID: %s";
        String createdId = ticketService.saveNewTicket(Integer.parseInt(args.get("project_id")), args.get("priority"), args.get("status"), args.get("short_description"), args.get("full_description"), Integer.parseInt(args.get("employer_id"))).toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет тикет по идентификатору
     *
     * @param args список аргументов
     * @return строка с результатом
     */

    private String deleteById(Map<String, String> args) {
        try {
            ticketService.deleteById(Integer.parseInt(args.get("id")));
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
