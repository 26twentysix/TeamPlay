package com.lilangel.teamplay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTests {

    @Test
    public void testMethodFailInit() {
        assertFalse(App.testMethod(false));
    }

    @Test
    public void testMethodCorrectInit() {
        assertTrue(App.testMethod(true));
    }
}
