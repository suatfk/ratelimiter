package com.maltego.challenge.ipify;

import com.google.gson.annotations.Expose;
import lombok.Data;

/**
 * Model class for IPResult JSON object
 *
 * @author totakura
 */
@Data
public class IPResult {

  @Expose
  private String ip;
}
