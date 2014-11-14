package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.CreateStackRequest;
import com.amazonaws.services.cloudformation.model.ListStacksRequest;
import com.amazonaws.services.cloudformation.model.ListStacksResult;
import org.apache.http.util.ExceptionUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class App {

  private static final String ACCESS_KEY = "AWS_ACCESS_KEY";
  private static final String SECRET_KEY = "AWS_SECRET_KEY";
  private final AmazonCloudFormationClient clt = new AmazonCloudFormationClient();

  public void App() {

  }

  public void listStacks() {
    ListStacksRequest rq = new ListStacksRequest();
    ListStacksResult lst = clt.listStacks(rq);
    System.out.println("lst: " + lst.getStackSummaries());
  }

  public void processJson(String path) {
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

  public static void main(String[] args) {
    try {
      if (!System.getenv().containsKey(ACCESS_KEY) || !System.getenv().containsKey(SECRET_KEY)) {
        throw new RuntimeException("Please define aws api keys as env vars ("+ACCESS_KEY+" & "+SECRET_KEY+")!!");
      }

      System.out.println("args length: " + args.length);
      if (args.length != 1) {
        throw new RuntimeException("Usage: App <path to file>");
      }

      App app = new App();
      //app.processJson(args[0]);
      app.listStacks();
    } catch (RuntimeException e) {
      if (e.getCause() != null && e.getCause().getCause() != null) {
        e.printStackTrace();
      }
      else {
        System.err.println("*********** FAILED ***********");
        System.err.println(e.getMessage());
        System.err.println("******************************");
      }
      System.exit(1);
    }
  }
}
