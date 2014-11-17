package com.sciul.cloud_configurator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.CreateStackRequest;
import com.amazonaws.services.cloudformation.model.CreateStackResult;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.ListStacksRequest;
import com.amazonaws.services.cloudformation.model.ListStacksResult;

public class CloudConfiguratorImpl implements CloudConfiguratorApp, Provider {

  @Autowired
  Properties prop;

  private static final String ACCESS_KEY = "AWS_ACCESS_KEY";
  private static final String SECRET_KEY = "AWS_SECRET_KEY";

  private static Logger logger = LoggerFactory.getLogger(CloudConfiguratorImpl.class);

  private AWSCredentials credentials;
  private AmazonCloudFormationClient clt;
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

      inti();
      setRegion(cmd.getOptionValue("r"));

      switch (cmd.getOptionValue("c")) {
        case "update":
          logger.debug("update environment: {} for region: {}", cmd.getOptionValue("e"), cmd.getOptionValue("r"));
          createStack(cmd.getOptionValue("e"));
          logger.debug("****************Stack Creation Started****************");
          break;
        case "list":
          logger.debug("list environments for region: {}", cmd.getOptionValue("r"));
          DescribeStacksResult dst = null;
          if (cmd.getOptionValue("e") != null && !cmd.getOptionValue("e").equalsIgnoreCase("")) {
            dst = describeStacks(cmd.getOptionValue("e"));
          } else {
            dst = describeStacks(null);
          }
          logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
          logger.debug("dst: {}", dst);
          logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
          break;
      }

    } catch (Exception e) {

    }
  }

  public void inti() {
    try {
      credentials = new ProfileCredentialsProvider().getCredentials();
      clt = new AmazonCloudFormationClient(credentials);
    } catch (Exception e) {
      throw new RuntimeException("Cannot load the credentials from the credential profiles file. "
            + "Please make sure that your credentials file is at the correct "
            + "location (~/.aws/credentials), and is in valid format.", e);
    }
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    if (region == null) {
      throw new RuntimeException("illegal region value: " + region);
    }
    this.region = region;
    Regions r = Regions.DEFAULT_REGION;

    switch (region) {
      case "us-west-2":
        r = Regions.US_WEST_2;
        break;
      default:
        throw new RuntimeException("illegal region: " + region);
    }

    clt.setRegion(Region.getRegion(Regions.US_WEST_2));

  }

  public CreateStackResult createStack(final String environment) {
    if (environment == null || environment.length() == 0) {
      throw new RuntimeException("illegal environment name: " + environment);
    }
    try {
      final String template =
            convertStreamToString(CloudConfiguratorImpl.class.getResourceAsStream("/templates/cf-template-1.json"));
      CreateStackRequest crq = new CreateStackRequest() {
        {
          setStackName(environment);
          setTemplateBody(template);
        }
      };

      logger.debug("creating a new stack named: {}", environment);
      CreateStackResult crs = clt.createStack(crq);

      logger.debug("stack create result: {}", crs);
      return crs;
    } catch (AmazonServiceException ae) {
      throw new RuntimeException("server error", ae);
    } catch (AmazonClientException ae) {
      throw new RuntimeException("client error", ae);
    }
  }

  public DescribeStacksResult describeStacks(String environment) {
    DescribeStacksRequest d = new DescribeStacksRequest();
    if (environment != null && !environment.equalsIgnoreCase("")) {
      d.setStackName(environment);
    }
    return clt.describeStacks(d);
  }

  public ListStacksResult listStacks(String[] args) {
    ListStacksRequest rq = new ListStacksRequest();
    rq.setStackStatusFilters(new ArrayList<String>() {
      {
        add("CREATE_COMPLETE");
      }
    });
    ListStacksResult lst = clt.listStacks(rq);
    return lst;
  }

  public String convertStreamToString(InputStream in) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    StringBuilder stringbuilder = new StringBuilder();
    String line = null;
    try {
      while ((line = reader.readLine()) != null) {

        line = line.replaceAll("%ENV%", prop.getVpcNameing());
        line = line.replaceAll("%IP_BLOCK%", prop.getIpBlock());
        line = line.replaceAll("%IP_SUBNET1%", prop.getIpSubnet1());
        line = line.replaceAll("%IP_SUBNET2%", prop.getIpSubnet2());
        line = line.replaceAll("%DELETE_PROTECTION%", prop.getDeleteProtection());

        stringbuilder.append(line + "\n");
      }
      in.close();
    } catch (IOException e) {
      throw new RuntimeException("unable to read file!", e);
    }
    return stringbuilder.toString();
  }

  public void processJson(String[] args) {
    if (args.length != 1) {
      throw new RuntimeException("Usage: processJson <path to file>");
    }

    String path = args[0];

    CreateStackRequest crt = new CreateStackRequest();
    String template = "";
    try {
      List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
      StringBuffer sbf = new StringBuffer();
      for (String l : lines) {
        sbf.append(l);
      }
      template = sbf.toString();
      if (template.length() == 0) {
        throw new RuntimeException("zero length file read from path: " + path);
      }
    } catch (IOException e) {
      throw new RuntimeException("Unable to read file from path: " + path, e);
    }
    crt.setTemplateBody("");
  }
}
