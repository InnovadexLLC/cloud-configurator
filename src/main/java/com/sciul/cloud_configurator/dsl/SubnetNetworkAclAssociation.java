package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class SubnetNetworkAclAssociation extends Resource {

  private String subnetId;
  private String networkAclId;

  public SubnetNetworkAclAssociation(String name, String subnetId, String networkAclId, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);

    this.subnetId = subnetId;
    this.networkAclId = networkAclId;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createSubnetNetworkAclAssociation(this);
  }

  @Override
  public SubnetNetworkAclAssociation tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getSubnetId() {
    return subnetId;
  }

  public String getNetworkAclId() {
    return networkAclId;
  }
}
