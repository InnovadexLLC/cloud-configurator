package com.sciul.cloud_configurator.dsl;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.jclouds.ContextBuilder;
import org.jclouds.chef.ChefApi;
import org.jclouds.chef.ChefContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by sumeetrohatgi on 1/31/15.
 */
public class ChefApiWrapper {
  private String client;
  private String pemFile;
  private String chefServerUrl;
  private ChefApi chefApi;

  public String getChefServerUrl() {
    return chefServerUrl;
  }

  public void setChefServerUrl(String chefServerUrl) {
    this.chefServerUrl = chefServerUrl;
  }

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public String getPemFile() {
    return pemFile;
  }

  public void setPemFile(String pemFile) {
    this.pemFile = pemFile;
  }

  public ChefApi getChefApi() {
    if (chefApi == null) {
      synchronized (ChefApiWrapper.class) {
        if (chefApi == null) {
          String credential = null;
          try {
            credential = Files.toString(new File(pemFile), Charsets.UTF_8);
          } catch (IOException e) {
            throw new RuntimeException("unable to read pemFile: " + pemFile, e);
          }

          ChefContext context = ContextBuilder.newBuilder("chef")
              .endpoint(chefServerUrl)
              .credentials(client, credential)
              .buildView(ChefContext.class);

          // The raw API has access to all chef features, as exposed in the Chef REST API
          chefApi = context.unwrapApi(ChefApi.class);
        }
      }
    }
    return chefApi;
  }

}
