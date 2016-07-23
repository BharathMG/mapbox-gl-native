#!/usr/bin/env bash

set -e
set -o pipefail

export MASON_PLATFORM=android
export MASON_ANDROID_ABI=${1:-arm-v7}
export PATH="`pwd`/.mason:${PATH}"
export MASON_DIR="`pwd`/.mason"

export PATH=`mason env PATH`

echo "set(CMAKE_SYSTEM_NAME Linux)"
echo "set(CMAKE_SYSTEM_VERSION 1)"
echo "set(CMAKE_CXX_COMPILER \"`which $(mason env CXX)`\")"
echo "set(CMAKE_C_COMPILER \"`which $(mason env CC)`\")"
echo "set(ANDROID_JNIDIR \"`mason env JNIDIR`\")"