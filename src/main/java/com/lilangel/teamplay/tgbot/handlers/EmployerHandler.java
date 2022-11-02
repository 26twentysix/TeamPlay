package com.lilangel.teamplay.tgbot.handlers;

import com.lilangel.teamplay.service.EmployerService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.function.Function;


@Controller
public class EmployerHandler extends AbstractHandler {

    //TODO Написать справку
    private final String HELP_MESSAGE = "This is employer help message";

    private final String WRONG_COMMAND_MESSAGE = "Wrong command, try /employer help to get available commands";
    private final EmployerService employerService;

    private final Map<String, Function<List<String>, String>> handlers = new HashMap<>();

    @Autowired
    public EmployerHandler(EmployerService employerService) {
        this.employerService = employerService;
        handlers.put("help", this::helpMessage);
        handlers.put("getAll", this::getAll);
        handlers.put("create", this::create);
        handlers.put("getById", this::getById);
        handlers.put("deleteById", this::deleteById);
    }

    @Override
    public String messageHandler(String message) {
        String command;
        List<String> args = new ArrayList<>();
        int indexOfSpace = message.indexOf(" ");
        if (indexOfSpace == -1) {
            command = "help";
        } else {
            var parsed = message.split(" ");
            command = parsed[1];
            args.addAll(Arrays.asList(parsed).subList(2, parsed.length));
        }
        if (handlers.containsKey(command)) {
            return handlers.get(command).apply(args);
        }
        return WRONG_COMMAND_MESSAGE;
    }

    private String helpMessage(List<String> args) {
        return HELP_MESSAGE;
    }

    private String getAll(List<String> args) {
        //TODO Отформатировать вывод
        return employerService.getAll().toString();
    }

    @SneakyThrows
    private String getById(List<String> args) {
        //TODO Отформатировать вывод
        return employerService.getById(Integer.parseInt(args.get(0))).toString();
    }

    private String create(List<String> args) {
        //TODO Отформатировать вывод
        return employerService.saveNewEmployer(args.get(0),
                args.get(1) + " " + args.get(2),
                Integer.parseInt(args.get(3)))
                .toString();
    }

    @SneakyThrows
    private String deleteById(List<String> args) {
        employerService.deleteById(Integer.parseInt(args.get(0)));
        return "Successfully deleted";
    }
}
