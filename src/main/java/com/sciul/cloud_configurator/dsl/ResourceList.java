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

  public static ResourceList start(String name, Map<String, String> tags) {
    ResourceList resourceList = new ResourceList();
    name = name.toUpperCase();
    resourceList.tags.put("Name", name);
    resourceList.tags.put("BelongsTo", name);
    if ( tags != null ) resourceList.tags.putAll(tags);
    return resourceList;
  }

  public Dns dns(String name, String hostedZoneName, String refer) {
    Dns dns = new Dns(name, hostedZoneName, refer, this);
    ll.add(dns);
    return dns;
  }

  public VPC vpc(String ciderBlock, String region) {
    VPC vpc = new VPC(ciderBlock, region, this);
    ll.add(vpc);
    return vpc;
  }

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
