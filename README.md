# Sample Spring Boot Service that consumes another service through Consul

## To Build into Docker using Maven

First make sure you're running in an environment that has docker available to you. Then execute:

$ mvn package docker:build

Once completed you will have an atomist/simple-boot-microservice-consumer image available, as seen by executing:

$ docker images

```
REPOSITORY                                                      TAG                 IMAGE ID            CREATED             SIZE
antifragilesoftware/simple-boot-microservice-consumer                       latest              39fc873649d9        3 seconds ago       667.6 MB
```
