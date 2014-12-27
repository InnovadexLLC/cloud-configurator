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
  private ArrayList<Resource> ll = new ArrayList<>();
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
    ll.add(dns);
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
    ll.add(vpc);
    return this;
  }

  public ResourceList subnet(String name, String ciderBlock, String ... zone) {
    return subnet(name, ciderBlock, false, zone);
  }

  /**
   * define a subnet on a VPC
   * @param name
   * @param ciderBlock
   * @param isPublicConnected
   * @param zone
   * @return
   */
  public ResourceList subnet(String name, String ciderBlock, boolean isPublicConnected, String ... zone) {
    for (String z : zone) {
      Subnet subnet = new Subnet(name, ciderBlock, z, isPublicConnected, getName() + "-VPC", this);
      ciderBlock = incrementCidrBlock(ciderBlock);
      ll.add(subnet);
    }
    return this;
  }

  private String incrementCidrBlock(String cidrBlock) {
    String[] components = cidrBlock.split("\\.|/");
    if (components.length != 5) {
      logger.debug("components: {}, length: {}", (String[])components, components.length);
      throw new RuntimeException("incorrect cidrBlock passed in, unable to parse!");
    }

    int index = Integer.parseInt(components[4]) / 8 - 1;
    components[index] = String.valueOf(Integer.parseInt(components[index]) + 1);
    return String.format("%s.%s.%s.%s/%s", components[0], components[1], components[2], components[3], components[4]);
  }

  /**
   * define a new load balancer
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
    return ll;
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
    ll.add(resource);
  }

}
