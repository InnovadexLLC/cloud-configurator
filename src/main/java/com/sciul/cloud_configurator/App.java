package com.sciul.cloud_configurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("cloud-configure-app.properties")
public class App {

  private static Logger logger = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    try {

      ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
      CloudConfiguratorApp obj = (CloudConfiguratorApp) context.getBean("configure");

      obj.configure(args);

    } catch (Exception e) {
      logger.error("Error running Configurator App", e);
      System.exit(1);
    }
  }
}
