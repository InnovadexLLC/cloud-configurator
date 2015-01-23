package com.sciul.cloud_configurator.services;

import com.sciul.cloud_application.models.WebApplication;
import com.sciul.cloud_configurator.dsl.ResourceList;

/**
 * Created by sumeetrohatgi on 1/23/15.
 */
public interface TemplateService {
  ResourceList build(WebApplication webApplication);
}
