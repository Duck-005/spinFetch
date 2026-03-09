#!/bin/bash

rm -rf build
mkdir build

javac -d build -sourcepath src src/cli/Main.java

jar --create --file spinFetch.jar --main-class cli.Main -C build .
