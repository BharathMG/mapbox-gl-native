// This file is generated. Do not edit.

#pragma once

#include "layer.hpp"
#include <mbgl/style/layers/raster_layer.hpp>
#include <jni/jni.hpp>

namespace mbgl {
namespace android {

class RasterLayer : public Layer {
public:

    static constexpr auto Name() { return "com/mapbox/mapboxsdk/style/layers/RasterLayer"; };

    static jni::Class<RasterLayer> javaClass;

    static void registerNative(jni::JNIEnv&);

    RasterLayer(jni::JNIEnv&, jni::String, jni::String);

    RasterLayer(mbgl::Map&, mbgl::style::RasterLayer&);

    ~RasterLayer();

    jni::jobject* createJavaPeer(jni::JNIEnv&);
};

} // namespace android
} // namespace mbgl
