package com.sciul.cloud_configurator.dsl;

/**
 * Created by sumeetrohatgi on 12/28/14.
 */
class DataService {
  private String name;
  private Integer[] ports;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setPorts(Integer[] ports) {
    this.ports = ports;
  }

  public Integer[] getPorts() {
    return ports;
  }
}
