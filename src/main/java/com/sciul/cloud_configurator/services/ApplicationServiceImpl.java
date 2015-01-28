package com.sciul.cloud_configurator.services;

import com.sciul.cloud_application.dsl.Application;
import com.sciul.cloud_application.models.CloudBlueprint;
import com.sciul.cloud_configurator.dsl.ResourceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by sumeetrohatgi on 1/23/15.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

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
}
