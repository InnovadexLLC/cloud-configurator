package com.sciul.cloud_configurator.dsl;

import com.sciul.cloud_configurator.Provider;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Resources required by a typical cloud.
 *
 * Created by sumeetrohatgi on 12/23/14.
 */
public abstract class Resource {
  protected Map<String, String> tags = new HashMap<>();
  protected ResourceList resourceList;
  protected String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    tags.put("Name", name);
  }

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

  /**
   * read all tags on a given resource;
   * inherits tags from <code>ResourceList</code>
   * @return
   */
  public Set<Map.Entry<String, String>> tags() {
    Map<String, String> myTags = new HashMap<>();

    if (resourceList != null && resourceList.tags != null) {
      myTags.putAll(resourceList.tags);
    }

    myTags.putAll(tags);
    return myTags.entrySet();
  }
}
