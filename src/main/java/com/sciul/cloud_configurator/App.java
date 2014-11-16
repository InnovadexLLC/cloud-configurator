package com.sciul.cloud_configurator;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.amazonaws.services.cloudformation.model.DescribeStacksResult;

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

  public App() {
  }

  public App(Provider provider) {
    this.provider = provider;
  }

  public static void main(String[] args) {
    try {

      Options options = new Options() {
        {
          addOption("r", "region", true, "aws region, example: us-west-2");
          addOption("e", "environment", true, "engineering environment, example: prod, dev, qa");
          addOption("c", "command", true, "command to run, example: update, list");
          addOption("h", "help", false, "this help message");
        }
      };

      final CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, "run.sh -r <region> -c <command> [-e <environment>] [-h]",
              "Configure your cloud environments", options, "");
        System.exit(0);
      }

      if (!cmd.hasOption("c")) {
        throw new RuntimeException("no command specified");
      }

      AwsProvider provider = new AwsProvider() {
        {
          setRegion(cmd.getOptionValue("r"));
        }
      };

      App app = new App(provider);

      switch (cmd.getOptionValue("c")) {
        case "update":
          logger.debug("update environment: {} for region: {}", cmd.getOptionValue("e"), cmd.getOptionValue("r"));
          app.getProvider().createStack(cmd.getOptionValue("e"));
          break;
        case "list":
          logger.debug("list environments for region: {}", cmd.getOptionValue("r"));
          if (cmd.getOptionValue("e") != null && !cmd.getOptionValue("e").equalsIgnoreCase("")) {
            DescribeStacksResult dst = app.getProvider().describeStacks(cmd.getOptionValue("e"));
            logger.debug("dst: {}", dst);
          } else {
            DescribeStacksResult dst = app.getProvider().describeStacks(null);
            logger.debug("dst: {}", dst);
          }
          break;
      }

    } catch (RuntimeException e) {
      logger.error("Error running Configurator App", e);
      System.exit(1);
    } catch (ParseException e) {
      logger.error("Error parsing Configurator App options", e);
      System.exit(1);
    }
  }
}
