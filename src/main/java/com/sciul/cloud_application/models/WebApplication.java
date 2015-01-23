package com.sciul.cloud_application.models;

/**
 * Created by sumeetrohatgi on 1/22/15.
 */
public class WebApplication {
  private String name;
  private String region;
  private String apiDomain;
  private String webDomain;
  private String apiKey;
  private String webKey;
  private String[] dataServices;

  public String[] getDataServices() {
    return dataServices;
  }

  public void setDataServices(String[] dataServices) {
    this.dataServices = dataServices;
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
