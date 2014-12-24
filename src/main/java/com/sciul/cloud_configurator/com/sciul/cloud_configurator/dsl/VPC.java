package com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl.Resource;
import com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl.ResourceList;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public class VPC implements Resource {
  private final boolean defaultTenancy;
  private final boolean dnsSupport;
  private final boolean dnsHostname;
  private ResourceList resourceList;

  public VPC(String ciderBlock, ResourceList resourceList) {
    this(ciderBlock, true, true, true, resourceList);
  }

  public VPC(String ciderBlock, boolean defaultTenancy, boolean dnsSupport, boolean dnsHostname,
             ResourceList resourceList) {
    this.defaultTenancy = defaultTenancy;
    this.dnsSupport = dnsSupport;
    this.dnsHostname = dnsHostname;
    this.resourceList = resourceList;
  }

  public ResourceList next() {
    return resourceList;
  }

  @Override
  public String toJson() {
    return null;
  }

}
