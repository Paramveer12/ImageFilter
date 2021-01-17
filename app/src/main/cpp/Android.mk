LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := imageprocessing
LOCAL_SRC_FILES := imageprocessing.c
LOCAL_LDLIBS    := -lm -llog -ljnigraphics
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)