package com.sciul.cloud_configurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@PropertySource("classpath:cloud-configure-app.properties")
@Configuration
@ComponentScan
public class Config {

  private static Logger logger = LoggerFactory.getLogger(Config.class);

  @Bean
  public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
    logger.debug("building placeHolderConfigurer");
    PropertySourcesPlaceholderConfigurer psc = new PropertySourcesPlaceholderConfigurer();
    psc.setIgnoreUnresolvablePlaceholders(true);
    return psc;
  }

  @Bean(name = "configure")
  public CloudConfiguratorApp configureCloud() {
    return new CloudConfiguratorImpl();
  }

}
