package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class InternetGateway extends Resource {

  public InternetGateway(String name, ResourceList resourceList) {
    this.resourceList = resourceList;
    this.setName(name);
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createInternetGateway(this);
  }

  @Override
  public InternetGateway tag(String name, String value) {
    tags.put(name, value);
    return this;
  }
}
