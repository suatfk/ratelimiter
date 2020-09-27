/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maltego.helpers;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Request interceptor for adding our custom headers
 * <p>
 * We add {@code Accept-Language}, and {@code User-Agent} headers.
 */
public class RequestInterceptor implements Interceptor {

  public static final String HEADER_ACCEPT = "Accept";
  public static final String HEADER_ACCEPT_VALUE = "application/vnd.github.v3.full+json";
  public static final String HEADER_USER_AGENT = "User-Agent";
  public static final String HEADER_USER_AGENT_VALUE = "Maltego Transform Server";

  private RequestInterceptor() {

  }
  /**
   * Get the request, add headers, and return the response
   */
  @Override
  public Response intercept(Chain chain) throws IOException {

    Request request = chain.request().newBuilder()
        .addHeader(HEADER_ACCEPT, HEADER_ACCEPT_VALUE)
        .addHeader(HEADER_USER_AGENT, HEADER_USER_AGENT_VALUE)
        .build();
    return chain.proceed(request);
  }

  public static RequestInterceptor create() {
    return new RequestInterceptor();
  }
}
