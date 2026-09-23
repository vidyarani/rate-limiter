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
    private final int limit;
    private final long windowMillis;
    private final Map<String, Deque<Long>> timestampsByClient = new HashMap<>();

    public RateLimiter(Clock clock) {
        this(clock, LIMIT, WINDOW_MILLIS);
    }

    public RateLimiter(Clock clock, int limit, long windowMillis) {
        this.clock = clock;
        this.limit = limit;
        this.windowMillis = windowMillis;
    }

    public boolean isAllowed(String clientId) {
        Deque<Long> timestamps = timestampsByClient.computeIfAbsent(clientId, key -> new ArrayDeque<>());
        long currentTime = clock.millis();
        long windowStart = currentTime - windowMillis;
        while (!timestamps.isEmpty() && timestamps.peekFirst() <= windowStart) {
            timestamps.removeFirst();
        }

        if (timestamps.size() >= limit) {
            return false;
        }

        timestamps.addLast(currentTime);
        return true;
    }
}
