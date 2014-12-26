cloud-configurator
==================

A language parser program for configuring cloud environments.

## Provider ##

An abstraction that allows same program to be used across multiple cloud providers.

## Template ##

A dsl defined for writing and managing cloud configuration. An example dsl is provided below.

The dsl (when executed), creates a new VPC with seven subnets and three security groups.

    ResourceList
        .start(name, null)
        .dns("APP", webDomain, "ELB")
        .next()
        .dns("API", apiDomain, "ELB")
        .next()
        .vpc("10.0.0.0/16", region)
        .subnet("ELB", zoneA, "10.0.12.0/24")
        .subnet("ELB", zoneB, "10.0.13.0/24")
        .subnet("APP", zoneA, "10.0.51.0/24")
        .subnet("APP", zoneB, "10.0.52.0/24")
        .subnet("DB", zoneA, "10.0.91.0/24")
        .subnet("DB", zoneB, "10.0.92.0/24")
        .subnet("NAT", zoneB, "10.0.0.0/24")
        .next()
        .end();