package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.services.Provider;

import javax.json.JsonObject;

/**
 * Created by sumeetrohatgi on 12/25/14.
 */
public class NetworkAcl extends Resource {

  private String vpcName;

  public NetworkAcl(String name, String vpcName, ResourceList resourceList) {
    this.resourceList = resourceList;
    setName(name);
    this.vpcName = vpcName;

    resourceList.add(new NetworkAclEntry("ACL-IN", "0.0.0.0/0", "-1", "allow", "100", getName(), false, resourceList));
    resourceList.add(new NetworkAclEntry("ACL-OUT", "0.0.0.0/0", "-1", "allow", "100", getName(), true, resourceList));
  }

  @Override
  public JsonObject toJson(Provider provider) {
    return provider.createAcl(this);
  }

  @Override
  public NetworkAcl tag(String name, String value) {
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
