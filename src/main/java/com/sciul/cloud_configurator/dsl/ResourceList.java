package com.sciul.cloud_configurator.dsl;


import java.util.ArrayList;
import java.util.List;

/**
 * Captures cloud building intent in as neutral terms
 * as possible.
 *
 * Created by sumeetrohatgi on 12/23/14.
 */
public class ResourceList {
  private ArrayList<Resource> ll = new ArrayList<>();

  private ResourceList() {}

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

  public Subnet subnet(String cidrBlock, String availabilityZone) {
    Subnet sn = new Subnet(cidrBlock, availabilityZone, this);
    ll.add(sn);
    return sn;
  }

  public ResourceList end() {
    return this;
  }

  public List<Resource> resources() {
    return ll;
  }

}
