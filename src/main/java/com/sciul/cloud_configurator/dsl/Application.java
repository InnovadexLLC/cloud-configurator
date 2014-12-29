package com.sciul.cloud_configurator.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Captures a modern web application's configuration
 * requirements. This in turn feeds into an abstraction
 * for a cloud environment.
 *
 * Created by sumeetrohatgi on 12/28/14.
 */
public class Application {
  private ResourceList resourceList;

  private List<ProxyService> proxyServices;
  private Map<String, HttpService> httpServices;
  private List<DataService> dataServices;

  private final String name;

  private Application(String name) {
    this.name = name;
    proxyServices = new ArrayList<>();
    httpServices = new HashMap<>();
    dataServices = new ArrayList<>();
  }

  public static Application create(String name) {
    Application application = new Application(name);

    return application;
  }

  /**
   * proxies load balance and can optionally
   * cache results.
   *
   * @param httpAppName
   * @param domain
   * @param certificateKeyFile
   * @return
   */
  public Application proxyService(String httpAppName, String domain, File certificateKeyFile) {
    ProxyService proxyService = new ProxyService();
    HttpService httpService = httpServices.get(httpAppName);

    if (httpService == null) {
      throw new RuntimeException("please define httpAppName first");
    }

    proxyService.setHttpService(httpService);
    proxyService.setDomain(domain);
    proxyService.setCertificateKeyFile(certificateKeyFile);

    proxyServices.add(proxyService);
    return this;
  }

  public Application dataService(String dataServiceName, Integer[] inPorts) {
    DataService dataService = new DataService();

    dataService.setName(dataServiceName);
    dataService.setPorts(inPorts);

    dataServices.add(dataService);
    return this;
  }

  /**
   * dominant type of services available for the cloud today.
   *
   * @param httpServiceName
   * @return
   */
  public Application httpService(String httpServiceName) {
    HttpService httpService = new HttpService();

    httpService.setName(httpServiceName);

    httpServices.put(httpServiceName, httpService);
    return this;
  }

  /**
   * provides the ResourceList that is fed into a cloud provider
   * for configuring the given application.
   *
   * @return
   */
  public ResourceList build(String region, String cidrBlock) {
    ResourceList resourceList = ResourceList.start(name);

    for (ProxyService p : proxyServices) {
      resourceList.dns("ELB", p.getHttpService().getName(), p.getDomain());
    }

    String zoneA = region + "a";
    String zoneB = region + "b";

    resourceList.vpc(cidrBlock, region);

    //.subnet("ELB", "10.0.12.0/24", zoneA, zoneB)
    for (ProxyService p : proxyServices) {
      resourceList.subnet("ELB", cidrBlock, true, zoneA, zoneB);
    }

    resourceList
        .subnet("APP", "10.0.51.0/24", zoneA, zoneB)     // all applications get their own subnet
        .subnet("DBS", "10.0.91.0/24", zoneA, zoneB)     // a database layer
        .subnet("OPS", "10.0.0.0/24", true, zoneB)       // an operations endpoint
        .end();

    return resourceList;
  }

}
