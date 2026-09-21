package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void appGreetsJava() {
        App app = new App();
        assertEquals("Hello, Java!", app.getGreeting());
    }
}
