package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class Route extends Resource {
  private String destinationCidrBlock;
  private String routeTableId;
  private String idType;
  private String idValue;
  private String vpcGatewayAttachmentId;
  private boolean dependsOn;
  private String dependOnId;

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
