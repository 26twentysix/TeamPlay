package com.lilangel.teamplay.tgbot.handlers.admins;

import com.lilangel.teamplay.exception.UserNotFoundException;
import com.lilangel.teamplay.models.User;
import com.lilangel.teamplay.service.UserService;
import com.lilangel.teamplay.tgbot.handlers.AbstractHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class UserHandler extends AbstractHandler {
    /**
     * Справка
     */
    private final String HELP_MESSAGE;
    /**
     * Сообщение о том, что команда не существует
     */
    private final String WRONG_COMMAND_MESSAGE;
    /**
     * Количество аргументов, требуемое для создания команды
     */
    private final Integer ARGS_COUNT_TO_CREATE;

    private final UserService userService;

    private final Map<String, Function<Map<String, String>, String>> handlers = new HashMap<>();

    @Autowired
    public UserHandler(UserService userService) {
        this.userService = userService;
        handlers.put("help", this::help);
        handlers.put("create", this::create);
        handlers.put("get_all", this::getAll);
        handlers.put("get_by_id", this::getById);
        handlers.put("delete_by_id", this::deleteById);
        HELP_MESSAGE = """
                /user Help:
                    `/user help` - print this message
                    `/user get_all` - get all users
                    `/user get_by_id id={id}` - get user by id
                    `/user create tg_id={tg_id} employer_id={employer_id} is_admin={is_admin} - create new user
                    `/user delete_by_id id={id}` - delete user""";
        WRONG_COMMAND_MESSAGE = "Wrong command, try `/user help` to get available commands";
        ARGS_COUNT_TO_CREATE = 3;
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

    protected String create(Map<String, String> args) {
        if (args.size() != ARGS_COUNT_TO_CREATE) {
            return "Wrong args number";
        }
        String template = "Successfully created\nNew user ID: %s";
        String createdId = userService.create(Long.valueOf(args.get("tg_id")),
                        Integer.parseInt(args.get("employer_id")),
                        Boolean.valueOf(args.get("is_admin")))
                .toString();
        return String.format(template, createdId);
    }

    protected String getAll(Map<String, String> args) {
        String template = """
                \t\t\t\tID: %d
                \t\t\t\tTg ID: %d
                \t\t\t\tEmployer ID: %d
                \t\t\t\tIs admin: %b
                                
                """;
        StringBuilder response = new StringBuilder("Users:\n");
        List<User> users = userService.getAll();
        for (User u : users) {
            response.append(String.format(template, u.getId(), u.getTgId(), u.getEmployerId(), u.getIsAdmin()));
        }
        return response.toString();
    }

    protected String getById(Map<String, String> args) {
        String template = """
                Project:
                    ID: %d
                    TG ID: %d
                    Employer ID: %d
                    Is admin: %b""";
        User user;
        try {
            user = userService.getById(Integer.parseInt(args.get("id")));
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }
        return String.format(template, user.getId(), user.getTgId(), user.getEmployerId(), user.getIsAdmin());
    }

    protected String deleteById(Map<String, String> args) {
        try {
            userService.deleteById(Integer.parseInt(args.get("id")));
        } catch (UserNotFoundException e) {
            return e.getMessage();
        }
        return "Successfully deleted";
    }
}
