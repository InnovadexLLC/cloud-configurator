package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.services.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class NetworkAclEntry extends Resource {

  private String cidrBlock;
  private String protocol;
  private String ruleAction;
  private String ruleNumber;
  private String networkAclId;
  private boolean egress;

  public NetworkAclEntry(String name, String cidrBlock, String protocol, String ruleAction,
                         String ruleNumber, String networkAclId, boolean egress, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);

    this.cidrBlock = cidrBlock;
    this.protocol = protocol;
    this.ruleAction = ruleAction;
    this.ruleNumber = ruleNumber;
    this.networkAclId = networkAclId;
    this.egress = egress;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createNetworkAclEntry(this);
  }

  @Override
  public NetworkAclEntry tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getCidrBlock() {
    return cidrBlock;
  }

  public String getProtocol() {
    return protocol;
  }

  public String getRuleAction() {
    return ruleAction;
  }

  public String getRuleNumber() {
    return ruleNumber;
  }

  public String getNetworkAclId() {
    return networkAclId;
  }

  public boolean isEgress() {
    return egress;
  }
}
