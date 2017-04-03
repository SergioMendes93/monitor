#!/bin/bash

for container in $(docker ps -q); do
  status=`docker exec $container ls /proc/$1 2>/dev/null`
  if [ ! -z "$status" ]; then
    name=`docker ps --filter ID=$container --format "{{.Names}}"`
    echo "PID: $1 found in $container ($name)"
    break;
  fi
done;

