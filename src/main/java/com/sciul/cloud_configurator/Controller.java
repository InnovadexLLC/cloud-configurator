package com.sciul.cloud_configurator;

import com.sciul.cloud_application.models.Cloud;
import com.sciul.cloud_application.models.CloudBlueprint;
import com.sciul.cloud_configurator.services.ApplicationService;
import com.sciul.cloud_configurator.services.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sumeetrohatgi on 1/22/15.
 */
@RestController
@EnableAutoConfiguration
@Configuration
@ComponentScan
@PropertySource("classpath:cloud-configure-app.properties")
public class Controller {

  @Autowired
  private ApplicationService applicationService;

  @Autowired
  private ProviderService providerService;

  @RequestMapping(name = "/template", method = RequestMethod.PUT)
  String generate(@RequestBody CloudBlueprint cloudBlueprint) {
    return providerService.template(applicationService.build(cloudBlueprint));
  }

  @RequestMapping(name = "/cloud", method = RequestMethod.POST)
  Cloud apply(@RequestBody CloudBlueprint cloudBlueprint) {
    return providerService.apply(applicationService.build(cloudBlueprint));
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Controller.class, args);
  }
}
