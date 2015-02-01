package com.sciul.cloud_configurator.services;

import com.sciul.cloud_application.models.CloudBlueprint;
import com.sciul.cloud_application.models.Server;
import com.sciul.cloud_configurator.dsl.ResourceList;

import java.util.List;

/**
 * Created by sumeetrohatgi on 1/23/15.
 */
public interface ApplicationService {
  ResourceList build(CloudBlueprint cloudBlueprint);
  List<Server> getServers(String environment);
}
