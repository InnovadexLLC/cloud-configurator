package com.sciul.cloud_application.dsl;

/**
 * Created by sumeetrohatgi on 1/3/15.
 */
class Port {
  private String protocol;
  private Integer port;
  private String type;

  public Port(String protocol, Integer port, String type) {
    this.protocol = protocol;
    this.port = port;
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }
}
