package com.sciul.cloud_configurator;

import org.slf4j.Logger;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.CreateStackRequest;
import com.amazonaws.services.cloudformation.model.ListStacksRequest;
import com.amazonaws.services.cloudformation.model.ListStacksResult;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by sumeetrohatgi on 11/14/14.
 */
public class AwsProvider implements Provider {
  private static final String ACCESS_KEY = "AWS_ACCESS_KEY";
  private static final String SECRET_KEY = "AWS_SECRET_KEY";
  private final AmazonCloudFormationClient clt = new AmazonCloudFormationClient();
  private static Logger logger = LoggerFactory.getLogger(AwsProvider.class);

  public AwsProvider() {
    validate();
  }

  public static boolean validate() {
    if (!System.getenv().containsKey(ACCESS_KEY) || !System.getenv().containsKey(SECRET_KEY)) {
      return false;
    }
    return true;
  }

  public ListStacksResult listStacks(String[] args) {
    ListStacksRequest rq = new ListStacksRequest();
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
