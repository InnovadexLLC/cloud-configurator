package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.ListStacksResult;

public class App {

  public void App() {

  }

  public void run() {
    AmazonCloudFormationClient clt = new AmazonCloudFormationClient();
    ListStacksResult lst = clt.listStacks();
    System.out.println("lst: " + lst);
  }

  public static void main(String[] args) {
    App app = new App();
    app.run();
  }
}
