package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumeetrohatgi on 12/24/14.
 */
public class Subnet extends Resource {
  private String cidrBlock, availabilityZone;
  private VPC vpc;

  public Subnet(String name, String cidrBlock, String availabilityZone, VPC vpc) {
    setName(name);
    setCidrBlock(cidrBlock);
    setAvailabilityZone(availabilityZone);
    setVPC(vpc);
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

  public VPC getVPC() {
    return vpc;
  }

  public void setVPC(VPC vpc) {
    this.vpc = vpc;
    resourceList = vpc.resourceList;
  }

  public String getCidrBlock() {
    return cidrBlock;
  }

  public void setCidrBlock(String cidrBlock) {
    this.cidrBlock = cidrBlock;
  }
}
