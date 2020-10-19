package com.maltego.helpers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import java.util.LinkedList;
import java.util.Queue;

public class RateLimiter {

    private final Queue<Long> log = new LinkedList<>();

    private long maxRequestPerSec;

    public static RateLimiter from(int requests, long delay, TimeUnit delayUnit) {
        return new RateLimiter(requests / delayUnit.toSeconds(delay));
    }

    private RateLimiter(long maxRequestPerSec) {
        this.maxRequestPerSec = maxRequestPerSec;
    }

    public synchronized boolean tryAcquire() {
        //clear outdated timestamp logs
        while (!log.isEmpty() && log.element() <= System.currentTimeMillis() - 1000) {
            log.poll();
        }
        //put request even discarded, push client to stop
        log.add(System.currentTimeMillis());
        return log.size() <= maxRequestPerSec;
    }
}
