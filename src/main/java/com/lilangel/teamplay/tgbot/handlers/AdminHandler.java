package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.tgbot.handlers.admins.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AdminHandler extends AbstractHandler {

    private final String HELP_MESSAGE;

    private final String WRONG_COMMAND_MESSAGE;

    private final Map<String, AbstractHandler> handlers = new HashMap<>();

    private final Set<String> entities = new HashSet<>();

    @Autowired
    public AdminHandler(EmployerHandler employerHandler, ProjectHandler projectHandler, TeamHandler teamHandler,
                        TicketHandler ticketHandler, UserHandler userHandler, DefaultUserHandler defaultUserHandler) {
        handlers.put("employerHandler", employerHandler);
        handlers.put("projectHandler", projectHandler);
        handlers.put("teamHandler", teamHandler);
        handlers.put("ticketHandler", ticketHandler);
        handlers.put("userHandler", userHandler);
        handlers.put("defaultUserHandler", defaultUserHandler);
        entities.add("user");
        entities.add("team");
        entities.add("ticket");
        entities.add("project");
        entities.add("employer");
        HELP_MESSAGE = """
                admin help message""";
        WRONG_COMMAND_MESSAGE = "Admin wrong command message";
    }

    @Override
    public String requestHandler(String message, Map<String, String> args) {
        if (Objects.equals(message, "/help")) {
            return HELP_MESSAGE;
        }
        String command;
        if (message.startsWith("/")) {
            command = extractFirstWord(message);
            AbstractHandler handler;
            if (entities.contains(command)) {
                handler = handlers.get(command + "Handler");
                return handler.requestHandler(getSubCommand(message), args);
            } else {
                handler = handlers.get("defaultUserHandler");
                return handler.requestHandler(command, args);
            }
        }
        return WRONG_COMMAND_MESSAGE;
    }

    private String getSubCommand(String message) {
        var splittedString = message.split(" ");
        if (splittedString.length > 1) {
            return splittedString[1];
        }
        return "help";
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

    private String extractFirstWord(String message) {
        int indexOfSpace = message.indexOf(" ");
        if (indexOfSpace == -1) {
            return message.substring(1);
        } else {
            return message.substring(1, indexOfSpace);
        }
    }

}
