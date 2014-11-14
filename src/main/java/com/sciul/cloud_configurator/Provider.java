package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.model.ListStacksResult;

/**
 * Created by sumeetrohatgi on 11/14/14.
 */
interface Provider {
  ListStacksResult listStacks(String[] args);
  void processJson(String[] args);
}

