package com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl;


import java.util.ArrayList;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public class ResourceList {
  private ArrayList<Resource> ll = new ArrayList<>();

  public static ResourceList start() {
    return new ResourceList();
  }

  public Dns dns(String hostedZoneName) {
    Dns dns = new Dns(hostedZoneName, this);
    ll.add(dns);
    return dns;
  }

  public VPC vpc(String ciderBlock) {
    VPC vpc = new VPC(ciderBlock, this);
    ll.add(vpc);
    return vpc;
  }

  public ResourceList end() {
    return this;
  }

  public String toJson() {
    return "";
  }
}
