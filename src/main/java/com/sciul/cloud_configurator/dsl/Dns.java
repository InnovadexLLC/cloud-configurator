package com.sciul.cloud_configurator.dsl;

import javax.json.JsonObject;

/**
 * Captures DNS entries required for setting up a new environment
 *
 * Created by sumeetrohatgi on 12/23/14.
 */
public class Dns extends Resource {
  private final String hostedZoneName;
  private String type = null;
  private String domain;
  private int ttl;
  private final String refer;

  public String getHostedZoneName() {
    return hostedZoneName;
  }

  public String getRefer() { return refer; }

  public String getType() {
    return type;
  }

  public String getDomain() {
    return domain;
  }

  public int getTtl() {
    return ttl;
  }

  public Dns(String name, String hostedZoneName, String refer, ResourceList resourceList) {
    String envName = resourceList.getName();

    this.hostedZoneName = hostedZoneName;

    this.refer = envName + "-" + refer + "-" + name.toUpperCase();
    this.resourceList = resourceList;
    setName("Dns" + name);
    type = "CNAME";
    String envPrefix = envName.substring(envName.indexOf('-') + 1).toLowerCase();
    this.domain = envPrefix + "-" + name.toLowerCase() + "." + hostedZoneName;
    this.ttl = 300;
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createDNS(this);
  }

  @Override
  public Dns tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

}
