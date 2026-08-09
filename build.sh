#!/usr/bin/env bash
set -e
rm -rf out
mkdir -p out
find src -name "*.java" > sources.txt
javac -d out @sources.txt
rm sources.txt
