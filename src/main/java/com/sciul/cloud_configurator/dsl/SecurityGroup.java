package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/30/14.
 */
public class SecurityGroup extends Resource {
  private IngressRules[] ingressRules;
  private String vpcId;
  private String groupDescription;

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createSecurityGroup(this);
  }

  @Override
  public SecurityGroup tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public IngressRules[] getIngressRules() {
    return ingressRules;
  }

  public String getVpcId() {
    return vpcId;
  }

  public String getGroupDescription() {
    return groupDescription;
  }

  public class IngressRules {

    private String ipProtocol;
    private String fromPort;
    private String toPort;
    private String sourceSecurityGroupId;

    public String getIpProtocol() {
      return ipProtocol;
    }

    public String getFromPort() {
      return fromPort;
    }

    public String getToPort() {
      return toPort;
    }

    public String getSourceSecurityGroupId() {
      return sourceSecurityGroupId;
    }
  }
}
