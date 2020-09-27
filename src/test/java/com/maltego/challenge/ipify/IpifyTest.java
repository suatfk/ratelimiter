package com.maltego.challenge.ipify;

import static org.junit.jupiter.api.Assertions.assertFalse;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author totakura
 */
@Slf4j
public class IpifyTest {
  private MockWebServer server;

  public IpifyTest() throws IOException {
    server = new MockWebServer();
    server.start(0);
  }

  @Test
  public void testConnection() throws InterruptedException {
    final var dispatcher = new MockDispatcher();
    server.setDispatcher(dispatcher);
    int tries = 20;
    final var semaphore = new Semaphore(tries);
    for (int round = 0; round < tries; round++) {
      var service = IpifyFactory.createService(server.url("/").toString());
      Call<IPResult> call = service.getIp();
      semaphore.acquire();
      call.enqueue(new ResponseCallback(semaphore));
    }
    semaphore.acquire(tries);
    assertFalse(dispatcher.hasFailed());
  }

  private static class ResponseCallback implements Callback {

    private Semaphore semaphore;

    ResponseCallback(Semaphore semaphore) {
      this.semaphore = semaphore;
    }
    @Override
    public void onResponse(Call call, Response rspns) {
      semaphore.release();
    }

    @Override
    public void onFailure(Call call, Throwable thrwbl) {
      semaphore.release();
    }

  }
}
