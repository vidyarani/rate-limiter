package com.ratelimiter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void appGreetsJava() {
        App app = new App();
        assertEquals("Hello, Java!", app.getGreeting());
    }
}
