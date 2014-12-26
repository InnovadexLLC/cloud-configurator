package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class RouteTable extends Resource {
  private String vpcName;

  public RouteTable(String name, String vpcName, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);
    this.vpcName = vpcName;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createRouteTable(this);
  }

  @Override
  public RouteTable tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getVpcName() {
    return vpcName;
  }
}
