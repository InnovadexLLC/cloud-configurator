package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.services.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class SubnetRouteTableAssociation extends Resource {

  private String routeTableId;
  private String subnetId;

  public SubnetRouteTableAssociation(String name, String routeTableId, String subnetId, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);

    this.subnetId = subnetId;
    this.routeTableId = routeTableId;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createSubnetRouteTableAssociation(this);
  }

  @Override
  public SubnetRouteTableAssociation tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getRouteTableId() {
    return routeTableId;
  }

  public String getSubnetId() {
    return subnetId;
  }
}
