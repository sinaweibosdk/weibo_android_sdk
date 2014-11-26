#ifndef _STDAFX_H_
#define _STDAFX_H_

#include <jni.h>
#include <string.h>
#include <malloc.h>
#include <stdlib.h>

//#define _LOG_ON   // 日志开关

#include "logDef.h"

//#define _DEBUG    // 编译debug版的so文件时 打开此宏

//无需混淆
//#ifndef _DEBUG
//#define _OBFUSECATE_CODE   //如果release版本的代码被混淆 则此项宏自动在_DEBUG关闭时打开 否则须手动关闭该宏
//#endif

#define PUSH_LOCAL_FRAME( RETURN_VALUE_ON_FAIL )\
    if ( (*env)->PushLocalFrame( env, 16 ) < 0 )\
        return RETURN_VALUE_ON_FAIL

#define POP_LOCAL_FRAME( RETURN_VALUE )\
    RETURN_VALUE = (*env)->PopLocalFrame( env, RETURN_VALUE )

#define POP_LOCAL_FRAME_NULL()\
    (*env)->PopLocalFrame( env, NULL )

#define _NO_EXPORT __attribute__((visibility("hidden")))
#endif
