set -e
bin/runtime.sh conf/contube.yaml examples/*.yaml

if ! diff examples/source.txt examples/test-result-sink.txt > /tmp/contube_diff_output.txt; then
    echo "Test failed: The sink content is different, see differences below:"
    cat /tmp/contube_diff_output.txt
    exit 1
fi

echo "Test passed!"
