package com.sciul.cloud_configurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
@PropertySource("classpath:cloud-configure-app.properties")
public class App {

  private static Logger logger = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    try {

      ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
      CloudConfiguratorApp obj = (CloudConfiguratorApp) context.getBean("configure");

      obj.configure(args);

    } catch (Exception e) {
      logger.error("Error running Configurator App", e);
      System.exit(1);
    }
  }

  @Bean(name = "configure")
  public CloudConfiguratorApp configureCloud() {
    return new CloudConfiguratorApp();
  }

}
