cloud-configurator
==================

A program for configuring cloud environments (AWS) for multi-tier webapps/ webapi. Preference is convention over configuration.

## use case ##

We find that building out and maintaining cloud environments is surprisingly hard. There are many solutions available from vendors, but each one comes with a vendor lock in. Our particular needs (and we feel others are in this bucket too), are pretty vanilla. However, managing even a single cloud application with dev, qa, stage & production is tricky!

With this project, we are able to define an application using the language of application developers and map it to a cloud providers environment configuration. This cloud configuration can be run at will of the application developer to generate appropriate cloud artifacts.

At UL, **completely** automate our entire development workflow by using [Chef](https://www.chef.io/chef/choose-your-version/) to label (calling it a chef `environment`) a set of cloud resources and run a parametrized (using chef `roles`) set of recipes against them.

## status ##

![travis-ci](https://travis-ci.org/ulini/cloud-configurator.svg?branch=master)

Still under development. `read as NOT READY!`

## application ##

Abstraction for a commonly used `cloud` application. Most developers will use this level of abstraction to configure their applications. An example of this is provided below:

    // building a cloud ResourceList

    Application.create("myAwesomeApp-dev")
        .dataService("C*", Integer[] { 3120 })
        .httpService("api")
        .proxyService("api", "api.myawesomeapp.com", new File("privateApiKeyFile.cer")
        .httpService("app")
        .proxyService("app", "app.myawesomeapp.com", new File("privateAppKeyFile.cer")
        .build("us-west-2")

## template ##

Low level abstraction for users with more controlled needs over their environment. Example:

    String zoneA = region + "a";

    String zoneB = region + "b";

    return ResourceList
        .start(name)  // name of the environment
        .dns("ELB", "APP", webDomain) // CNAME to load balancer
        .dns("ELB", "API", apiDomain) // CNAME to load balancer
        .vpc("10.0.0.0/16", region)
        .subnet("ELB", "10.0.12.0/24", /* isPubliclyAccessible */ true, zoneA, zoneB)
        .subnet("APP", "10.0.51.0/24", zoneA, zoneB)
        .subnet("DB", "10.0.91.0/24", zoneA, zoneB)
        .subnet("OPS", "10.0.0.0/24", /* isPubliclyAccessible */ true, zoneB)
        .end();

## provider ##

An abstraction that allows same program to be used across multiple cloud providers.

For now, AWSProvider is the only one that's provided in the source code. It uses AWS's excellent [CloudFormation](http://aws.amazon.com/cloudformation/) service.

