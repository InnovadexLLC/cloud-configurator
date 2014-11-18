package com.sciul.cloud_configurator;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.cloudformation.model.DescribeStacksResult;

public class CloudConfiguratorImpl implements CloudConfiguratorApp {

  @Autowired
  Properties prop;

  @Autowired
  AwsProvider provider;

  private static Logger logger = LoggerFactory.getLogger(CloudConfiguratorImpl.class);

  private String region;
  private CommandLineParser parser = new BasicParser();

  @Override
  public void configure(String... args) {
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

      provider.setRegion(cmd.getOptionValue("r"));

      switch (cmd.getOptionValue("c")) {
        case "update":
          logger.debug("update environment: {} for region: {}", cmd.getOptionValue("e"), cmd.getOptionValue("r"));
          provider.createStack(cmd.getOptionValue("e"));
          logger.debug("****************Stack Creation Started****************");
          break;
        case "list":
          logger.debug("list environments for region: {}", cmd.getOptionValue("r"));
          DescribeStacksResult dst = null;
          if (cmd.getOptionValue("e") != null && !cmd.getOptionValue("e").equalsIgnoreCase("")) {
            dst = provider.describeStacks(cmd.getOptionValue("e"));
          } else {
            dst = provider.describeStacks(null);
          }
          logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
          logger.debug("dst: {}", dst);
          logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
          break;
      }

    } catch (ParseException | RuntimeException e) {
      logger.error("Unable to create VPC", e);
      System.exit(1);
    }
  }

}
