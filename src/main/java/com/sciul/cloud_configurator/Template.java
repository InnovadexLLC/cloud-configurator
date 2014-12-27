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
        .start(name)
        .dns("ELB", "APP", webDomain)
        .dns("ELB", "API", apiDomain)
        .vpc("10.0.0.0/16", region)
        .subnet("ELB", "10.0.12.0/24", zoneA, zoneB)
        .subnet("APP", "10.0.51.0/24", zoneA, zoneB)
        .subnet("DB", "10.0.91.0/24", zoneA, zoneB)
        .subnet("NAT", "10.0.0.0/24", true, zoneB)
        .end();
  }

  public String getName() {
    return name;
  }
}

