package com.sciul.cloud_configurator.dsl;

import javax.json.*;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public class VPC extends Resource {
  private final String defaultTenancy = "default";
  private final boolean dnsSupport;
  private final boolean dnsHostname;
  private final String cidrBlock;

  public VPC(String ciderBlock, String region, ResourceList resourceList) {
    this(ciderBlock, region, true, true, resourceList);
  }

  public VPC(String ciderBlock, String region, boolean dnsSupport, boolean dnsHostname,
             ResourceList resourceList) {
    this.cidrBlock = ciderBlock;
    this.dnsSupport = dnsSupport;
    this.dnsHostname = dnsHostname;
    this.resourceList = resourceList;
    setName("Vpc");

    DHCPOptions dhcpOptions = new DHCPOptions("Dpt", region + ".compute.internal", resourceList);
    resourceList.add(dhcpOptions);

    VPCDHCPOptionsAssociation association = new VPCDHCPOptionsAssociation("VpcDptEntry", getName(),
        dhcpOptions.getName(), resourceList);

    resourceList.add(association);

    InternetGateway gw = new InternetGateway("Igw", resourceList);
    resourceList.add(gw);

    resourceList.add(new VPCGatewayAttachment("VpcGw", getName(), gw.getName(), resourceList));
    resourceList.add(new NetworkAcl("Acl", getName(), resourceList));
  }

  public String getDefaultTenancy() {
    return defaultTenancy;
  }

  public boolean isDnsSupport() {
    return dnsSupport;
  }

  public boolean isDnsHostname() {
    return dnsHostname;
  }

  public String getCidrBlock() {
    return cidrBlock;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createVPC(this);
  }

  @Override
  public VPC tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

}
