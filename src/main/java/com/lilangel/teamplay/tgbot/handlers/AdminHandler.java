package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.tgbot.handlers.admins.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class AdminHandler extends AbstractHandler {

    private final String HELP_MESSAGE;

    private final String WRONG_COMMAND_MESSAGE;

    private final Map<String, AbstractHandler> handlers = new HashMap<>();

    @Autowired
    public AdminHandler(EmployerHandler employerHandler, ProjectHandler projectHandler, TeamHandler teamHandler,
                        TicketHandler ticketHandler, UserHandler userHandler, DefaultUserHandler defaultUserHandler) {
        handlers.put("employerHandler", employerHandler);
        handlers.put("projectHandler", projectHandler);
        handlers.put("teamHandler", teamHandler);
        handlers.put("ticketHandler", ticketHandler);
        handlers.put("userHandler", userHandler);
        handlers.put("defaultUserHandler", defaultUserHandler);
        HELP_MESSAGE = "Admin help message";
        WRONG_COMMAND_MESSAGE = "Admin wrong command message";
    }

    @Override
    public String requestHandler(String message, Map<String, String> args) {
        if (Objects.equals(message, "/help")) {
            return HELP_MESSAGE;
        }
        String command;
        if (message.startsWith("/")) {
            int indexOfSpace = message.indexOf(" ");
            if (indexOfSpace == -1) {
                command = message;
            } else {
                var parsed = message.split(" ");
                command = parsed[0];
            }
            AbstractHandler handler;
            if (handlers.containsKey(command.substring(1) + "Handler")) {
                handler = handlers.get(command.substring(1) + "Handler");
                return handler.requestHandler(getSubCommand(message), args);
            } else {
                handler = handlers.get("defaultUserHandler");
                return handler.requestHandler(command.substring(1), args);
            }
        }
        return WRONG_COMMAND_MESSAGE;
    }

    private String getSubCommand(String message) {
        var splittedString = message.split(" ");
        if (splittedString.length > 1) {
            return splittedString[2];
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
}
