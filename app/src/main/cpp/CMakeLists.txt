cmake_minimum_required(VERSION 3.22.1)
project("zygisk")

find_package(cxx REQUIRED CONFIG)
link_libraries(cxx::cxx)

if (NOT TARGET dobby)
    set(DOBBY_DIR ${CMAKE_SOURCE_DIR}/Dobby)
    add_subdirectory(${DOBBY_DIR} dobby)
    get_property(DOBBY_INCLUDE_DIRECTORIES
            TARGET dobby
            PROPERTY INCLUDE_DIRECTORIES)
    include_directories(
            .
            ${DOBBY_INCLUDE_DIRECTORIES}
            $<TARGET_PROPERTY:dobby,INCLUDE_DIRECTORIES>
    )
endif ()

add_library(${CMAKE_PROJECT_NAME} SHARED
        main.cpp)
find_library(log-lib log)

target_link_libraries(${CMAKE_PROJECT_NAME}
        ${log-lib}
        dobby_static)