package com.maltego.helpers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 * @author totakura
 */
public class RatelimitingInterceptor implements Interceptor {

  private RateLimiter rateLimiter;

  /**
   * Private constructor to force instantiation through factory method
   */
  private RatelimitingInterceptor() {
  }

  public RatelimitingInterceptor(RateLimiter rateLimiter) {
    this.rateLimiter = rateLimiter;
  }

  /**
   * Allow or deny the request after applying ratelimiting.
   *
   * The request is allowed if the number of requests have not exceeded that configured number of
   * requests per delay.
   *
   * @return true if the request is to be allowed; false if not
   */
  private boolean allowRequest() {
    return rateLimiter.tryAcquire();
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request();
    //ip or clientId based implementation required
    //but for the simplicity our limiter works for every requests
    //and we have to destroy ClientBased RateLimiter after given timeout to save execution time
    if (!allowRequest()) {
      Response failResponse = new Response.Builder()
          .request(request)
          .protocol(Protocol.HTTP_1_1)
          .code(901)
          .message("Ratelimited at the client")
          .body(ResponseBody.create(MediaType.get("text/plain"), "Ratelimited at the client"))
          .build();

      return failResponse;
    }
    return chain.proceed(request);
  }

  /**
   * Creates a ratelimiting interceptor object
   *
   * @param maxRequestPerSec the number of requests to be allowed in second
   * @return a RatelimitingInterceptor object
   */
  public static RatelimitingInterceptor create(int maxRequestPerSec) {
    return new RatelimitingInterceptor(RateLimiter.from(maxRequestPerSec));
  }
}
