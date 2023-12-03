#!/bin/bash

if [ -z "$(ls -A /contube/tubes/*.yaml 2>/dev/null)" ]; then
  echo "No Tube YAML files found in /contube/tubes. Please provide at least one YAML file."
  exit 1
fi

bin/runtime.sh conf/contube.yaml tubes/*.yaml
