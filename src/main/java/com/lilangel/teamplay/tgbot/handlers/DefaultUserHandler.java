package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.TicketNotFoundException;
import com.lilangel.teamplay.models.Ticket;
import com.lilangel.teamplay.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class DefaultUserHandler extends AbstractHandler {

    private final String HELP_MESSAGE;

    private final String WRONG_COMMAND_MESSAGE;

    private Integer ARGS_COUNT_TO_CREATE;

    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    private final TicketService ticketService;

    @Autowired
    public DefaultUserHandler(TicketService ticketService) {
        handlers.put("help", this::help);
        this.ticketService = ticketService;
        WRONG_COMMAND_MESSAGE = "Default user wrong command msg";
        handlers.put("help", this::help);
        handlers.put("create_ticket", this::createTicket);
        handlers.put("view_tickets", this::viewTickets);
        handlers.put("change_ticket_info", this::changeTicketInfo);
        handlers.put("pick_up_ticket", this::pickUpTicket);
        HELP_MESSAGE = """
                /user Help:
                    `/user help` - print this message
                    `/user change_info ticket_Id={ticket_Id} args = {args}` - change info by ticket_id
                    `/user view_tickets` - view all tickets
                    `/user create ticket create project_id={project_id} priority={priority} status={status} short_description={short_description} full_description={full_description} employer_id={employer_id}` - create new ticket
                    `/user pick_up_ticket ticket_id={ticket_id}` - pick up ticket
                    """;
        ARGS_COUNT_TO_CREATE = 6;
    }

    @Override
    public String requestHandler(String command, Map<String, String> args) {
        if (handlers.containsKey(command)) {
            return handlers.get(command).apply(args);
        }
        return getWrongCommandMessage();
    }

    protected String createTicket(Map<String, String> args) {
        if (args.size() != ARGS_COUNT_TO_CREATE) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew ticket ID: %s";
        String createdId = ticketService.create(Integer.parseInt(args.get("project_id")), args.get("priority"), args.get("status"), args.get("short_description"), args.get("full_description"), Integer.parseInt(args.get("employer_id"))).toString();
        return String.format(template, createdId);
    }

    protected String viewTickets(Map<String, String> args) {
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

    protected String changeTicketInfo(Map<String, String> args) {
        String template = """
                ID: %d
                Project ID: %d
                Importance: %s
                Status: %s
                Short Description: %s
                Full Description: %s
                Employer ID: %d
                                
                """;
        Ticket ticket;
        Map<String, String> newArgs = new HashMap<>();
        newArgs.put("full_description", args.get("full_description"));
        newArgs.put("priority", args.get("priority"));
        newArgs.put("short_description", args.get("short_description"));
        try {
            ticket = ticketService.getById(Integer.parseInt(args.get("id")));
            ticketService.updateTicketInfo(Integer.parseInt(args.get("id")), newArgs);
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, ticket.getId(), ticket.getProjectId(), ticket.getPriority(), ticket.getStatus(), ticket.getShortDescription(), ticket.getFullDescription(), ticket.getEmployerId());
    }

    protected String pickUpTicket(Map<String, String> args) {
        String template = " You took ticket with ID: %s";
        try {
            HashMap<String, String> newArgs = new HashMap<>();
            newArgs.put("status", "picked_up");
            newArgs.put("employer_id", args.get("tg_id"));
            ticketService.updateTicketInfo(Integer.parseInt(args.get("id")), newArgs);
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, args.get("id"));
    }

    @Override
    protected String getHelpMessage() {
        return HELP_MESSAGE;
    }

    @Override
    protected String getWrongCommandMessage() {
        return WRONG_COMMAND_MESSAGE;
    }

    @Override
    protected String help(Map<String, String> args) {
        return getHelpMessage();
    }
}
