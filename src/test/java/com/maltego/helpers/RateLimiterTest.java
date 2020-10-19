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
        RateLimiter rateLimiter = RateLimiter.from(4, 1, TimeUnit.SECONDS);
        for (int i = 0; i < 5; i++){
            if (rateLimiter.tryAcquire()){
                log.info("work");
            }
        }

        TimeUnit.SECONDS.sleep(1);
        for (int i = 0; i < 5; i++){
            if (rateLimiter.tryAcquire()){
                log.info("work");
            }
        }

    }
}