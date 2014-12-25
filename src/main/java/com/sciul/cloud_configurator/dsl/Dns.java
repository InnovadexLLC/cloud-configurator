package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Captures DNS entries required for setting up a new environment
 *
 * Created by sumeetrohatgi on 12/23/14.
 */
public class Dns extends Resource {
  private final String hostedZoneName;
  private RecordSets recordSets = new RecordSets();
  private String type = null;
  private String domain;
  private int ttl;
  private final String refer;

  public String getHostedZoneName() {
    return hostedZoneName;
  }

  public String getRefer() { return refer; }

  public RecordSets getRecordSets() {
    return recordSets;
  }

  public void setRecordSets(RecordSets recordSets) {
    this.recordSets = recordSets;
  }

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
    String envName = resourceList.tags.get("Name");

    this.hostedZoneName = hostedZoneName;

    this.refer = envName + "-" + refer + "-" + name.toUpperCase();
    this.resourceList = resourceList;
    setName("DNS-" + name);
    type = "CNAME";
    String envPrefix = envName.substring(envName.indexOf('-') + 1).toLowerCase();
    this.domain = envPrefix + "-" + name.toLowerCase() + "." + hostedZoneName;
    this.ttl = 300;
  }

  public ResourceList next() {
    return resourceList;
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
