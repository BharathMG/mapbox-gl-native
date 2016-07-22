mason_use(geojson 0.1.4)
mason_use(jni.hpp 2.0.0)
mason_use(libjpeg-turbo 1.4.2)
mason_use(libpng 1.6.20)
mason_use(libzip 0.11.2)
mason_use(nunicode 1.6)
mason_use(sqlite 3.9.1)
mason_use(zlib system)

macro(mbgl_platform_core)

    target_sources(mbgl-core
        # Loop
        PRIVATE platform/android/src/thread.cpp
        PRIVATE platform/android/src/async_task.cpp
        PRIVATE platform/android/src/run_loop.cpp
        PRIVATE platform/android/src/timer.cpp

        # File source
        PRIVATE platform/android/src/http_file_source.cpp
        PRIVATE platform/android/src/asset_file_source.cpp
        PRIVATE platform/default/default_file_source.cpp
        PRIVATE platform/default/online_file_source.cpp

        # Offline
        # PRIVATE include/mbgl/storage/offline.hpp
        PRIVATE platform/default/mbgl/storage/offline.cpp
        PRIVATE platform/default/mbgl/storage/offline_database.cpp
        PRIVATE platform/default/mbgl/storage/offline_database.hpp
        PRIVATE platform/default/mbgl/storage/offline_download.cpp
        PRIVATE platform/default/mbgl/storage/offline_download.hpp
        PRIVATE platform/default/sqlite3.cpp
        PRIVATE platform/default/sqlite3.hpp

        # Misc
        PRIVATE platform/android/src/native_map_view.cpp
        PRIVATE platform/android/src/jni.cpp
        PRIVATE platform/android/src/attach_env.cpp
        PRIVATE platform/android/src/log_android.cpp
        PRIVATE platform/default/string_stdlib.cpp

        # Image handling
        PRIVATE platform/default/image.cpp
        PRIVATE platform/default/png_reader.cpp
        PRIVATE platform/default/jpeg_reader.cpp

        # Headless view
        # TODO
    )

    target_include_directories(mbgl-core
        PRIVATE platform/default
    )

    target_add_mason_package(mbgl-core PUBLIC sqlite)
    target_add_mason_package(mbgl-core PUBLIC nunicode)
    target_add_mason_package(mbgl-core PUBLIC libpng)
    target_add_mason_package(mbgl-core PUBLIC libjpeg-turbo)
    target_add_mason_package(mbgl-core PUBLIC zlib)
    target_add_mason_package(mbgl-core PUBLIC libzip)
    target_add_mason_package(mbgl-core PUBLIC geojson)
    target_add_mason_package(mbgl-core PUBLIC jni.hpp)

    target_link_libraries(mbgl-core
        PRIVATE -llog
        PRIVATE -landroid
        PRIVATE -lEGL
        PRIVATE -lGLESv2
        PRIVATE -lstdc++
        PRIVATE -latomic
    )
endmacro()
