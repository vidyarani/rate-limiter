package com.ratelimiter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class RateLimiterTest {
    private final MutableClock clock = new MutableClock();

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

    @Test
    void allowsARequestAfterTheWindowExpires() {
        RateLimiter rateLimiter = new RateLimiter(clock);

        rateLimiter.isAllowed("client-1");
        rateLimiter.isAllowed("client-1");
        rateLimiter.isAllowed("client-1");

        clock.advanceMillis(RateLimiter.WINDOW_MILLIS);

        assertTrue(rateLimiter.isAllowed("client-1"));
    }

    private static final class MutableClock extends Clock {
        private long currentMillis;

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return Instant.ofEpochMilli(currentMillis);
        }

        void advanceMillis(long millis) {
            currentMillis += millis;
        }
    }
}
