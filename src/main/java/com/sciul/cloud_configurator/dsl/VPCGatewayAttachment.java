package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class VPCGatewayAttachment extends Resource {

  private String vpcId;
  private String internetGatewayId;

  public VPCGatewayAttachment(String name, String vpcId, String internetGatewayId, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);

    this.internetGatewayId = internetGatewayId;
    this.vpcId = vpcId;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createVPCGatewayAttachment(this);
  }

  @Override
  public VPCGatewayAttachment tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getVpcId() {
    return vpcId;
  }

  public String getInternetGatewayId() {
    return internetGatewayId;
  }
}
