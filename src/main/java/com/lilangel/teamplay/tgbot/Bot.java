package com.lilangel.teamplay.tgbot;

import com.lilangel.teamplay.models.User;
import com.lilangel.teamplay.service.impl.AuthServiceImpl;
import com.lilangel.teamplay.service.impl.EmployerServiceImpl;
import com.lilangel.teamplay.service.impl.UserServiceImpl;
import com.lilangel.teamplay.tgbot.handlers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class Bot extends TelegramLongPollingBot {
    Map<String, AbstractHandler> handlers = new HashMap<>();

    private final UserServiceImpl userService;

    private final AuthServiceImpl authService;

    private final EmployerServiceImpl employerService;

    @Autowired
    public Bot(AdminHandler adminHandler, DefaultUserHandler defaultUserHandler, UserServiceImpl userService, AuthServiceImpl authService, EmployerServiceImpl employerService) {
        handlers.put("adminHandler", adminHandler);
        handlers.put("defaultUserHandler", defaultUserHandler);
        this.userService = userService;
        this.authService = authService;
        this.employerService = employerService;
    }

    @Value("${bot.username}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(messageHandler(update.getMessage().getText(), update.getMessage().getFrom().getId()));
            message.setParseMode("Markdown");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Базовый обработчик сообщения
     *
     * @param message строка сообщения
     * @return строка ответа
     */
    public String messageHandler(String message, Long tgId) {
        if (userService.isAuthorized(tgId)) {
            User currentUser = userService.getByTgId(tgId);
            AbstractHandler handler;
            if (currentUser.getIsAdmin()) {
                handler = handlers.get("adminHandler");
            } else {
                handler = handlers.get("defaultUserHandler");
            }
            return handler.requestHandler(message, parseArgs(message));
        } else if (message.startsWith("/auth")) {
            Map<String, String> args = parseArgs(message);
            return auth(tgId, args.get("name"), args.get("email"), Integer.parseInt(args.get("team_id")), args.get("password"));
        } else {
            return """
                    You need to auth using
                    `/auth name={name} email={email} team_id={team_id} password={password}`""";
        }
    }

    public String auth(Long tgId, String name, String email, Integer teamId, String password) {
        if (authService.isValid(password)) {
            Integer employerId = employerService.create(name, email, teamId);
            boolean isAdmin = password.length() == 15;
            userService.create(tgId, employerId, isAdmin);
            return "Successfully authenticated";
        } else {
            return "Invalid password. Try again or ask for new password";
        }
    }

    /**
     * Извлекает из сообщения аргументы команды
     *
     * @param message сообщение, из которого нужно извлечь аргументы команды
     * @return Словарь вида paramName : paramValue
     */
    public Map<String, String> parseArgs(String message) {
        Map<String, String> args = new HashMap<>();
        List<String> splittedArgs = (Arrays.stream(message.split("=")).toList());
        for (int i = 0; i < splittedArgs.size() - 1; i++) {
            String argName = splittedArgs.get(i).substring(splittedArgs.get(i).lastIndexOf(" ") + 1);
            String arg;
            if (i != splittedArgs.size() - 2) {
                arg = splittedArgs.get(i + 1).substring(0, splittedArgs.get(i + 1).lastIndexOf(" "));
            } else {
                arg = splittedArgs.get(i + 1);
            }
            args.put(argName, arg);
        }
        return args;
    }

    /**
     * Извлекает команду из сообщения
     *
     * @param message сообщение
     * @return команда
     */
    public String getCommand(String message) {
        int indexOfSpace = message.indexOf(" ");
        if (indexOfSpace == -1) {
            return "help";
        } else {
            var parsed = message.split(" ");
            return parsed[1];
        }
    }

    @PostConstruct
    public void registerBot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }
}
