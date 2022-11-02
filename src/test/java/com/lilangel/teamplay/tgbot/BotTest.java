package com.lilangel.teamplay.tgbot;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BotTest {

    private final Bot bot;

    @Autowired
    public BotTest(Bot bot) {
        this.bot = bot;
    }

    @Test
    public void correctCommandTest() {
        String response = bot.messageHandler("/help");
        assertNotNull(response);
        assertTrue(response.startsWith("Bot Help"));

    }

    @Test
    public void wrongCommandTest() {
        String response = bot.messageHandler("/thisCommandDoesntExist");
        assertNotNull(response);
        assertTrue(response.startsWith("Wrong command"));
    }

    @Test
    public void correctInitTest() {
        assertNotNull(bot.getBotToken());
        assertNotNull(bot.getBotUsername());
    }
}
