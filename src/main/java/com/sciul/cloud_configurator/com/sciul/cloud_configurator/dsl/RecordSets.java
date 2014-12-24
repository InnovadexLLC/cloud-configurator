package com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl;

/**
 * Created by sumeetrohatgi on 12/23/14.
 */
public class RecordSets {
  private ResourceList resourceList;

  public ResourceList next() {
    return resourceList;
  }

  public ResourceList getResourceList() {
    return resourceList;
  }

  public void setResourceList(ResourceList resourceList) {
    this.resourceList = resourceList;
  }
}
