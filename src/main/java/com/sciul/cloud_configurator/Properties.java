package com.sciul.cloud_configurator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

  @Value("${app.name}")
  private String appName;

  @Value("${vpc.naming}")
  private String vpcNameing;

  @Value("${subnet.naming}")
  private String subnetNaming;

  @Value("${secgroup.naming}")
  private String secgroupNaming;

  @Value("${routetable.naming}")
  private String routetableNaming;

  @Value("${acl.naming}")
  private String aclNaming;

  @Value("${ip.block}")
  private String ipBlock;

  @Value("${ip.subnet.1}")
  private String ipSubnet1;

  @Value("${ip.subnet.2}")
  private String ipSubnet2;

  @Value("${delete.protection}")
  private String deleteProtection;

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getVpcNameing() {
    return vpcNameing;
  }

  public void setVpcNameing(String vpcNameing) {
    this.vpcNameing = vpcNameing;
  }

  public String getSubnetNaming() {
    return subnetNaming;
  }

  public void setSubnetNaming(String subnetNaming) {
    this.subnetNaming = subnetNaming;
  }

  public String getSecgroupNaming() {
    return secgroupNaming;
  }

  public void setSecgroupNaming(String secgroupNaming) {
    this.secgroupNaming = secgroupNaming;
  }

  public String getRoutetableNaming() {
    return routetableNaming;
  }

  public void setRoutetableNaming(String routetableNaming) {
    this.routetableNaming = routetableNaming;
  }

  public String getAclNaming() {
    return aclNaming;
  }

  public void setAclNaming(String aclNaming) {
    this.aclNaming = aclNaming;
  }

  public String getIpBlock() {
    return ipBlock;
  }

  public void setIpBlock(String ipBlock) {
    this.ipBlock = ipBlock;
  }

  public String getIpSubnet1() {
    return ipSubnet1;
  }

  public void setIpSubnet1(String ipSubnet1) {
    this.ipSubnet1 = ipSubnet1;
  }

  public String getIpSubnet2() {
    return ipSubnet2;
  }

  public void setIpSubnet2(String ipSubnet2) {
    this.ipSubnet2 = ipSubnet2;
  }

  public String getDeleteProtection() {
    return deleteProtection;
  }

  public void setDeleteProtection(String deleteProtection) {
    this.deleteProtection = deleteProtection;
  }

}
