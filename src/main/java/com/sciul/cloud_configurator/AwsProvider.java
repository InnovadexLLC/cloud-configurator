package com.sciul.cloud_configurator;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.*;
import com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl.Resource;
import com.sciul.cloud_configurator.com.sciul.cloud_configurator.dsl.ResourceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;

/**
 * Created by sumeetrohatgi on 11/14/14.
 */
@Component
@PropertySource("classpath:cloud-configure-app.properties")
public class AwsProvider implements Provider {
  private static final String ACCESS_KEY = "AWS_ACCESS_KEY";
  private static final String SECRET_KEY = "AWS_SECRET_KEY";
  private static Logger logger = LoggerFactory.getLogger(AwsProvider.class);

  private AWSCredentials credentials;
  private AmazonCloudFormationClient clt;
  private String region;

  public AwsProvider() {
    try {

      credentials = new ProfileCredentialsProvider().getCredentials();

      clt = new AmazonCloudFormationClient(credentials);

    } catch (Exception e) {
      throw new RuntimeException("Cannot load the credentials from the credential profiles file. "
          + "Please make sure that your credentials file is at the correct "
          + "location (~/.aws/credentials), and is in valid format.", e);
    }
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    if (region == null) {
      throw new RuntimeException("illegal region value: " + region);
    }
    this.region = region;
    Regions r = Regions.DEFAULT_REGION;
    switch (region) {
      case "us-west-2":
        r = Regions.US_WEST_2;
        break;
      default:
        throw new RuntimeException("illegal region: " + region);
    }
    clt.setRegion(Region.getRegion(Regions.US_WEST_2));
  }

  private String toJson(Template template) {
    ResourceList resourceList = template.generateResourceList();
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
    int i = 0;
    for (Resource resource : resourceList.resources()) {
      jsonObjectBuilder.add("arbitrary" + i, resource.toJson());
      i++;
    }

    return jsonObjectBuilder.build().toString();
  }

  public CreateStackResult createStack(final Template template) {

    try {
      CreateStackRequest crq = new CreateStackRequest() {
        {
          setStackName(template.getName());
          setTemplateBody(toJson(template));
        }
      };

      logger.debug("template: {}", crq.getTemplateBody());

      CreateStackResult crs = null; //clt.createStack(crq);

      logger.debug("stack create result: {}", crs);
      return crs;
    } catch (AmazonServiceException ae) {
      throw new RuntimeException("server error", ae);
    } catch (AmazonClientException ae) {
      throw new RuntimeException("client error", ae);
    }
  }

  public DescribeStacksResult describeStacks(String environment) {
    //logger.debug(s.getVpcName());
    DescribeStacksRequest d = new DescribeStacksRequest();
    if (environment != null && !environment.equalsIgnoreCase("")) {
      d.setStackName(environment);
    }
    return clt.describeStacks(d);
  }

  public ListStacksResult listStacks(String[] args) {
    ListStacksRequest rq = new ListStacksRequest();
    rq.setStackStatusFilters(new ArrayList<String>() {
      {
        add("CREATE_COMPLETE");
      }
    });
    ListStacksResult lst = clt.listStacks(rq);
    return lst;
  }

}
