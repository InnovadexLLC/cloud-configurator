package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.model.ListStacksResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private Provider provider;
  private String request;
  private static Logger logger = LoggerFactory.getLogger(App.class);

  public Provider getProvider() {
    return provider;
  }

  public void setProvider(Provider provider) {
    this.provider = provider;
  }

  public String getRequest() {
    return request;
  }

  public void setRequest(String request) {
    this.request = request;
  }

  public App() {  }
  
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

      logger.debug("lst: {}", lst);

    } catch (RuntimeException e) {
      if (e.getCause() != null && e.getCause().getCause() != null) {
        logger.error("Error running Configurator App", e);
      }
      else {
        logger.error(e.getMessage());
      }
      System.exit(1);
    }
  }
}
