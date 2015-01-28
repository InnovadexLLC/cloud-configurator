package com.sciul.cloud_configurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by sumeetrohatgi on 1/27/15.
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class Application {
  private static Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws Exception {
    ApplicationContext ctx = SpringApplication.run(Application.class, args);

    Arrays
        .asList(ctx.getBeanDefinitionNames())
        .stream()
        .sorted()
        .forEach(beanName -> logger.debug("injected bean: {}", beanName));
  }

}
