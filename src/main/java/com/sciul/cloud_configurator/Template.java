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

    String envPrefix = env.substring(env.indexOf('-') + 1) + "-";

    return ResourceList
        .start(name, null)
        .dns("APP", webDomain, "ELB")
        .next()
        .dns("API", apiDomain, "ELB")
        .next()
        .vpc("10.0.0.0/16", region)
        .subnet("ELB", zoneA, "10.0.12.0/24")
        .subnet("ELB", zoneB, "10.0.13.0/24")
        .subnet("APP", zoneA, "10.0.51.0/24")
        .subnet("APP", zoneB, "10.0.52.0/24")
        .subnet("DB", zoneA, "10.0.91.0/24")
        .subnet("DB", zoneB, "10.0.92.0/24")
        .subnet("NAT", zoneB, "10.0.0.0/24")
        .next()
        .end();
  }

  public String getName() {
    return name;
  }
}

