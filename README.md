cloud-configurator
==================

A language parser program for configuring cloud environments.

## Provider ##

An abstraction that allows same program to be used across multiple cloud providers.

## Template ##

A dsl defined for writing and managing cloud configuration

    cloud defaults {
      provider: "aws",
      region: "us-west-2",
      lbs: [ "api", "app" ],
      groups: [
        {
          name: "cassandra",
          ports: [ 9160, 9220 ]
        },
        {
          name: "elastic",
          ports: [ 2323 ]
        },
        {
          name: "rabbitmq",
          ports: [ 2323 ]
        },
        {
          name: "ssh",
          ports: [ 22 ]
        },
        {
          name: "tomcat",
          ports: [ 8080 ]
        },
        {
          name: "nodejs",
          ports: [ 3000 ]
        }
      ]
    }
