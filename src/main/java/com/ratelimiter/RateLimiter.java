package com.ratelimiter;

import java.time.Clock;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class RateLimiter {
    static final int LIMIT = 3;
    static final long WINDOW_MILLIS = 10_000L;

    private final Clock clock;
    private final Map<String, Deque<Long>> timestampsByClient = new HashMap<>();

    public RateLimiter(Clock clock) {
        this.clock = clock;
    }

    public boolean isAllowed(String clientId) {
        Deque<Long> timestamps = timestampsByClient.computeIfAbsent(clientId, key -> new ArrayDeque<>());
        if (timestamps.size() == LIMIT) {
            return false;
        }

        timestamps.addLast(clock.millis());
        return true;
    }
}
