package com.sciul.cloud_configurator.dsl;


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
  Map<String, String> tags = new HashMap<>();

  private ResourceList() {}

  public String getName() {
    return tags.get("Name");
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
   * @param refer unique name referred to in template (typically load balancer)
   * @return
   */
  public Dns dns(String name, String hostedZoneName, String refer) {
    Dns dns = new Dns(name, hostedZoneName, refer, this);
    ll.add(dns);
    return dns;
  }

  /**
   * define a vpc, only one per template for now
   *
   * @param ciderBlock example: 10.0.0.0/16
   * @param region example: us-west-2
   * @return
   */
  public VPC vpc(String ciderBlock, String region) {
    VPC vpc = new VPC(ciderBlock, region, this);
    ll.add(vpc);
    return vpc;
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

  void add(Resource resource) {
    ll.add(resource);
  }

}
