#ifndef _LOG_DEF_H
#define _LOG_DEF_H

#include <android/log.h>
#define LOG_TAG "weibosdk"

#ifdef _LOG_ON
#define LOGE(a) __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, a)
#else
#define LOGE(a)
#endif


#endif
