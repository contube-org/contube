bin/runtime.sh conf/contube.yaml examples/*.yaml

if ! diff examples/source.txt examples/test-result-sink.txt > /tmp/contube_diff_output.txt; then
    echo "\033[0;31mTest failed: The sink content is different, see differences below:\033[0m"
    cat /tmp/contube_diff_output.txt
    exit 1
fi

echo "\033[0;32mTest passed!\033[0m"
