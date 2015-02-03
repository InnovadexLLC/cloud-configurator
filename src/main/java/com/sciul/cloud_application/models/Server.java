package com.sciul.cloud_application.models;

/**
 * Created by sumeetrohatgi on 1/31/15.
 */
public class Server {
  private String name;
  private String internalIp;
  private String externalIp;
  private String sshKey;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInternalIp() {
    return internalIp;
  }

  public void setInternalIp(String internalIp) {
    this.internalIp = internalIp;
  }

  public String getExternalIp() {
    return externalIp;
  }

  public void setExternalIp(String externalIp) {
    this.externalIp = externalIp;
  }

  public String getSshKey() {
    return sshKey;
  }

  public void setSshKey(String sshKey) {
    this.sshKey = sshKey;
  }
}
