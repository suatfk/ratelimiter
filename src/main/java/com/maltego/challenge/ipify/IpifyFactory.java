package com.maltego.challenge.ipify;

import com.maltego.helpers.ClientFactory;

/**
 *
 * @author totakura
 */
public class IpifyFactory {

  public static IpifyQueryService createService(String url) {
    var retrofit = ClientFactory.getGsonDefaultClient(url);
    return retrofit.create(IpifyQueryService.class);
  }

  public static IpifyQueryService createService() {
    return createService("https://api.ipify.org/");
  }
}
