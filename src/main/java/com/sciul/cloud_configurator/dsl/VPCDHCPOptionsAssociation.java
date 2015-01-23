package com.sciul.cloud_configurator.dsl;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/26/14.
 */
public class VPCDHCPOptionsAssociation extends Resource {
  private final String vpcId;
  private final String dhcpOptionsId;

  public VPCDHCPOptionsAssociation(String name, String vpcId, String dhcpOptionsId, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);

    this.vpcId = vpcId;
    this.dhcpOptionsId = dhcpOptionsId;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createVPCDHCPOptionsAssociation(this);
  }

  @Override
  public VPCDHCPOptionsAssociation tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getVpcId() {
    return vpcId;
  }

  public String getDhcpOptionsId() {
    return dhcpOptionsId;
  }
}
