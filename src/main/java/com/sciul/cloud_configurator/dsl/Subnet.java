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
  private final String vpcName;

  public Subnet(String name, String cidrBlock, String availabilityZone,
                boolean publicConnected, String vpcName, ResourceList resourceList) {
    this.resourceList = resourceList;
    this.vpcName = vpcName;

    setCidrBlock(cidrBlock);
    setAvailabilityZone(availabilityZone);
    name = "SUBNET-" + name;
    setName(name + "-" + availabilityZone);

    // a new route table
    RouteTable rt = new RouteTable(name + "-RTB", vpcName, resourceList);

    // associated with the vpc
    SubnetRouteTableAssociation rta =
        new SubnetRouteTableAssociation(name + "-RTB" + "-RTA", rt.getName(), vpcName, resourceList);

    String envName = resourceList.tags.get("Name");

    // with a default acl
    SubnetNetworkAclAssociation naa =
        new SubnetNetworkAclAssociation(name + "-RTB" + "-ACL", getName(), envName + "-ACL", resourceList);

    // a new route
    String gatewayId = null;
    String instanceId = null;

    if (publicConnected) {
      gatewayId = resourceList.getName() + "-IGW";
    } else {
      instanceId = "";
    }

    Route route = new Route(name + "-RTB" + "-E", rt.getName(), gatewayId, instanceId, resourceList);

    resourceList.add(rt);
    resourceList.add(rta);
    resourceList.add(naa);
    resourceList.add(route);
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

  public String getCidrBlock() {
    return cidrBlock;
  }

  public void setCidrBlock(String cidrBlock) {
    this.cidrBlock = cidrBlock;
  }

  public String getVpcName() {
    return vpcName;
  }
}
