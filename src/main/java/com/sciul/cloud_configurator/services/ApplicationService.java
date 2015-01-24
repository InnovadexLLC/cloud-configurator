package com.sciul.cloud_configurator.services;

import com.sciul.cloud_application.models.CloudBlueprint;
import com.sciul.cloud_configurator.dsl.ResourceList;

/**
 * Created by sumeetrohatgi on 1/23/15.
 */
public interface ApplicationService {
  ResourceList build(CloudBlueprint cloudBlueprint);
}
