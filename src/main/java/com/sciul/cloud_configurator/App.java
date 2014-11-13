package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.ListStacksResult;

public class App {

  private static final String ACCESS_KEY = "AWS_ACCESS_KEY";
  private static final String SECRET_KEY = "AWS_SECRET_KEY";

  public void App() {

  }

  public void run() {
    AmazonCloudFormationClient clt = new AmazonCloudFormationClient();
    ListStacksResult lst = clt.listStacks();
    System.out.println("lst: " + lst);
  }

  public static void main(String[] args) {
    if (!System.getenv().containsKey(ACCESS_KEY) || !System.getenv().containsKey(SECRET_KEY)) {
      System.err.print("Please define aws api keys as env vars ("+ACCESS_KEY+" & "+SECRET_KEY+")!!");
      System.exit(1);
    }
    App app = new App();
    app.run();
  }
}
