package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class NetworkAclEntry extends Resource {

  @Override
  public JsonObject toJson(Provider provider) {
    return null;
  }

  @Override
  public Resource tag(String name, String value) {
    return null;
  }
}
