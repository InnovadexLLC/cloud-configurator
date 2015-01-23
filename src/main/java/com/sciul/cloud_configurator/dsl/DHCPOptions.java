package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.services.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class DHCPOptions extends Resource {

  private final String domainName;

  public DHCPOptions(String name, String domainName, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);
    this.domainName = domainName;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createDHCPOptions(this);
  }

  @Override
  public DHCPOptions tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getDomainName() {
    return domainName;
  }

}
