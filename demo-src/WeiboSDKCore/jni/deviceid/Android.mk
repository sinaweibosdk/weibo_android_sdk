LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := weibosdkcore

LOCAL_SRC_FILES := utils.c \
    crypt.c \
    configLoader.c \
    encodedConstants.c \
    deviceIdHelper.c \
    weiboGetDeviceId.c

LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog

include $(BUILD_SHARED_LIBRARY)
