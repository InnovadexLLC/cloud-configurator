package com.sciul.cloud_configurator.services;

import com.sciul.cloud_application.dsl.Application;
import com.sciul.cloud_application.models.CloudBlueprint;
import com.sciul.cloud_application.models.Server;
import com.sciul.cloud_configurator.dsl.ChefApiWrapper;
import com.sciul.cloud_configurator.dsl.ResourceList;
import org.jclouds.chef.ChefApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sumeetrohatgi on 1/23/15.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

  @Autowired
  private ChefApiWrapper chefApiWrapper;

  @Override
  public ResourceList build(CloudBlueprint cloudBlueprint) {
    if (cloudBlueprint.getName() == null ) cloudBlueprint.setName("dev");
    cloudBlueprint.setApiDomain("sciul.com");
    cloudBlueprint.setWebDomain("ulclearview.com");
    cloudBlueprint.setApiKey("");
    cloudBlueprint.setWebKey("");
    cloudBlueprint.setServices(new HashMap<String, Integer[]>() {{
      put("C*", new Integer[]{9042});
    }});

    logger.warn("cloudBlueprint: {}", cloudBlueprint);

    return Application.buildResourceList(cloudBlueprint);
  }

  @Override
  public List<Server> getServers(String environment) {
    return chefApiWrapper.getChefApi()
        .listNodesInEnvironment(environment)
        .stream()
        .map(name -> {Server server = new Server(); server.setName(name); return server;})
        .collect(Collectors.toList());
  }
}
