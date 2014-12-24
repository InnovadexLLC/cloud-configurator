package com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl.Resource;
import com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl.ResourceList;

import javax.json.*;
import java.util.Map;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public class VPC implements Resource {
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

  @Override
  public JsonObject toJson() {
    return
        Json.createObjectBuilder()
            .add("CidrBlock", cidrBlock)
            .add("InstanceTenancy", defaultTenancy)
            .add("EnableDnsSupport", dnsSupport)
            .add("EnableDnsHostnames", dnsHostname)
            .build();
  }

}
