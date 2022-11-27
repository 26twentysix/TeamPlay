package com.lilangel.teamplay.tgbot;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс юнит-тестов для {@link Bot}
 */
@SpringBootTest
public class BotTest {

    private final Bot bot;

    @Autowired
    public BotTest(Bot bot) {
        this.bot = bot;
    }

    /**
     * Тестирует метод {@link Bot#messageHandler(String)}
     * с сообщением, содержащим команду "/help"
     * Метод проходит проверку, если возвращает ответ, начинающийся с "Bot Help"
     */
    @Test
    public void correctCommandTest() {
        String response = bot.messageHandler("/help");
        assertNotNull(response);
        assertTrue(response.startsWith("Bot Help"));

    }

    /**
     * Тестирует метод {@link Bot#messageHandler(String)}
     * с сообщением, содержащим несуществующую команду "/thisCommandDoesntExist"
     * Метод проходит проверку, если возвращает ответ, начинающийся с "Wrong command"
     */
    @Test
    public void wrongCommandTest() {
        String response = bot.messageHandler("/thisCommandDoesntExist");
        assertNotNull(response);
        assertTrue(response.startsWith("Wrong command"));
    }

    /**
     * Тестирует корректную инициализацию бота
     * Тест проходится, если методы {@link Bot#getBotToken()} и {@link Bot#getBotUsername()} возвращают ненулевую строку
     */
    @Test
    public void correctInitTest() {
        assertNotNull(bot.getBotToken());
        assertNotNull(bot.getBotUsername());
    }

    @Test
    public void argsParseTest() {
        Map<String, String> expected = new HashMap<>();
        expected.put("name", "John Doe");
        expected.put("email", "johndoe@test.com");
        Map<String, String> actual = Bot.parseArgs("/command register name=John Doe email=johndoe@test.com");
        assertEquals(expected, actual);
    }
}
