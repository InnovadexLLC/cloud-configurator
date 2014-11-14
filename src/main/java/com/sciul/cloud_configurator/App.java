package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.model.ListStacksResult;

public class App {

  public Provider getProvider() {
    return provider;
  }

  public void setProvider(Provider provider) {
    this.provider = provider;
  }

  private Provider provider;

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  private String request;

  public App() {
    
  }
  
  public App(Provider provider) {
    this.provider = provider;
  }


  public static void main(String[] args) {
    try {

      if (!AwsProvider.validate()) {
        throw new RuntimeException("Unable to instantiate AwsProvider!");
      }

      App app = new App(new AwsProvider());

      //app.processJson(args[0]);
      ListStacksResult lst = app.getProvider().listStacks(args);

      System.out.println("lst: " + lst);

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
