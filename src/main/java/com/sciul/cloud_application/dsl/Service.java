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

  public List<Integer> portListWithin(String protocol) {
    return portList(protocol, "within");
  }

  public List<Integer> portListExternal(String protocol) {
    return portList(protocol, "external");
  }

  private List<Integer> portList(String protocol, String type) {
    List<Integer> l = new ArrayList<>();
    for (Port p : ports) {
      if (p.getProtocol().equals(protocol) && p.getType().equals(type)) {
        l.add(p.getPort());
      }
    }
    return l;
  }


  public static final boolean isPortExternal(Port port) {
    return port.getType().equals("external");
  }

  public static final boolean isPortWithin(Port port) {
    return port.getType().equals("within");
  }
}
