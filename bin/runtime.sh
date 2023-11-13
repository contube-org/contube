base_dir=$(dirname $0)

cd $base_dir/../ || exit 1

CLASSPATH=$(find . lib -type f \( -name '*.jar' -o -path "*/build/libs/*.jar" \) | tr '\n' ':')

java -cp $CLASSPATH com.zikeyang.contube.runtime.Runtime "$@"
