package com.sciul.cloud_configurator;

import com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl.ResourceList;
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
    return ResourceList.start()
         .dns("sciul.com.").recordSetA("sciul.com.")
         .next()
         .dns("sciul.com.").recordSetCNAME("api.sciul.com.")
         .next()
         .vpc("10.0.0.0/16")
         .next()
         .end();
  }

  public String getName() {
    return name;
  }
}

