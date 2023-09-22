cmake_minimum_required(VERSION 3.22.1)
project("zygisk")

set(DOBBY_DIR ${CMAKE_SOURCE_DIR}/Dobby)
macro(SET_OPTION option value)
    set(${option} ${value} CACHE INTERNAL "" FORCE)
endmacro()
SET_OPTION(DOBBY_DEBUG OFF)
SET_OPTION(DOBBY_GENERATE_SHARED OFF)
SET_OPTION(Plugin.Android.BionicLinkerUtil ON)
add_subdirectory(${DOBBY_DIR} dobby)
get_property(DOBBY_INCLUDE_DIRECTORIES
        TARGET dobby
        PROPERTY INCLUDE_DIRECTORIES)
include_directories(
        .
        ${DOBBY_INCLUDE_DIRECTORIES}
        $<TARGET_PROPERTY:dobby,INCLUDE_DIRECTORIES>
)

add_library(${CMAKE_PROJECT_NAME} SHARED main.cpp)

target_link_libraries(${CMAKE_PROJECT_NAME} PUBLIC log dobby_static)

target_compile_features(${CMAKE_PROJECT_NAME} PUBLIC c_std_23)
target_compile_features(${CMAKE_PROJECT_NAME} PUBLIC cxx_std_23)

target_compile_options(${CMAKE_PROJECT_NAME} PUBLIC -fno-exceptions -fno-rtti -fvisibility=hidden -fvisibility-inlines-hidden -O3 -mllvm -polly -flto -faddrsig -ffunction-sections -fdata-sections)
target_link_options(${CMAKE_PROJECT_NAME} PUBLIC -O3 -mllvm -polly -flto -Wl,--icf=all -Wl,--exclude-libs,ALL -Wl,--gc-sections)