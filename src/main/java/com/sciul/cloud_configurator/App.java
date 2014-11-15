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
        addOption("c", "command", true, "command to run, example: update, list");
        addOption("h", "help", false, "this help message");
      }};

      final CommandLine cmd = parser.parse( options, args);

      if (cmd.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80,
            "run.sh -r <region> -c <command> [-e <environment>] [-h]",
            "Configure your cloud environments", options, "");
        System.exit(0);
      }

      if (!cmd.hasOption("c")) {
        throw new RuntimeException("no command specified");
      }

      switch(cmd.getOptionValue("c")) {
        case "update":
          logger.debug("update environment: {} for region: {}", cmd.getOptionValue("e"), cmd.getOptionValue("r"));
          break;
        case "list":
          logger.debug("list environments for region: {}", cmd.getOptionValue("r"));
          break;
      }

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
