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

  private Provider provider;

  public App() {
    
  }
  
  public App(Provider provider) {
    this.provider = provider;
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

      App app = new App(new AwsProvider());

      //app.processJson(args[0]);
      app.provider.listStacks();
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
