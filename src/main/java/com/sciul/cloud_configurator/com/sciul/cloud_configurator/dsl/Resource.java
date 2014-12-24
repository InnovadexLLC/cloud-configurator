package com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public interface Resource {
  JsonObject toJson(Provider provider);
}
