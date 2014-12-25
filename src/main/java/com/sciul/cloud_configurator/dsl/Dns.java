package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public class Dns extends Resource {
  private final String hostedZoneName;
  private RecordSets recordSets = new RecordSets();
  private String type = null;
  private String domain = "";
  private int ttl;

  public String getHostedZoneName() {
    return hostedZoneName;
  }

  public RecordSets getRecordSets() {
    return recordSets;
  }

  public void setRecordSets(RecordSets recordSets) {
    this.recordSets = recordSets;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public int getTtl() {
    return ttl;
  }

  public void setTtl(int ttl) {
    this.ttl = ttl;
  }

  public Dns(String hostedZoneName, ResourceList resourceList) {
    if (hostedZoneName.endsWith(".")) {
      this.hostedZoneName = hostedZoneName;
    } else {
      this.hostedZoneName = hostedZoneName + ".";
    }

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
