#!/usr/bin/env bash
set -e
rm -rf out-test
mkdir -p out-test
find src test -name "*.java" > test-sources.txt
javac -d out-test @test-sources.txt
rm test-sources.txt
java -cp out-test legion.test.TestRunner
