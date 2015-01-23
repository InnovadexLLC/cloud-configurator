package com.sciul.cloud_application.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by sumeetrohatgi on 1/3/15.
 */
abstract class Service {
  private static final String WITHIN = "within";
  private static final String EXTERNAL = "external";
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
    ports.add(new Port(protocol, port, WITHIN));
    return this;
  }

  public final Service addPortExternal(String protocol, Integer port) {
    ports.add(new Port(protocol, port, EXTERNAL));
    return this;
  }

  public List<Integer> portListWithin(String protocol) {
    return portList(protocol, WITHIN);
  }

  public List<Integer> portListExternal(String protocol) {
    return portList(protocol, EXTERNAL);
  }

  private List<Integer> portList(String protocol, String type) {
    return ports
        .stream()
        .filter(p -> p.getProtocol().equals(protocol) && p.getType().equals(type))
        .map(Port::getPort)
        .collect(Collectors.toList());
  }


  public static final boolean isPortExternal(Port port) {
    return port.getType().equals(EXTERNAL);
  }

  public static final boolean isPortWithin(Port port) {
    return port.getType().equals(WITHIN);
  }
}
