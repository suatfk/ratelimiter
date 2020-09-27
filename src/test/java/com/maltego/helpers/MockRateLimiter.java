package com.maltego.helpers;

import java.util.concurrent.TimeUnit;

public class MockRateLimiter extends RateLimiter {

    public int clearSemaphoreCount = 0;

    public int tryAcquireCount = 0;

    protected MockRateLimiter(int maxPermits, long delay, TimeUnit delayUnit) {
        super(maxPermits, delay, delayUnit);
    }

    @Override
    public boolean tryAcquire() {
        tryAcquireCount++;
        return super.tryAcquire();
    }

    @Override
    public void clearSemaphore() {
        clearSemaphoreCount++;
        super.clearSemaphore();
    }
}
