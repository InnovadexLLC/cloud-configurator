package com.sciul.cloud_configurator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sciul.cloud_configurator.dsl.ChefApiWrapper;
import org.jclouds.ContextBuilder;
import org.jclouds.chef.ChefApi;
import org.jclouds.chef.ChefContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by sumeetrohatgi on 1/27/15.
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = "com.sciul")
public class Application {
  private static Logger logger = LoggerFactory.getLogger(Application.class);

  @Bean
  public ChefApiWrapper buildChefApi() {
    String client = "sumeet";
    String pemFile = System.getProperty("user.home") + "/.chef/" + client + ".pem";
    String chefServerUrl = "http://chef.devtools.sciul.com";

    ChefApiWrapper chefApiWrapper = new ChefApiWrapper();
    chefApiWrapper.setChefServerUrl(chefServerUrl);
    chefApiWrapper.setClient(client);
    chefApiWrapper.setPemFile(pemFile);

    return chefApiWrapper;
  }

  public static void main(String[] args) throws Exception {
    ApplicationContext ctx = SpringApplication.run(Application.class, args);

    Arrays
        .asList(ctx.getBeanDefinitionNames())
        .stream()
        .sorted()
        .forEach(beanName -> logger.debug("injected bean: {}", beanName));
  }

}
