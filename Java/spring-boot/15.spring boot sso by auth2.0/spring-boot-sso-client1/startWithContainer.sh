#!/bin/bash

cd /jar
java -Dloader.path=./libs -jar /jar/sso-client1.jar |tee /jar/jar.log
