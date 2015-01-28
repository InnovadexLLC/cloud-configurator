package com.sciul.cloud_configurator.dsl;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.*;
import com.sciul.cloud_application.models.Cloud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by sumeetrohatgi on 11/14/14.
 */
@Component
@PropertySource("classpath:application.properties")
public class AwsProvider implements Provider {
  private static final String ACCESS_KEY = "AWS_ACCESS_KEY";
  private static final String SECRET_KEY = "AWS_SECRET_KEY";
  private static Logger logger = LoggerFactory.getLogger(AwsProvider.class);

  private AWSCredentials credentials;
  private AmazonCloudFormationClient clt;
  private String region;

  public AwsProvider() {
    try {

      credentials = new ProfileCredentialsProvider().getCredentials();

      clt = new AmazonCloudFormationClient(credentials);

    } catch (Exception e) {
      throw new RuntimeException("Cannot load the credentials from the credential profiles file. "
          + "Please make sure that your credentials file is at the correct "
          + "location (~/.aws/credentials), and is in valid format.", e);
    }
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    if (region == null) {
      throw new RuntimeException("illegal region value: " + region);
    }
    this.region = region;
    Regions r = Regions.DEFAULT_REGION;
    switch (region) {
      case "us-west-2":
        r = Regions.US_WEST_2;
        break;
      default:
        throw new RuntimeException("illegal region: " + region);
    }
    clt.setRegion(Region.getRegion(Regions.US_WEST_2));
  }

  public String generateStackTemplate(final ResourceList resourceList) {
    JsonObjectBuilder resourceBuilder = Json.createObjectBuilder();

    for (Resource resource : resourceList.resources()) {
      resourceBuilder.add(resource.getName(), resource.toJson(this));
    }

    JsonObjectBuilder mainBuilder = Json
        .createObjectBuilder()
        .add("AWSTemplateFormatVersion","2010-09-09")
        .add("Resources", resourceBuilder);

    return mainBuilder.build().toString();
  }

  public Cloud createStack(final ResourceList resourceList) {

    try {
      CreateStackRequest crq = new CreateStackRequest() {
        {
          setStackName(resourceList.getName());
          setTemplateBody(generateStackTemplate(resourceList));
        }
      };

      logger.debug("template: {}", crq.getTemplateBody());

      CreateStackResult crs = clt.createStack(crq);

      logger.debug("createStack result: {}", crs);

      Cloud cloud = new Cloud();
      cloud.setId(crs.getStackId());
      return cloud;
    } catch (AmazonServiceException ae) {
      throw new RuntimeException("server error", ae);
    } catch (AmazonClientException ae) {
      throw new RuntimeException("client error", ae);
    }
  }

  @Override
  public JsonObject createVPC(VPC vpc) {
    return
        Json.createObjectBuilder()
            .add("Type", "AWS::EC2::VPC")
            .add("Properties",
                Json.createObjectBuilder()
                    .add("CidrBlock", vpc.getCidrBlock())
                    .add("InstanceTenancy", vpc.getDefaultTenancy())
                    .add("EnableDnsSupport", vpc.isDnsSupport() + "")
                    .add("EnableDnsHostnames", vpc.isDnsHostname() + "")
                    .add("Tags", getTagBuilder(vpc))
            )
            .build();
  }

  @Override
  public JsonObject createDNS(Dns dns) {
    String hostedDomain = dns.getHostedZoneName().endsWith(".")?dns.getHostedZoneName(): dns.getHostedZoneName() + ".";
    String domainName = dns.getDomain().endsWith(".")?dns.getDomain(): dns.getDomain() + ".";

    return
        Json.createObjectBuilder()
            .add("Type", "AWS::Route53::RecordSetGroup")
            .add("Properties",
                Json.createObjectBuilder()
                    .add("HostedZoneName", hostedDomain)
                    .add("RecordSets",
                        Json.createArrayBuilder()
                            .add(Json.createObjectBuilder()
                                    .add("Name", domainName)
                                    .add("Type", dns.getType())
                                    .add("TTL", dns.getTtl())
                                    .add("ResourceRecords", Json.createArrayBuilder()
                                        .add(Json.createObjectBuilder().add("Ref",dns.getRefer())))
                            )
                    )
                    .add("Tags", getTagBuilder(dns)))
            .build();
  }

  @Override
  public JsonObject createSubnet(Subnet subnet) {
    return
        Json.createObjectBuilder()
            .add("Type", "AWS::EC2::Subnet")
            .add("Properties", Json.createObjectBuilder()
                .add("CidrBlock", subnet.getCidrBlock())
                .add("AvailabilityZone", subnet.getAvailabilityZone())
                .add("VpcId", Json.createObjectBuilder().add("Ref", subnet.getVpcName()))
                .add("Tags", getTagBuilder(subnet)))
            .build();
  }

  @Override
  public JsonObject createInternetGateway(InternetGateway internetGateway) {
    return
        Json.createObjectBuilder()
            .add("Type", "AWS::EC2::InternetGateway")
            .add("Properties", Json.createObjectBuilder()
                .add("Tags", getTagBuilder(internetGateway)))
            .build();
  }

  @Override
  public JsonObject createDHCPOptions(DHCPOptions dhcpOptions) {
    return
        Json.createObjectBuilder()
            .add("Type", "AWS::EC2::DHCPOptions")
            .add("Properties", Json.createObjectBuilder()
                .add("DomainName", dhcpOptions.getDomainName())
                .add("DomainNameServers", Json.createArrayBuilder().add("AmazonProvidedDNS"))
                .add("Tags", getTagBuilder(dhcpOptions)))
            .build();
  }

  @Override
  public JsonObject createAcl(NetworkAcl networkAcl) {
    return
        Json.createObjectBuilder()
            .add("Type", "AWS::EC2::NetworkAcl")
            .add("Properties", Json.createObjectBuilder()
                .add("VpcId", Json.createObjectBuilder().add("Ref", networkAcl.getVpcName()))
                .add("Tags", getTagBuilder(networkAcl)))
            .build();
  }

  @Override
  public JsonObject createRouteTable(RouteTable routeTable) {
    return
        Json.createObjectBuilder()
            .add("Type", "AWS::EC2::RouteTable")
            .add("Properties", Json.createObjectBuilder()
                .add("VpcId", Json.createObjectBuilder().add("Ref", routeTable.getVpcName()))
                .add("Tags", getTagBuilder(routeTable)))
            .build();
  }

  @Override
  public JsonObject createNetworkAclEntry(NetworkAclEntry networkAclEntry) {
    return
        Json.createObjectBuilder()
            .add("Type", "AWS::EC2::NetworkAclEntry")
            .add("Properties", Json.createObjectBuilder()
                .add("CidrBlock", networkAclEntry.getCidrBlock())
                .add("Egress", networkAclEntry.isEgress())
                .add("Protocol", networkAclEntry.getProtocol())
                .add("RuleAction", networkAclEntry.getRuleAction())
                .add("RuleNumber", networkAclEntry.getRuleNumber())
                .add("NetworkAclId", Json.createObjectBuilder().add("Ref", networkAclEntry.getNetworkAclId()))
                .add("Tags", getTagBuilder(networkAclEntry)))
            .build();
  }

  @Override
  public JsonObject createSubnetNetworkAclAssociation(SubnetNetworkAclAssociation subnetNetworkAclAssociation) {
    return Json.createObjectBuilder()
        .add("Type", "AWS::EC2::SubnetNetworkAclAssociation")
        .add("Properties", Json.createObjectBuilder()
            .add("SubnetId", Json.createObjectBuilder()
                .add("Ref", subnetNetworkAclAssociation.getSubnetId()))
            .add("NetworkAclId", Json.createObjectBuilder()
                .add("Ref", subnetNetworkAclAssociation.getNetworkAclId()))
            .add("Tags", getTagBuilder(subnetNetworkAclAssociation)))
        .build();
  }

  @Override
  public JsonObject createVPCGatewayAttachment(VPCGatewayAttachment vpcGatewayAttachment) {
    return Json.createObjectBuilder()
        .add("Type", "AWS::EC2::VPCGatewayAttachment")
        .add("Properties", Json.createObjectBuilder()
            .add("VpcId", Json.createObjectBuilder()
                .add("Ref", vpcGatewayAttachment.getVpcId()))
            .add("InternetGatewayId", Json.createObjectBuilder()
                .add("Ref", vpcGatewayAttachment.getInternetGatewayId()))
            .add("Tags", getTagBuilder(vpcGatewayAttachment)))
        .build();
  }

  @Override
  public JsonObject createSubnetRouteTableAssociation(SubnetRouteTableAssociation association) {
    return Json.createObjectBuilder()
        .add("Type", "AWS::EC2::SubnetRouteTableAssociation")
        .add("Properties", Json.createObjectBuilder()
            .add("RouteTableId", Json.createObjectBuilder()
                .add("Ref", association.getRouteTableId()))
            .add("SubnetId", Json.createObjectBuilder()
                .add("Ref", association.getSubnetId()))
            .add("Tags", getTagBuilder(association)))
        .build();
  }

  @Override
  public JsonObject createRoute(Route route) {
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
        .add("Type", "AWS::EC2::Route")
        .add("Properties", Json.createObjectBuilder()
            .add("DestinationCidrBlock", route.getDestinationCidrBlock())
            .add("RouteTableId", Json.createObjectBuilder()
                .add("Ref", route.getRouteTableId()))
            .add(route.getIdType(), Json.createObjectBuilder()
                .add("Ref", route.getIdValue())));

    if (route.isDependsOn()) {
      jsonObjectBuilder.add("DependsOn", route.getDependOnId());
    }

    return jsonObjectBuilder.build();
  }

  @Override
  public JsonObject createVPCDHCPOptionsAssociation(VPCDHCPOptionsAssociation association) {
    return Json.createObjectBuilder()
        .add("Type", "AWS::EC2::VPCDHCPOptionsAssociation")
        .add("Properties", Json.createObjectBuilder()
            .add("VpcId", Json.createObjectBuilder()
                .add("Ref", association.getVpcId()))
            .add("DhcpOptionsId", Json.createObjectBuilder()
                .add("Ref", association.getDhcpOptionsId())))
        .build();
  }

  @Override
  public JsonObject createSecurityGroup(SecurityGroup securityGroup) {
    JsonArrayBuilder ingressRules = Json.createArrayBuilder();

    for (SecurityGroup.IngressRules ir : securityGroup.getIngressRules()) {
      ingressRules
          .add(Json.createObjectBuilder()
                  .add("IpProtocol", ir.getIpProtocol())
                  .add("FromPort", ir.getFromPort())
                  .add("ToPort", ir.getToPort())
                  .add("SourceSecurityGroupId", Json.createObjectBuilder()
                  .add("Ref", ir.getSourceSecurityGroupId()))
          );
    }

    return Json.createObjectBuilder()
        .add("Type", "AWS::EC2::SecurityGroup")
        .add("Properties", Json.createObjectBuilder()
            .add("VpcId", Json.createObjectBuilder()
                .add("Ref", securityGroup.getVpcId()))
            .add("GroupDescription", securityGroup.getGroupDescription())
            .add("SecurityGroupIngress", ingressRules))
        .build();
  }

  private JsonArrayBuilder getTagBuilder(Resource resource) {
    JsonArrayBuilder tagArrayBuilder = Json.createArrayBuilder();

    for (Map.Entry<String, String> tag : resource.tags()) {
      tagArrayBuilder
          .add(Json.createObjectBuilder()
              .add("Key", tag.getKey())
              .add("Value", tag.getValue()));
    }

    return tagArrayBuilder;
  }

  public DescribeStacksResult describeStacks(String environment) {
    //logger.debug(s.getVpcName());
    DescribeStacksRequest d = new DescribeStacksRequest();
    if (environment != null && !environment.equalsIgnoreCase("")) {
      d.setStackName(environment);
    }
    return clt.describeStacks(d);
  }

  public ListStacksResult listStacks(String[] args) {
    ListStacksRequest rq = new ListStacksRequest();
    rq.setStackStatusFilters(new ArrayList<String>() {
      {
        add("CREATE_COMPLETE");
      }
    });
    ListStacksResult lst = clt.listStacks(rq);
    return lst;
  }

}
