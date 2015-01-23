package com.sciul.cloud_configurator.services;

import com.sciul.cloud_application.dsl.Application;
import com.sciul.cloud_application.models.WebApplication;
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
  public ResourceList build(WebApplication webApplication) {
    if (webApplication.getName() == null ) webApplication.setName("dev");
    webApplication.setApiDomain("sciul.com");
    webApplication.setWebDomain("ulclearview.com");
    webApplication.setApiKey("");
    webApplication.setWebKey("");
    webApplication.setServices(new HashMap<String, String[]>() {{
      put("C*", new String[]{"9042"});
    }});

    return Application.create(webApplication);
  }
}
