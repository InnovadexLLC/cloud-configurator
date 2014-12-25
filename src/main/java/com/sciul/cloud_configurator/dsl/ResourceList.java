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
    resourceList.tags.put("Name", name);
    resourceList.tags.put("BelongsTo", name);
    if ( tags != null ) resourceList.tags.putAll(tags);
    return resourceList;
  }

  public Dns dns(String name, String hostedZoneName) {
    Dns dns = new Dns(name, hostedZoneName, this);
    ll.add(dns);
    return dns;
  }

  public VPC vpc(String name, String ciderBlock) {
    VPC vpc = new VPC(name, ciderBlock, this);
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
