package com.sciul.cloud_configurator.dsl;

import javax.json.JsonObject;

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

    String subnetName = "Subnet" + name + "-" + availabilityZone;
    setName(subnetName);

    // a new route table
    String routeTableName = "Subnet" + name + "Rtb";
    RouteTable rt = new RouteTable(routeTableName, vpcName, resourceList);

    // associated with the vpc
    String routeTableAssociation = subnetName + "Rta";
    SubnetRouteTableAssociation rta =
        new SubnetRouteTableAssociation(routeTableAssociation, rt.getName(), getName(), resourceList);


    // with a default acl
    String defaultAcl = resourceList.getName() + "Acl";
    String networkAcl = subnetName + "Acl";
    SubnetNetworkAclAssociation naa =
        new SubnetNetworkAclAssociation(networkAcl, getName(), defaultAcl, resourceList);

    // a new route
    String gatewayId = null;
    String instanceId = null;

    if (publicConnected) {
      gatewayId = resourceList.getName() + "Igw";
    } else {
      instanceId = "";
    }

    String routeTableEntry = routeTableName + "RouteEntry";
    Route route = new Route(routeTableEntry, rt.getName(), gatewayId, instanceId, resourceList);

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
