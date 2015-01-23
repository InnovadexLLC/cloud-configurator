package com.sciul.cloud_configurator;

import com.sciul.cloud_application.models.WebApplication;
import com.sciul.cloud_configurator.dsl.Provider;
import com.sciul.cloud_configurator.services.TemplateService;
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
  private TemplateService templateService;

  @Autowired
  private Provider provider;

  @RequestMapping("/")
  String home() {
    return "Hello World!";
  }

  @RequestMapping(name = "/template", method = RequestMethod.PUT)
  String build(@RequestBody WebApplication webApplication) {
    return provider.generateStackTemplate(templateService.build(webApplication));
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Controller.class, args);
  }
}
