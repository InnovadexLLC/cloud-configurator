package com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl;

import com.amazonaws.util.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public class Dns implements Resource {
  private final String hostedZoneName;
  private RecordSets recordSets = new RecordSets();
  private String type = null;
  private String domain = "";
  private int ttl;
  private ResourceList resourceList;

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
  public JsonObject toJson() {
    return
        Json.createObjectBuilder()
            .add("HostedZoneName", hostedZoneName)
            .add("RecordSets",
                Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                             .add("Name",domain)
                             .add("Type", type)
                             .add("TTL",ttl)
                    )
             )
            .build();
  }

}
