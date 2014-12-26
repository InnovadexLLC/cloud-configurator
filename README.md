cloud-configurator
==================

A language parser program for configuring cloud environments. Philosophy is that convention triumphs over configuration.

## Provider ##

An abstraction that allows same program to be used across multiple cloud providers. For now, AWSProvider is the only one that's provided in the source code.

## Template ##

A dsl defined for writing and managing cloud configuration. An example dsl is provided below. When the dsl is executed, it creates a new VPC with *seven* subnets and *four* security groups.

    String zoneA = region + "a";
    String zoneB = region + "b";

    return ResourceList
        .start(name)
        .dns("ELB", "APP", webDomain)
        .dns("ELB", "API", apiDomain)
        .vpc("10.0.0.0/16", region)
        .subnet("ELB", zoneA, "10.0.12.0/24")
        .subnet("ELB", zoneB, "10.0.13.0/24")
        .subnet("APP", zoneA, "10.0.51.0/24")
        .subnet("APP", zoneB, "10.0.52.0/24")
        .subnet("DB", zoneA, "10.0.91.0/24")
        .subnet("DB", zoneB, "10.0.92.0/24")
        .subnet("NAT", zoneB, "10.0.0.0/24", true)
        .end();
