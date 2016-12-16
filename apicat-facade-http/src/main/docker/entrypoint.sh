#!/bin/bash

dockerize -wait tcp://apicat-mysql:3306 -timeout 30s

java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar /app.jar