package com.sciul.cloud_configurator;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudformation.model.*;
import org.slf4j.Logger;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sumeetrohatgi on 11/14/14.
 */
@Component
public class AwsProvider implements Provider {
  private static final String ACCESS_KEY = "AWS_ACCESS_KEY";
  private static final String SECRET_KEY = "AWS_SECRET_KEY";
  private final AmazonCloudFormationClient clt = new AmazonCloudFormationClient();
  private static Logger logger = LoggerFactory.getLogger(AwsProvider.class);

  public AwsProvider() {
    clt.setRegion(Region.getRegion(Regions.US_WEST_2));
  }

  public AwsProvider(String region) {
    logger.debug("passed in region: {}", region);
    clt.setRegion(Region.getRegion(Regions.US_WEST_2));
  }

  public static boolean validate() {
    if (!System.getenv().containsKey(ACCESS_KEY) || !System.getenv().containsKey(SECRET_KEY)) {
      return false;
    }
    return true;
  }

  public void createStack(String environment) {

  }

  public DescribeStacksResult describeStacks(String[] args) {
    return clt.describeStacks(new DescribeStacksRequest());
  }

  public ListStacksResult listStacks(String[] args) {
    ListStacksRequest rq = new ListStacksRequest();
    rq.setStackStatusFilters(new ArrayList<String>() {{ add("CREATE_COMPLETE"); }});
    ListStacksResult lst = clt.listStacks(rq);
    return lst;
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
