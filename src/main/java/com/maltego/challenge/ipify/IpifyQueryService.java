package com.maltego.challenge.ipify;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *
 * @author totakura
 */
public interface IpifyQueryService {

  /**
   * Perform a GET request to retrieve our current IP address
   *
   * @return
   */
  @GET("/?format=json")
  Call<IPResult> getIp();
}
