#!/bin/bash
set -e

base_dir=$(dirname $0)

pushd $base_dir/..
log4jYaml=$(realpath conf/log4j2.yaml)
CLASSPATH=$(find . -type f \( -name '*.jar' -o -path "*/build/libs/*.jar" \) -exec readlink -f {} \; | tr '\n' ':')
popd

java $JAVA_OPTS -cp $CLASSPATH -Dlog4j.configurationFile=$log4jYaml io.github.contube.runtime.Runtime "$@"
