package com.sciul.cloud_configurator;

import com.sciul.cloud_configurator.dsl.ResourceList;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
@PropertySource("classpath:cloud-configure-app.properties")
public class Template {

  private String env, name, region, apiDomain, webDomain;

  public Template(String env, String region, String apiDomain, String webDomain) {
    this.env = env;
    this.name = env;
    this.region = region;
    this.apiDomain = apiDomain;
    this.webDomain = webDomain;
  }

  public ResourceList generateResourceList() {
    String zoneA = region + "a";
    String zoneB = region + "b";

    return ResourceList
        .start(name, null)
        .dns("DNS-APP", webDomain, "APP-ELB")
        .recordSetCNAME("qa-apps." + webDomain)
        .next()
        .dns("DNS-API", apiDomain, "API-ELB")
        .recordSetCNAME("qa-api." + apiDomain)
        .next()
        .vpc("VPC", "10.0.0.0/16")
        .subnet("ELB-2A", zoneA, "10.0.12.0/24")
        .subnet("ELB-2B", zoneB, "10.0.13.0/24")
        .subnet("APP-2A", zoneA, "10.0.51.0/24")
        .subnet("APP-2B", zoneB, "10.0.52.0/24")
        .subnet("DB-2A", zoneA, "10.0.91.0/24")
        .subnet("DB-2B", zoneB, "10.0.92.0/24")
        .next()
        .end();
  }

  public String getName() {
    return name;
  }
}

