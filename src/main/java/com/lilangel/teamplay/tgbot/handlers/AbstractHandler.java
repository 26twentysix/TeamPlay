package com.lilangel.teamplay.tgbot.handlers;

import java.util.Map;

public abstract class AbstractHandler {
    public abstract String requestHandler(String message, Map<String, String> args);
}
