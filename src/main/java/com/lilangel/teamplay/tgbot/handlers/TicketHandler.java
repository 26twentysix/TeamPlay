package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.TicketNotFoundException;
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
                `/ticket help` - print this message""";

    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try `/ticket help` to get available commands";

    private final TicketService ticketService;

    /**
     * Словарь, хранящий обработчики различных комманд
     */
    private final Map<String, Function<List<String>, String>> handlers = new HashMap<>();

    @Autowired
    public TicketHandler(TicketHandler ticketHandler) {
        this.ticketService = ticketHandler;
        handlers.put("help", this::helpMessage);
        handlers.put("getAll", this::getAll);
        handlers.put("create", this::create);
        handlers.put("getById", this::getById);
        handlers.put("deleteById", this::deleteById);
    }

    /**
     * Базовый обработчик для сообщений, начинающихся с "/ticket"
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
     * Возвращает информацию о всех тикетах
     *
     * @param args список аргументов
     * @return строка с информацией о всех тикетах
     */
    private String getAll(List<String> args) {
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
        List<Tickets> tickets = ticketService.getAll();
        for (Ticket t : tickets) {
            response.append(String.format(template, t.getId(), t.getProjectId(), t.getImportance(), t.getStatus(),
                    t.getShortDesctiption(), t.getFullDescription(), t.getEmployerId()));
        }
        return response.toString();
    }

    /**
     * Возвращает информацию о тикету по eго идентификатору
     *
     * @param args список аргументов, args[0] - Integer id
     * @return строка с информацией о тикете
     */
    private String getById(List<String> args) {
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
            ticket = ticketService.getById(Integer.parseInt(args.get(0)));
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, ticket.getId(), ticket.getProjectId(), ticket.getImportance(), ticket.getStatus(),
                ticket.getShortDesctiption(), ticket.getFullDescription(), ticket.getEmployerId());
    }

    /**
     * Создает новый тикет
     *
     * @param args список аргументов, args[0] - project id, args[1] - важность, args[2] - статус,
     *             args[3] - короткое описание, args[4] - полное описание
     * @return строка с идентификатором созданного тикета
     */
    private String create(List<String> args) {
        if (args.size() != 5) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew ticket ID: %s";
        String createdId = ticketService.saveNewTicket(
                        Integer.parseInt(args.get(0)),
                        args.get(1),
                        args.get(2),
                        args.get(3),
                        args.get(4))
                .toString();
        return String.format(template, createdId);
    }

    /**
     * Удаляет тикет по идентификатору
     *
     * @param args список аргументов, args[0] - Integer id
     * @return строка с результатом
     */

    private String deleteById(List<String> args) {
        try {
            ticketService.deleteById(Integer.parseInt(args.get(0)));
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
