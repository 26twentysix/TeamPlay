package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.tgbot.handlers.admins.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DefaultUserHandler extends AbstractHandler {
    //TODO написать обработчики для команд для обычного юзера
    private final String HELP_MESSAGE;

    private final String WRONG_COMMAND_MESSAGE;

    private final UserHandler userHandler;

    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public DefaultUserHandler(UserHandler userHandler) {
        handlers.put("help", this::help);
        this.userHandler = userHandler;
        WRONG_COMMAND_MESSAGE = "Default user wrong command msg";
        HELP_MESSAGE = "Help msg";
    }

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

    @Override
    protected String getWrongCommandMessage() {
        return WRONG_COMMAND_MESSAGE;
    }

    @Override
    protected String help(Map<String, String> args) {
        return getHelpMessage();
    }
}
