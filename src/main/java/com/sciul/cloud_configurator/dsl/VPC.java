package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public class VPC extends Resource {
  private final String defaultTenancy = "default";
  private final boolean dnsSupport;
  private final boolean dnsHostname;
  private String cidrBlock;
  private ResourceList resourceList;

  public VPC(String ciderBlock, ResourceList resourceList) {
    this(ciderBlock, true, true, resourceList);
  }

  public VPC(String ciderBlock, boolean dnsSupport, boolean dnsHostname,
             ResourceList resourceList) {
    this.cidrBlock = ciderBlock;
    this.dnsSupport = dnsSupport;
    this.dnsHostname = dnsHostname;
    this.resourceList = resourceList;
  }

  public ResourceList next() {
    return resourceList;
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

  public void setCidrBlock(String cidrBlock) {
    this.cidrBlock = cidrBlock;
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
