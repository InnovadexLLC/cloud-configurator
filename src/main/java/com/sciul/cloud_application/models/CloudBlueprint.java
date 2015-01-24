package com.sciul.cloud_application.models;

import java.util.Map;

/**
 * Created by sumeetrohatgi on 1/22/15.
 */
public class CloudBlueprint {
  private String name;
  private String region;
  private String apiDomain;
  private String webDomain;
  private String apiKey;
  private String webKey;
  private Map<String, String[]> services;

  public Map<String, String[]> getServices() {
    return services;
  }

  public void setServices(Map<String, String[]> services) {
    this.services = services;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getApiDomain() {
    return apiDomain;
  }

  public void setApiDomain(String apiDomain) {
    this.apiDomain = apiDomain;
  }

  public String getWebDomain() {
    return webDomain;
  }

  public void setWebDomain(String webDomain) {
    this.webDomain = webDomain;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getWebKey() {
    return webKey;
  }

  public void setWebKey(String webKey) {
    this.webKey = webKey;
  }
}
