package com.sciul.cloud_configurator.services;

import com.sciul.cloud_application.models.Cloud;
import com.sciul.cloud_configurator.dsl.Provider;
import com.sciul.cloud_configurator.dsl.ResourceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sumeetrohatgi on 1/23/15.
 */
@Service
public class ProviderServiceImpl implements ProviderService {
  @Autowired
  private Provider provider;

  @Override
  public String template(ResourceList resourceList) {
    return provider.generateStackTemplate(resourceList);
  }

  @Override
  public Cloud build(ResourceList resourceList) {
    return provider.createStack(resourceList);
  }
}
