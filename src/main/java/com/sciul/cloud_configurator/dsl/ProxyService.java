package com.sciul.cloud_configurator.dsl;

import java.io.File;

/**
 * Created by sumeetrohatgi on 12/28/14.
 */
class ProxyService {
  private HttpService httpService;
  private String domain;
  private File certificateKeyFile;

  public void setHttpService(HttpService httpService) {
    this.httpService = httpService;
  }

  public HttpService getHttpService() {
    return httpService;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getDomain() {
    return domain;
  }

  public void setCertificateKeyFile(File certificateKeyFile) {
    this.certificateKeyFile = certificateKeyFile;
  }

  public File getCertificateKeyFile() {
    return certificateKeyFile;
  }
}
