package com.sciul.cloud_configurator.dsl;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeetrohatgi on 12/30/14.
 */
public class SecurityGroup extends Resource {
  private List<IngressRules> ingressRules;
  private String vpcId;
  private String groupDescription;

  public SecurityGroup(String name, String vpcId, String groupDescription, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);

    this.vpcId = vpcId;
    this.groupDescription = groupDescription;

    this.ingressRules = new ArrayList<IngressRules>();
  }

  /**
   * add an ingress rule
   *
   * @return
   */
  public ResourceList addRule(String ipProtocol, String fromPort, String toPort, String sourceSecurityGroupId) {
    IngressRules rule = new IngressRules();
    rule.fromPort = fromPort;
    rule.ipProtocol = ipProtocol;
    rule.toPort = toPort;
    rule.sourceSecurityGroupId = sourceSecurityGroupId;

    ingressRules.add(rule);

    return resourceList;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createSecurityGroup(this);
  }

  @Override
  public SecurityGroup tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public Iterable<IngressRules> getIngressRules() {
    return ingressRules;
  }

  public String getVpcId() {
    return vpcId;
  }

  public String getGroupDescription() {
    return groupDescription;
  }

  public final class IngressRules {
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
