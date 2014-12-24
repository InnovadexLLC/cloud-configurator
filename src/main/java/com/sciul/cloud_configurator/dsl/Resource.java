package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Resources required by a typical cloud.
 *
 * Created by sumeetrohatgi on 12/23/14.
 */
public abstract class Resource {
  protected Map<String, String> tags = new HashMap<>();

  /**
   * visitor design pattern method, allows a provider
   * to customize data readout
   *
   * @param provider cloud provider like AWS, Azure, GCE, ...
   * @return <code>JsonObject</code>
   */
  public abstract JsonObject toJson(Provider provider);

  /**
   * for grouping resources of the same type
   * @param name tag name
   * @param value any
   * @return <code>this</code>
   */
  public abstract Resource tag(String name, String value);
}
