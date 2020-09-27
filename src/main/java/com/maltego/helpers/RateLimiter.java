package com.maltego.helpers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
    private Semaphore semaphore;
    private ScheduledExecutorService scheduledExecutor;
    private int maxPermits;

    public static RateLimiter from(int maxPermits, long delay, TimeUnit delayUnit) {
        return new RateLimiter(maxPermits, delay, delayUnit);
    }

    protected RateLimiter(int maxPermits, long delay, TimeUnit delayUnit) {
        this.maxPermits = maxPermits;
        this.semaphore = new Semaphore(maxPermits);
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.scheduledExecutor.scheduleAtFixedRate(this::clearSemaphore, delay, delay, delayUnit);
    }

    public boolean tryAcquire(){
        return semaphore.tryAcquire();
    }

    public void clearSemaphore() {
        semaphore.release(maxPermits - getAvailablePermits());
    }

    public int getAvailablePermits(){
        return semaphore.availablePermits();
    }
}
