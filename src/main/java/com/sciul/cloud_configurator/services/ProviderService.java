package com.sciul.cloud_configurator.services;

import com.sciul.cloud_application.models.Cloud;
import com.sciul.cloud_configurator.dsl.ResourceList;

/**
 * Created by sumeetrohatgi on 1/23/15.
 */
public interface ProviderService {
  String template(ResourceList resourceList);
  Cloud build(ResourceList resourceList);
}
