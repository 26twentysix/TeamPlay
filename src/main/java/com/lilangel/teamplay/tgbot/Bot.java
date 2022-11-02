package com.lilangel.teamplay.tgbot;

import com.lilangel.teamplay.tgbot.handlers.AbstractHandler;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

@Component
public class Bot extends TelegramLongPollingBot {

    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try /help to get bot available commands";

    //TODO Написать справку
    private final String HELP_MESSAGE = "This is bot help message";
    private ListableBeanFactory listableBeanFactory;

    Map<String, Object> controllers;

    @Autowired
    public Bot(ListableBeanFactory listableBeanFactory) {
        this.listableBeanFactory = listableBeanFactory;
        controllers = listableBeanFactory.getBeansWithAnnotation(Controller.class);
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
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String messageHandler(String message) {
        if (Objects.equals(message, "/help")) {
            return HELP_MESSAGE;
        }
        String command = "";
        if (message.startsWith("/")) {
            int indexOfSpace = message.indexOf(" ");
            if (indexOfSpace == -1) {
                command = message;
            }
            else {
                var parsed = message.split(" ");
                command = parsed[0];
                }
            }
        if (controllers.containsKey(command.substring(1) + "Handler")) {
            AbstractHandler handler = (AbstractHandler) controllers.get(command.substring(1) + "Handler");
            return handler.messageHandler(message);
        } else {
            return WRONG_COMMAND_MESSAGE;
        }
    }

    @PostConstruct
    public void registerBot() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }
}
