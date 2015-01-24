package com.sciul.cloud_application.dsl;

import com.sciul.cloud_application.models.CloudBlueprint;
import com.sciul.cloud_configurator.dsl.CidrUtils;
import com.sciul.cloud_configurator.dsl.ResourceList;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

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

    List<ProxyService> proxyServiceList = services
        .stream()
        .filter(s -> s.getName().equals(httpAppName) && s instanceof HttpService)
        .map(h -> {
          ProxyService proxyService1 = new ProxyService();

          proxyService1.setHttpService((HttpService) h);
          proxyService1.setName("PROXY-"+httpAppName);
          proxyService1.setDomain(domain);
          proxyService1.setCertificateKeyFile(certificateKeyFile);

          return proxyService1;
        })
        .collect(Collectors.toList());

    if (proxyServiceList.size() == 0)
      throw new IllegalStateException("please define httpAppName first");

    services.addAll(proxyServiceList);

    return this;
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

    Arrays.asList(inPorts)
        .stream()
        .forEach(port -> dataService.addPortExternal("tcp", port));

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


  private ResourceList build(String region, String cidrBlock) {
    ResourceList resourceList = ResourceList.start(name);

    List<Integer> dbPortsWithin = new ArrayList<>();
    List<Integer> dbPortsExternal = new ArrayList<>();
    List<Integer> httpPortsExternal = new ArrayList<>();

    services.stream()
        .forEach(s -> {
          if (s instanceof ProxyService) {
            ProxyService p = (ProxyService) s;
            resourceList.dns("ELB", p.getHttpService().getName(), p.getDomain());
          }

          if (s instanceof DataService) {
            dbPortsExternal.addAll(s.portListExternal("tcp"));
            dbPortsWithin.addAll(s.portListWithin("tcp"));
          }

          if (s instanceof HttpService) {
            httpPortsExternal.addAll(s.portListExternal("tcp"));
          }
        });

    String zoneA = region + "a";
    String zoneB = region + "b";

    resourceList.vpc(cidrBlock, region);

    CidrUtils cidrUtils = CidrUtils.build(cidrBlock).changeMask(24);

    resourceList.subnet("ELB", cidrUtils, true, zoneA, zoneB);

    final boolean publicAccess = true;

    resourceList
        .subnet("APP", cidrUtils, zoneA, zoneB)      // all applications get their own subnet
        .subnet("DBS", cidrUtils, zoneA, zoneB)      // a database layer
        .subnet("OPS", cidrUtils, true, zoneB)       // an operations endpoint
        .group("ELB", "ELB", "")
        .addRule("tcp", "0", "0", "ELB")
        .allow("ELB", publicAccess, "tcp", Collections.<Integer>emptyList())
        .allow("APP", "ELB", "tcp", httpPortsExternal)
        .allow("DBS", "DBS", "tcp", dbPortsWithin)
        .allow("DBS", "APP", "tcp", dbPortsExternal)
        .allow("OPS", publicAccess, "tcp", new ArrayList<Integer>() {{ add(22); }})
        .end();

    return resourceList;
  }

  public static ResourceList buildResourceList(CloudBlueprint cloudBlueprint) {
    Application application = Application.create(cloudBlueprint.getName())
        .httpService("APP", 3000)
        .httpService("API", 8080)
        .dataService("C*", new Integer[]{3120})
        .dataService("RMQ", new Integer[]{2333})
        .dataService("ES", new Integer[] {2322})
        .proxyService("APP", cloudBlueprint.getWebDomain(), new File(cloudBlueprint.getWebKey()))
        .proxyService("API", cloudBlueprint.getApiDomain(), new File(cloudBlueprint.getApiKey()));

    return application.build(cloudBlueprint.getRegion());
  }

}
