package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumeetrohatgi on 12/24/14.
 */
public class Subnet implements Resource {
  private String cidrBlock, availabilityZone;
  private ResourceList resourceList;
  private Map<String, String> tags = new HashMap<>();

  public Subnet(String cidrBlock, String availabilityZone, ResourceList resourceList) {
    setCidrBlock(cidrBlock);
    setAvailabilityZone(availabilityZone);
    setResourceList(resourceList);
  }

  public ResourceList next() {
    return resourceList;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createSubnet(this);
  }

  @Override
  public Subnet tag(String name, String value) {
    tags.put(name, value);
    return this;
  }


  public String getAvailabilityZone() {
    return availabilityZone;
  }

  public void setAvailabilityZone(String availabilityZone) {
    this.availabilityZone = availabilityZone;
  }

  public ResourceList getResourceList() {
    return resourceList;
  }

  public void setResourceList(ResourceList resourceList) {
    this.resourceList = resourceList;
  }

  public String getCidrBlock() {
    return cidrBlock;
  }

  public void setCidrBlock(String cidrBlock) {
    this.cidrBlock = cidrBlock;
  }
}
