package com.maltego.challenge.ipify;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

/**
 * @author totakura
 */
@Slf4j
public final class MockDispatcher extends Dispatcher {

  private final AtomicInteger counter;
  private boolean failed;

  public MockDispatcher() {
    this.counter = new AtomicInteger();
    failed = false;
  }

  @Override
  public MockResponse dispatch(RecordedRequest rr) throws InterruptedException {
    int value = counter.incrementAndGet();
    if (5 == value) { //checks if at anytime there were more than 4 concurrent requests
      failed = true;
    }
    Thread.sleep(1000); //artificial delay to simulate processing time at the server
    counter.decrementAndGet();
    var response = new MockResponse();
    response.setBody("OK");
    return response;
  }

  public boolean hasFailed() {
    return failed;
  }

}
