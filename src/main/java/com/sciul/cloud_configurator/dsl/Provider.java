package com.sciul.cloud_configurator.dsl;

import com.amazonaws.services.cloudformation.model.CreateStackResult;
import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.ListStacksResult;
import com.sciul.cloud_application.models.Cloud;


import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 11/14/14.
 */
public interface Provider {
  ListStacksResult listStacks(String[] args);

  DescribeStacksResult describeStacks(String environment);

  Cloud createStack(ResourceList resourceList);

  void setRegion(String region);

  String generateStackTemplate(ResourceList resourceList);

  JsonObject createVPC(VPC vpc);

  JsonObject createDNS(Dns dns);

  JsonObject createSubnet(Subnet subnet);

  JsonObject createInternetGateway(InternetGateway internetGateway);

  JsonObject createDHCPOptions(DHCPOptions dhcpOptions);

  JsonObject createAcl(NetworkAcl networkAcl);

  JsonObject createRouteTable(RouteTable routeTable);

  JsonObject createNetworkAclEntry(NetworkAclEntry networkAclEntry);

  JsonObject createSubnetNetworkAclAssociation(SubnetNetworkAclAssociation subnetNetworkAclAssociation);

  JsonObject createVPCGatewayAttachment(VPCGatewayAttachment vpcGatewayAttachment);

  JsonObject createSubnetRouteTableAssociation(SubnetRouteTableAssociation association);

  JsonObject createRoute(Route route);

  JsonObject createVPCDHCPOptionsAssociation(VPCDHCPOptionsAssociation association);

  JsonObject createSecurityGroup(SecurityGroup securityGroup);
}
