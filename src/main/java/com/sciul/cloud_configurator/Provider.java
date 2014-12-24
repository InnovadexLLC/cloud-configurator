package com.sciul.cloud_configurator;

import com.amazonaws.services.cloudformation.model.CreateStackResult;
import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.ListStacksResult;

/**
 * Created by sumeetrohatgi on 11/14/14.
 */
interface Provider {
  ListStacksResult listStacks(String[] args);

  DescribeStacksResult describeStacks(String environment);

  CreateStackResult createStack(Template template);
}
