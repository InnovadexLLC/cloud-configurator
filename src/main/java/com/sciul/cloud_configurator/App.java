package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.io.*;

@Configuration
@ComponentScan
@PropertySource("classpath:cloud-configure-app.properties")
public class App {

  private static Logger logger = LoggerFactory.getLogger(App.class);

  private CommandLineParser parser = new BasicParser();

  @Autowired
  AwsProvider provider;

  public static void main(String[] args) {
    try {

      ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
      App obj = (App) context.getBean("configure");

      obj.configure(args);

    } catch (Exception e) {
      logger.error("Error running Configurator App", e);
      System.exit(1);
    }
  }

  @Bean(name = "configure")
  public App myApp() {
    return new App();
  }

  public void configure(String... args) {
    try {

      Options options = new Options() {
        {
          addOption("c", "command", true, "command to run, example: update, list");
          addOption("r", "region", true, "aws region");
          addOption("f", "file", true, "file to read input/ write output");
          addOption("h", "help", false, "this help message");
        }
      };

      final CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, "run.sh -c <command> -c <update|list|generate> [-f <filename>]-r <region> [-h]",
            "Configure your cloud environment", options, "");
        return;
      }

      if (!cmd.hasOption("c")) {
        throw new RuntimeException("no command specified");
      }

      if (!cmd.hasOption("r")) {
        throw new RuntimeException("no region specified");
      }

      provider.setRegion(cmd.getOptionValue("r"));

      switch (cmd.getOptionValue("c")) {
        case "generate":
          String template = provider.generateStackTemplate(new Template("SCI-QA", "us-west-2","sciul.com"));
          if (!cmd.hasOption("f")) {
            logger.debug("generated template: {}", template);
            break;
          }

          Writer writer = null;

          try {
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(cmd.getOptionValue("f")), "utf-8"));
            writer.write(template);
          } catch (IOException ex) {
            throw new RuntimeException("unable to write to file");
          } finally {
            try {
              writer.close();
              logger.info("wrote template to file: {}", cmd.getOptionValue("f"));
            } catch (Exception ex) {
              throw new RuntimeException("unable to write to file");
            }
          }
          break;

        case "update":
          provider.createStack(new Template("SCI-QA", "us-west-2","sciul.com"));
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

    } catch (ParseException e) {
      throw new RuntimeException("Unable to parse command line options", e);
    }
  }

}
