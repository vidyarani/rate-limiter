package com.ratelimiter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class RateLimiterTest {
    private final Clock clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);

    @Test
    void allowsThreeRequestsForOneClient() {
        RateLimiter rateLimiter = new RateLimiter(clock);

        assertTrue(rateLimiter.isAllowed("client-1"));
        assertTrue(rateLimiter.isAllowed("client-1"));
        assertTrue(rateLimiter.isAllowed("client-1"));
    }

    @Test
    void rejectsTheFourthRequestForOneClient() {
        RateLimiter rateLimiter = new RateLimiter(clock);

        rateLimiter.isAllowed("client-1");
        rateLimiter.isAllowed("client-1");
        rateLimiter.isAllowed("client-1");

        assertFalse(rateLimiter.isAllowed("client-1"));
    }

    @Test
    void tracksClientsIndependently() {
        RateLimiter rateLimiter = new RateLimiter(clock);

        rateLimiter.isAllowed("client-1");
        rateLimiter.isAllowed("client-1");
        rateLimiter.isAllowed("client-1");

        assertTrue(rateLimiter.isAllowed("client-2"));
    }
}
