package com.sciul.cloud_configurator.dsl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Captures cloud building intent in as neutral terms
 * as possible.
 *
 * Created by sumeetrohatgi on 12/23/14.
 */
public class ResourceList {
  private ArrayList<Resource> resources = new ArrayList<>();
  private Map<String, String> tags = new HashMap<>();
  private static Logger logger = LoggerFactory.getLogger(ResourceList.class);


  private ResourceList() {}

  public String getName() {
    return tags.get("Name");
  }

  /**
   * starting to define the template
   *
   * @param name of the environment
   * @return
   */
  public static ResourceList start(String name) {
    return start(name, null);
  }

  /**
   * starting to define the template
   *
   * @param name of the environment
   * @param tags master tags
   * @return
   */
  public static ResourceList start(String name, Map<String, String> tags) {
    ResourceList resourceList = new ResourceList();
    name = name.toUpperCase();
    resourceList.tags.put("Name", name);
    resourceList.tags.put("BelongsTo", name);
    if ( tags != null ) resourceList.tags.putAll(tags);
    return resourceList;
  }

  /**
   * define dns entry; we assume for now that its a CNAME
   *
   * @param name unique dsl entry name
   * @param hostedZoneName main domain name
   * @return
   */
  public ResourceList dns(String loadBalancer, String name, String hostedZoneName) {
    Dns dns = new Dns(name, hostedZoneName, loadBalancer, this);
    resources.add(dns);
    return this;
  }

  /**
   * define a vpc, only one per template for now
   *
   * @param ciderBlock example: 10.0.0.0/16
   * @param region example: us-west-2
   * @return
   */
  public ResourceList vpc(String ciderBlock, String region) {
    VPC vpc = new VPC(ciderBlock, region, this);
    resources.add(vpc);
    return this;
  }

  /**
   * private subnet on a VPC
   * @param name
   * @param ciderUtils
   * @param zone
   * @return
   */
  public ResourceList subnet(String name, CidrUtils ciderUtils, String ... zone) {
    return subnet(name, ciderUtils, false, zone);
  }

  /**
   * public subnet on a VPC
   *
   * @param name
   * @param ciderUtils
   * @param isPublicConnected
   * @param zone
   * @return
   */
  public ResourceList subnet(String name, CidrUtils ciderUtils, boolean isPublicConnected, String ... zone) {
    boolean first = true;
    for (String z : zone) {
      if (first) {
        first = false;
      } else {
        ciderUtils.incrementSubnet();
      }
      logger.debug("name: {} cidrBlock: {}", name, ciderUtils);
      Subnet subnet = new Subnet(name, ciderUtils.toString(), z, isPublicConnected, getName() + "-VPC", this);
      resources.add(subnet);
    }
    ciderUtils.incrementSubnet();
    return this;
  }

  /**
   * a new security group
   *
   * @param name
   * @param description
   * @param vpcId
   * @return
   */
  public SecurityGroup group(String name, String description, String vpcId) {
    return new SecurityGroup(name, description, getName() + "-VPC", this);
  }

  /**
   * TODO: define a new load balancer
   *
   * @param name
   * @return
   */
  public ResourceList elb(String name) {
    return this;
  }

  /**
   * indicate that the template is finished
   *
   * @return
   */
  public ResourceList end() {
    return this;
  }

  public List<Resource> resources() {
    return resources;
  }

  /**
   * shallow copy of tags
   *
   * @return
   */
  public Map<String, String> getTags() {
    return new HashMap<>(tags);
  }

  void add(Resource resource) {
    resources.add(resource);
  }

  /**
   * add or modify security group
   *
   * @param subnetName name of the subnet
   * @param publiclyAccessible is it open to public?
   * @param protocol tcp or udp
   * @param ports list of ports
   * @return
   */
  public ResourceList allow(String subnetName, boolean publiclyAccessible, String protocol, List<Integer> ports) {
    // have we already added this security group?
    SecurityGroup sg = null;
    for (Resource r : resources) {
      if (r instanceof SecurityGroup && r.getName().equals(subnetName)) {
        sg = (SecurityGroup)r;
        break;
      }
    }

    if (sg == null) {
      sg = new SecurityGroup(subnetName, "VPC", subnetName, this);
      resources.add(sg);
    }

    return this;
  }

  /**
   * add or modify security group
   *
   * @param subnetName
   * @param fromSubnetName
   * @param protocol
   * @param ports
   * @return
   */
  public ResourceList allow(String subnetName, String fromSubnetName, String protocol, List<Integer> ports) {

    return this;
  }
}
