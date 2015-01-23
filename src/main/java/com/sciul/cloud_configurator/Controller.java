package com.sciul.cloud_configurator;

import com.sciul.cloud_application.models.Application;
import com.sciul.cloud_configurator.services.Provider;
import com.sciul.cloud_configurator.services.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
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
  Provider provider;

  @RequestMapping("/")
  String home() {
    return "Hello World!";
  }

  @RequestMapping(name = "/template")
  String build(Application application) {
    if (application.getName() == null ) application.setName("dev");
    application.setApiDomain("sciul.com");
    application.setWebDomain("ulclearview.com");
    application.setApiKey("");
    application.setWebKey("");
    application.setDataServices(new String[] { "C*", "MQ" });

    Template template = new Template(application.getName(), application.getRegion(),
        application.getWebDomain(), application.getWebKey(),
        application.getApiDomain(), application.getApiKey(),
        application.getDataServices());

    //Template template = new Template("dev", "us-west-2", "ulclearview.com", "", "sciul.com", "", "C*");

    return provider.generateStackTemplate(template);
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Controller.class, args);
  }
}
