package com.lilangel.teamplay.tgbot;

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

    /**
     * Сообщение, если команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try `/help` to get bot available commands";

    /**
     * Справка
     */
    //TODO Написать справку
    private final String HELP_MESSAGE = """
            Bot Help:
                This is bot help message""";

    Map<String, AbstractHandler> handlers = new HashMap<>();

    @Autowired
    public Bot(EmployerHandler employerHandler, ProjectHandler projectHandler, TeamHandler teamHandler,
               TicketHandler ticketHandler, UserHandler userHandler) {
        handlers.put("employerHandler", employerHandler);
        handlers.put("projectHandler", projectHandler);
        handlers.put("teamHandler", teamHandler);
        handlers.put("ticketHandler", ticketHandler);
        handlers.put("userHandler", userHandler);
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
            message.setText(messageHandler(update.getMessage().getText()));
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
    public String messageHandler(String message) {
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
            if (handlers.containsKey(command.substring(1) + "Handler")) {
                AbstractHandler handler = handlers.get(command.substring(1) + "Handler");
                return handler.requestHandler(getCommand(message), parseArgs(message));
            }
        }
        return WRONG_COMMAND_MESSAGE;
    }

    /**
     * Извлекает из сообщения аргументы команды
     * @param message сообщение, из которого нужно извлечь аргументы команды
     * @return Словарь вида paramName : paramValue
     */
    public static Map<String, String> parseArgs(String message) {
        Map<String, String> args = new HashMap<>();
        List<String> splittedArgs = (Arrays.stream(message.split("=")).toList());
        for (int i = 0; i < splittedArgs.size() - 1; i++) {
            String argName = splittedArgs.get(i).substring(splittedArgs.get(i).lastIndexOf(" ") + 1);
            String arg;
            if (i != splittedArgs.size() - 2) {
                arg = splittedArgs.get(i+1).substring(0, splittedArgs.get(i+1).lastIndexOf(" "));
            }
            else {
                arg = splittedArgs.get(i+1);
            }
            args.put(argName, arg);
        }
        return args;
    }

    public static String getCommand(String message) {
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
