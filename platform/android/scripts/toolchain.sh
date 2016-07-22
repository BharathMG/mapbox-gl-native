#!/usr/bin/env bash

set -e
set -o pipefail

export MASON_PLATFORM=android
export MASON_ANDROID_ABI=${1:-arm-v7}
export PATH="`pwd`/.mason:${PATH}"
export MASON_DIR="`pwd`/.mason"

export PATH=`mason env PATH`

echo "set(CMAKE_SYSTEM_NAME Android)"
echo "set(CMAKE_SYSTEM_VERSION 1)"
echo "set(NDK_ROOT \"/Users/ivo/git/mapbox-gl-native/mason_packages/osx-x86_64/android-ndk/arm-9-r12b\")"
echo "set(CMAKE_FIND_ROOT_PATH \${NDK_ROOT})"
echo "set(CMAKE_CXX_COMPILER \"`which $(mason env CXX)`\")"
echo "set(CMAKE_C_COMPILER \"`which $(mason env CC)`\")"
echo "set(CMAKE_LINKER \"`which $(mason env LD)`\")"
echo "set(CMAKE_AR \"`which $(mason env AR)`\")"
echo "set(CMAKE_RANLIB \"`which $(mason env RANLIB)`\")"
echo "set(CMAKE_STRIP \"`which $(mason env STRIP)`\")"

#echo CMAKE_LINK \"`which $(mason env CXX)`\")"
#echo CMAKE_LDFLAGS=\"`mason env LDFLAGS` \${LDFLAGS:-}\"
#echo CFLAGS=\"`mason env CFLAGS` \${CFLAGS:-}\"
#echo CXXFLAGS=\"`mason env CXXFLAGS` \${CXXFLAGS:-}\"
#echo CPPFLAGS=\"`mason env CPPFLAGS` \${CPPFLAGS:-}\"
#echo JNIDIR=\"`mason env JNIDIR`\"