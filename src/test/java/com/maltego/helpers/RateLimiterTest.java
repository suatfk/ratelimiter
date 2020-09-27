package com.maltego.helpers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@Slf4j
public class RateLimiterTest {

    @Test
    public void givenPermitsAndDelay_whenPeriodFinished_shouldClearSemaphore() throws InterruptedException {
        MockRateLimiter rateLimiter = new MockRateLimiter(4, 1, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(2500);

        assertEquals(2, rateLimiter.clearSemaphoreCount);
        assertEquals(4, rateLimiter.getAvailablePermits());
    }

    @Test
    public void givenPermitsAndDelay_whenTryAcquire_shouldAcquirePermits() throws InterruptedException {
        MockRateLimiter rateLimiter = new MockRateLimiter(4, 1, TimeUnit.SECONDS);
        rateLimiter.tryAcquire();
        TimeUnit.MILLISECONDS.sleep(200);
        rateLimiter.tryAcquire();
        TimeUnit.MILLISECONDS.sleep(200);
        rateLimiter.tryAcquire();

        assertEquals(1, rateLimiter.getAvailablePermits());
        assertEquals(3, rateLimiter.tryAcquireCount);
    }

    @Test
    public void givenMaxPermitsAndDelay_whenAllPermitsAcquired_thenNoAvailablePermits() throws InterruptedException {
        MockRateLimiter rateLimiter = new MockRateLimiter(4, 1, TimeUnit.SECONDS);
        rateLimiter.tryAcquire();
        TimeUnit.MILLISECONDS.sleep(100);
        rateLimiter.tryAcquire();
        TimeUnit.MILLISECONDS.sleep(100);
        rateLimiter.tryAcquire();
        TimeUnit.MILLISECONDS.sleep(100);
        rateLimiter.tryAcquire();

        assertFalse(rateLimiter.tryAcquire());
        assertEquals(5, rateLimiter.tryAcquireCount);
        assertEquals(0, rateLimiter.getAvailablePermits());

    }

    @Test
    public void givenMaxPermitsAndDelay_whenAcquiredPermits_shouldReleaseAfterPeriod() throws InterruptedException {
        MockRateLimiter rateLimiter = new MockRateLimiter(4, 1, TimeUnit.SECONDS);
        rateLimiter.tryAcquire();
        TimeUnit.MILLISECONDS.sleep(200);
        rateLimiter.tryAcquire();
        TimeUnit.MILLISECONDS.sleep(200);
        rateLimiter.tryAcquire();

        TimeUnit.MILLISECONDS.sleep(600);
        assertEquals(1, rateLimiter.clearSemaphoreCount);
        assertEquals(3, rateLimiter.tryAcquireCount);
        assertEquals(4, rateLimiter.getAvailablePermits());
    }

}