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
    this.hostedZoneName = hostedZoneName;

    this.refer = refer;
    setName(resourceList.tags.get("Name") + "-DNS-" + name);
    this.resourceList = resourceList;
    recordSets.setResourceList(resourceList);
  }

  public RecordSets recordSetA(String domain) {
    return recordSetA(domain, 300);
  }

  public RecordSets recordSetA(String domain, int ttl) {
    type = "A";
    this.domain = domain;
    this.ttl = ttl;
    return recordSets;
  }

  public RecordSets recordSetCNAME(String domain) {
    return recordSetCNAME(domain, 300);
  }

  public RecordSets recordSetCNAME(String domain, int ttl) {
    type = "CNAME";
    this.domain = domain;
    this.ttl = ttl;
    return recordSets;
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
