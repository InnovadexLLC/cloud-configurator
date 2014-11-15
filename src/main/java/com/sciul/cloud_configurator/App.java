package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.ListStacksResult;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("cloud-configure-app.properties")
public class App {

  private Provider provider;
  private String request;
  private static CommandLineParser parser = new BasicParser();
  private static Logger logger = LoggerFactory.getLogger(App.class);

  public Provider getProvider() {
    return provider;
  }

  public void setProvider(Provider provider) {
    this.provider = provider;
  }

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  public App() {  }
  
  public App(Provider provider) {
    this.provider = provider;
  }


  public static void main(String[] args) {
    try {

      Options options = new Options() {{
        addOption("r", "region", true, "aws region, example: us-west-2");
        addOption("e", "environment", true, "engineering environment, example: prod, dev, qa");
      }};

      final CommandLine cmd = parser.parse( options, args);

      AwsProvider provider = new AwsProvider() {{
        setRegion(cmd.getOptionValue("r"));
      }};

      App app = new App(provider);

      app.getProvider().createStack(cmd.getOptionValue("e"));

      DescribeStacksResult dst = app.getProvider().describeStacks(args);

      logger.debug("dst: {}", dst);

    } catch (RuntimeException e) {
      logger.error("Error running Configurator App", e);
      System.exit(1);
    } catch (ParseException e) {
      logger.error("Error parsing Configurator App options", e);
    }
  }
}
