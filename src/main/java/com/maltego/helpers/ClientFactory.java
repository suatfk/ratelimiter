/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maltego.helpers;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Factory for creating various API clients
 */
public class ClientFactory {

  private static final OkHttpClient CLIENT;

  static {
    CLIENT = new OkHttpClient.Builder()
        .addInterceptor(RequestInterceptor.create())
        .addInterceptor(RatelimitingInterceptor.create(4, 1, TimeUnit.SECONDS))
        .build();
  }

  /**
   * Creates a builder for a default Retrofit client
   * <p>
   * The client sends our custom headers added in {@link RequestInterceptor#intercept} and parses
   * the response as JSON using GSON
   *
   * @return retrofit client builder. Call {@link retrofit2.Retrofit.Builder#baseUrl()}.{@link
   * retrofit2.Retrofit.Builder#build() build()} to get the retrofit client
   */
  public static Retrofit.Builder getGsonDefaultClientBuilder() {
    return new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(CLIENT);
  }

  /**
   * Creates a default Retrofit client
   *
   * @param url URL of the API endpoint. E.g: https://test.wikipedia.org/
   * @return default retrofit client
   */
  public static Retrofit getGsonDefaultClient(String url) {
    return getGsonDefaultClientBuilder()
        .baseUrl(url)
        .build();
  }

}
