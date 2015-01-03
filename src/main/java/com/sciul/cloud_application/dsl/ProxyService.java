package com.sciul.cloud_application.dsl;

import java.io.File;

/**
 * a proxy service; typically shields an internal
 * http service
 *
 * Created by sumeetrohatgi on 12/28/14.
 */
class ProxyService extends Service {
  private HttpService httpService;
  private String domain;
  private File certificateKeyFile;

  public ProxyService() {
    addPortExternal("tcp", 443);
  }

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
