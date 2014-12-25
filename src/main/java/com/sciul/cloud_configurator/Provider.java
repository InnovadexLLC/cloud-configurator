package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.model.CreateStackResult;
import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.ListStacksResult;
import com.sciul.cloud_configurator.dsl.*;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 11/14/14.
 */
public interface Provider {
  ListStacksResult listStacks(String[] args);

  DescribeStacksResult describeStacks(String environment);

  CreateStackResult createStack(Template template);

  JsonObject createVPC(VPC vpc);
  JsonObject createDNS(Dns dns);
  JsonObject createSubnet(Subnet subnet);
  void setRegion(String region);
  String generateStackTemplate(Template template);

  JsonObject createInternetGateway(InternetGateway internetGateway);

  JsonObject createDHCPOptions(DHCPOptions dhcpOptions);
}
