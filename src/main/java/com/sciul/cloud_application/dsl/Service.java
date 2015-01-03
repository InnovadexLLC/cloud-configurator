package com.sciul.cloud_application.dsl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by sumeetrohatgi on 1/3/15.
 */
abstract class Service {
  private String name;
  private List<Port> ports = new ArrayList<>();

  public final String getName() {
    return name;
  }

  public final void setName(String name) {
    this.name = name;
  }

  public final Iterable<Port> getPorts() {
    return ports;
  }

  public final void setPorts(List<Port> ports) {
    this.ports = new ArrayList<>(ports);
  }

  public final Service addPortWithin(String protocol, Integer port) {
    ports.add(new Port(protocol, port, "within"));
    return this;
  }

  public final Service addPortExternal(String protocol, Integer port) {
    ports.add(new Port(protocol, port, "external"));
    return this;
  }

}
