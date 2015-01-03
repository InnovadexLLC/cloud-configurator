package com.sciul.cloud_application.dsl;

import com.sciul.cloud_configurator.CidrUtils;
import com.sciul.cloud_configurator.dsl.ResourceList;

import java.io.File;
import java.util.ArrayList;
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
  private List<Service> services = new ArrayList<>();

  private final String name;

  private Application(String name) {
    this.name = name;
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
    for (Service s : services) {
      if (s.getName().equals(httpAppName) && s instanceof HttpService) {
        ProxyService proxyService = new ProxyService();

        proxyService.setHttpService((HttpService)s);
        proxyService.setDomain(domain);
        proxyService.setCertificateKeyFile(certificateKeyFile);

        services.add(proxyService);
        return this;
      }
    }

    throw new IllegalStateException("please define httpAppName first");
  }

  /**
   * a new data service
   *
   * @param dataServiceName
   * @param inPorts
   * @return
   */
  public Application dataService(String dataServiceName, Integer[] inPorts) {
    DataService dataService = new DataService();

    dataService.setName(dataServiceName);
    for (Integer port : inPorts) {
      dataService.addPortExternal("tcp", port);
    }

    services.add(dataService);

    return this;
  }

  /**
   * dominant type of services available for the cloud today.
   * @param httpServiceName
   * @param externalPort
   * @return
   */
  public Application httpService(String httpServiceName, Integer externalPort) {
    HttpService httpService = new HttpService();

    httpService.setName(httpServiceName);
    httpService.addPortExternal("tcp", externalPort);

    services.add(httpService);
    return this;
  }

  /**
   * provides the ResourceList that is fed into a cloud provider
   * for configuring the given application.
   *
   * @return
   */
  public ResourceList build(String region) {
    return build(region, CidrUtils.build(16).toString());
  }

  ResourceList build(String region, String cidrBlock) {
    ResourceList resourceList = ResourceList.start(name);

    for (Service s : services) {
      if (s instanceof ProxyService) {
        ProxyService p = (ProxyService) s;
        resourceList.dns("ELB", p.getHttpService().getName(), p.getDomain());
      }
    }

    String zoneA = region + "a";
    String zoneB = region + "b";

    resourceList.vpc(cidrBlock, region);

    CidrUtils cidrUtils = CidrUtils.build(cidrBlock).changeMask(24);

    //.subnet("ELB", "10.0.12.0/24", zoneA, zoneB)
    resourceList.subnet("ELB", cidrUtils, true, zoneA, zoneB);

    resourceList
        .subnet("APP", cidrUtils, zoneA, zoneB)     // all applications get their own subnet
        .subnet("DBS", cidrUtils, zoneA, zoneB)     // a database layer
        .subnet("OPS", cidrUtils, true, zoneB)       // an operations endpoint
/*        .allow("ELB", true)
        .allow("APP", false, "ELB", httpPorts)
        .allow("DBS", false, "APP", dbPorts)
        .allow("OPS", true)*/
        .end();


    return resourceList;
  }

}
