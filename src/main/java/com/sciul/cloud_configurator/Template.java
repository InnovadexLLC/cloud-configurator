package com.sciul.cloud_configurator;

import com.sciul.cloud_configurator.dsl.ResourceList;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
@PropertySource("classpath:cloud-configure-app.properties")
public class Template {

  private String env, name, region, domain;

  public Template(String env, String region, String domain) {
    this.env = env;
    this.name = env;
    this.region = region;
    this.domain = domain;
  }

  public ResourceList generateResourceList() {
    return ResourceList
        .start("SCI-QA", null)
          .dns("ulclearview.com")
          .recordSetCNAME("qa-apps.ulclearview.com")
        .next()
          .dns("sciul.com.")
          .recordSetCNAME("qa-api.sciul.com")
        .next()
          .vpc("10.0.0.0/16")
          .subnet("us-west-2a", "10.0.12.0/24")
          .subnet("us-west-2b", "10.0.13.0/24")
          .subnet("us-west-2a", "10.0.51.0/24")
          .subnet("us-west-2b", "10.0.52.0/24")
        .next()
        .end();
  }

  public String getName() {
    return name;
  }
}

