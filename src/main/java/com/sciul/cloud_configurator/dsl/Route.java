package com.sciul.cloud_configurator.dsl;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class Route extends Resource {
  private final String destinationCidrBlock;
  private final String routeTableId;
  private final String idType;
  private final String idValue;
  private final boolean dependsOn;
  private final String dependOnId;

  public Route(String name, String routeTableId, String gatewayId, String instanceId, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);

    destinationCidrBlock = "0.0.0.0/0";
    String vpcGatewayAttachmentId = resourceList.getName() + "-VPC-GW";
    this.routeTableId = routeTableId;

    if (gatewayId != null) {
      idType = "GatewayId";
      idValue = gatewayId;
      dependsOn = true;
      dependOnId = vpcGatewayAttachmentId;
    } else {
      dependsOn = false;
      dependOnId = null;
      idType = "InstanceId";
      idValue = instanceId;
    }
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createRoute(this);
  }

  @Override
  public Route tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getDestinationCidrBlock() {
    return destinationCidrBlock;
  }

  public String getRouteTableId() {
    return routeTableId;
  }

  public String getIdType() {
    return idType;
  }

  public String getIdValue() {
    return idValue;
  }

  public boolean isDependsOn() {
    return dependsOn;
  }

  public String getDependOnId() {
    return dependOnId;
  }
}
