package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;

import java.util.List;
import java.util.Map;

public abstract class AbstractHandler {
    public abstract String requestHandler(String message, Map<String, String> args);

    protected abstract String getHelpMessage();

    protected abstract String getWrongCommandMessage();

    protected abstract String help(Map<String, String> args);

    protected abstract String getAll(Map<String, String> args);

    protected abstract String getById(Map<String, String> args);

    protected abstract String create(Map<String, String> args);

    protected abstract String deleteById(Map<String, String> args);
}
