package com.sciul.cloud_configurator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
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
  public ChefApi buildChefApi() throws IOException {
    String client = "sumeet";
    String pemFile = System.getProperty("user.home") + "/.chef/" + client + ".pem";
    String credential = Files.toString(new File(pemFile), Charsets.UTF_8);

    ChefContext context = ContextBuilder.newBuilder("chef")
        .endpoint("http://chef.devtools.sciul.com")
        .credentials(client, credential)
        .buildView(ChefContext.class);

    // The raw API has access to all chef features, as exposed in the Chef REST API
    ChefApi api = context.unwrapApi(ChefApi.class);
    return api;
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
