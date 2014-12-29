package com.sciul.cloud_configurator;

import com.sciul.cloud_configurator.dsl.Application;
import com.sciul.cloud_configurator.dsl.ResourceList;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
@PropertySource("classpath:cloud-configure-app.properties")
public class Template {

  private final String webKeyFile;
  private final String apiKeyFile;
  private String env, name, region, apiDomain, webDomain;
  private String applicationName;
  private static final Map<String, Integer[]> dataServicePorts = new HashMap<String, Integer[]>() {{
    put("C*", new Integer[] {3120});
    put("MQ", new Integer[] {3120});
    put("ES", new Integer[] {3120});
  }};
  private String[] dataServices;


  public Template(String env, String region, String apiDomain, String webDomain) {
    this.env = env;
    this.name = env;
    this.region = region;
    this.apiDomain = apiDomain;
    this.webDomain = webDomain;
    webKeyFile = null;
    apiKeyFile = null;
  }

  public Template(String applicationName, String webDomain, String webKeyFile,
                  String apiDomain, String apiKeyFile, String ... dataServices) {
    this.applicationName = applicationName;
    this.apiDomain = apiDomain;
    this.webDomain = webDomain;
    this.webKeyFile = webKeyFile;
    this.apiKeyFile = apiKeyFile;
    this.dataServices = dataServices;
  }

  public ResourceList generate() {
    Application app = Application.create(applicationName)
        .httpService("APP")
        .httpService("API");

    for (String dataService : dataServices) {
      app.dataService(dataService, dataServicePorts.get(dataService));
    }

    app.proxyService("APP", webDomain, new File(webKeyFile))
        .proxyService("API", apiDomain, new File(apiKeyFile));

    return app.build(region, "10.0.0.0/16");
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

