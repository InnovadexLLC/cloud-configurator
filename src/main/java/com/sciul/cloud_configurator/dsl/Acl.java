package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class Acl extends Resource {

  private String vpcName;

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createAcl(this);
  }

  @Override
  public Acl tag(String name, String value) {
    tags.put(name, value);
    return this;
  }

  public String getVpcName() {
    return vpcName;
  }

  public void setVpcName(String vpcName) {
    this.vpcName = vpcName;
  }
}
