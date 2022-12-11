package com.lilangel.teamplay.tgbot.handlers.admins;

import com.lilangel.teamplay.tgbot.handlers.AbstractHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserHandler extends AbstractHandler {
    //TODO вообще ничего не написано...
    @Override
    public String requestHandler(String message, Map<String, String> args) {
        return null;
    }

    @Override
    protected String getHelpMessage() {
        return null;
    }

    @Override
    protected String getWrongCommandMessage() {
        return null;
    }

    @Override
    protected String help(Map<String, String> args) {
        return null;
    }
}
