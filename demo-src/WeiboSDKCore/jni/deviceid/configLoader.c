#include "stdafx.h"
#include "crypt.h"
#include "encodedConstants.h"
#include "configLoader.h"
#include "deviceIdHelper.h"

#define GET_SIGNATURES 0x00000040

/**
 * read file
 * @param env
 * @param context
 * @param filename
 */
static jbyteArray readfile( JNIEnv *env, jobject context, const char* fileName )
{
    PUSH_LOCAL_FRAME( NULL );

    // open input stream in directory *assets*
    jclass contextCls = (*env)->GetObjectClass( env, context );

    // get assets manager
    char *methodName = getConstantString( env, GETASSETS);
    char *methodSig = getConstantString( env, __LANDROID_CONTENT_RES_ASSETMANAGER );
    jmethodID ctxGetAssetMethodID = (*env)->GetMethodID( env, contextCls, methodName, methodSig );
    free( methodName );
    free( methodSig );
    jobject assetMgrObj = (*env)->CallObjectMethod( env, context, ctxGetAssetMethodID );

    jclass assetMgrCls = (*env)->GetObjectClass( env, assetMgrObj);
    jmethodID assetMgrOpenMethodID = (*env)->GetMethodID( env, assetMgrCls, "open",
            "(Ljava/lang/String;)Ljava/io/InputStream;" );

    jstring srcFileName = (*env)->NewStringUTF( env, fileName );
    jobject srcInputStreamObj = (*env)->CallObjectMethod( env, assetMgrObj, assetMgrOpenMethodID, srcFileName );

    jclass inputStreamCls = (*env)->GetObjectClass( env, srcInputStreamObj );
    jmethodID isAvaMethodID = (*env)->GetMethodID( env, inputStreamCls, "available", "()I" );
    jint srcLength = (*env)->CallIntMethod( env, srcInputStreamObj, isAvaMethodID );

    // read file content to buffer
    jbyteArray buffer = (*env)->NewByteArray( env, srcLength );
    jmethodID isReadMethodID = (*env)->GetMethodID( env, inputStreamCls, "read", "([B)I" );
    jint length = (*env)->CallIntMethod( env, srcInputStreamObj, isReadMethodID, buffer );

    // close file
    jmethodID isCloseMethodID = (*env)->GetMethodID( env, inputStreamCls, "close", "()V" );
    (*env)->CallVoidMethod( env, srcInputStreamObj, isCloseMethodID );

    POP_LOCAL_FRAME( buffer );

    return buffer;
}
//
//_NO_EXPORT
//char* getPinFromConfig( JNIEnv *env, jobject context )
//{
//    LOGE( "loadConfig in" );
//
//    PUSH_LOCAL_FRAME( NULL );
//
//    jbyteArray byteArraySrc = readfile( env, context, "cfg.json" );
//    jint len = ( *env )->GetArrayLength( env, byteArraySrc );
//    jbyte* bytesSrc = ( *env )->GetByteArrayElements( env, byteArraySrc, JNI_FALSE );
//
//    char *buffer = ( char* )malloc( sizeof( char ) * ( len + 1 ) );
//    memcpy( buffer, bytesSrc, len );
//    buffer[len] = '\0';
//
//    ( *env )->ReleaseByteArrayElements( env, byteArraySrc, bytesSrc, 0 );
//
//    jstring strSrc = ( *env )->NewStringUTF( env, buffer );
//    free( buffer );
//
//    // load json
//    jclass jsonCls = ( *env )->FindClass( env, "org/json/JSONObject" );
//    jmethodID jsonCtrMethodID = ( *env )->GetMethodID( env, jsonCls, "<init>", "(Ljava/lang/String;)V" );
//    jobject jsonObj = ( *env )->NewObject( env, jsonCls, jsonCtrMethodID, strSrc );
//
//    jstring jstrPinKey = ( *env )->NewStringUTF( env, "KEY" );
//    // if the value of *PIN* exists
//    jmethodID jsonHasMethodID = ( *env )->GetMethodID( env, jsonCls, "has", "(Ljava/lang/String;)Z" );
//    jboolean isPinExisted = ( *env )->CallBooleanMethod( env, jsonObj, jsonHasMethodID, jstrPinKey );
//    if ( !isPinExisted )
//    {
//        POP_LOCAL_FRAME_NULL();
//        return NULL;
//    }
//
//    // get the value of *PIN*
//    jmethodID jsonGetMethodID = ( *env )->GetMethodID( env, jsonCls, "get",
//            "(Ljava/lang/String;)Ljava/lang/Object;" );
//
//    jstring jstrPinValue = ( *env )->CallObjectMethod( env, jsonObj, jsonGetMethodID, jstrPinKey );
//
//    // copy pin value to heap
//    char* pTmpPin = ( char* )( *env )->GetStringUTFChars( env, jstrPinValue, NULL );
//    int pinLen = strlen( pTmpPin );
//    char* pin = ( char* ) malloc( ( pinLen + 1 ) * sizeof( char ) );
//    strncpy( pin, pTmpPin, pinLen );
//    pin[ pinLen ] = '\0';
//    ( *env)->ReleaseStringUTFChars( env, jstrPinValue, pTmpPin );
//
//    LOGE( pin );
//
//    POP_LOCAL_FRAME_NULL();
//
//    LOGE( "loadConfig out" );
//    return pin;
//}

_NO_EXPORT
char* getMd5SignatrueString( JNIEnv *env, jobject context )
{
	jclass utilClass = (*env) -> FindClass( env, JNIUTILS_CLASS );
	jmethodID getMd5SignatureMethodID = (*env)->GetStaticMethodID( env, utilClass,
				"getMd5Signature",
				"(Landroid/content/Context;)Ljava/lang/String;" );

	jstring md5Sign = (jstring)(*env)->CallStaticObjectMethod( env,
			utilClass, getMd5SignatureMethodID, context);

	const char *md5Sign_cstr = (*env)->GetStringUTFChars( env, md5Sign, JNI_FALSE );

	char *md5SignCp = malloc((strlen(md5Sign_cstr)+1) * sizeof(char));
	strcpy(md5SignCp, md5Sign_cstr);

	(*env)->ReleaseStringUTFChars( env, md5Sign, md5Sign_cstr );

	return md5SignCp;

	/**
    jthrowable excp=0;

    jclass clazz_ContextWrapper = (*env)->FindClass(env, "android/content/ContextWrapper");
    if ( 0 == clazz_ContextWrapper )
    {
        return NULL;
    }

    // 得到当前class的方法
    jmethodID mid_GetPM = (*env)->GetMethodID(env, clazz_ContextWrapper, "getPackageManager", "()Landroid/content/pm/PackageManager;" );
    if ( 0 == mid_GetPM )
    {
        return NULL;
    }

    // 得到PackageManager类
    jclass clazz_PM = (*env)->FindClass(env,"android/content/pm/PackageManager");
    if ( 0 == clazz_PM )
    {
        return NULL;
    }

    // 得到PackageManager的对象
    jobject pm = (*env)->CallObjectMethod(env, context, mid_GetPM );
    if ( 0 == pm )
    {
        return NULL;
    }

    // 得到PackageInfo类
    jclass clazz_PkgInfo = (*env)->FindClass(env,"android/content/pm/PackageInfo");
    if ( 0 == clazz_PkgInfo )
    {
        return NULL;
    }

    // 得到PackageInfo类的方法getPackageInfo
    jmethodID mid_getPkgInfo = (*env)->GetMethodID(env, clazz_PM, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    if ( 0 == mid_getPkgInfo )
    {
        return NULL;
    }

    //得到调用的第三方的包名
    jmethodID mid_GetPackageName = (*env)->GetMethodID(env, clazz_ContextWrapper, "getPackageName", "()Ljava/lang/String;" );
    jstring packageName = (jstring)(*env)->CallObjectMethod(env, context, mid_GetPackageName);

    // 得到PackageInfo类的对象
    jobject pkgInfo = (*env)->CallObjectMethod(env, pm, mid_getPkgInfo, packageName, GET_SIGNATURES );
    // 捕捉异常
    excp = (*env)->ExceptionOccurred(env);
    if ( excp )
    {
        // 清除异常
        (*env)->ExceptionClear(env);
        return NULL;
    }
    if ( 0 == pkgInfo )
    {
        return NULL;
    }

    {
        // debuggable模式为假，需要判断Signature
        jfieldID fid_signatures = (*env)->GetFieldID(env, clazz_PkgInfo, "signatures", "[Landroid/content/pm/Signature;" );
        if ( 0 == fid_signatures )
        {
            return NULL;
        }
        jobjectArray signatures = (jobjectArray)(*env)->GetObjectField(env, pkgInfo, fid_signatures );
        if ( 0 == signatures )
        {
            return NULL;
        }
        // 得到Signature类
        jclass clazz_Signature = (*env)->FindClass(env, "android/content/pm/Signature" );
        if ( 0 == clazz_Signature )
        {
            return NULL;
        }

        // 得到类Signature的toByteArray方法
        jmethodID mid_toByteArray = (*env)->GetMethodID(env, clazz_Signature, "toByteArray", "()[B" );
        if ( 0 == mid_toByteArray )
        {
            return NULL;
        }
        // 得到Signature数组的第一个元素
        jobject sig = (*env)->GetObjectArrayElement(env, signatures, 0 );
        if ( 0 == sig )
        {
            return NULL;
        }
        // 得到byte数组
        jbyteArray byteArray = (jbyteArray)(*env)->CallObjectMethod(env,  sig, mid_toByteArray );
        if ( 0 == byteArray )
        {
            return NULL;
        }

        int byteLen = (*env)->GetArrayLength(env, byteArray);

        jbyte *pbyte = (*env)->GetByteArrayElements(env, byteArray, JNI_FALSE );

        const int len = byteLen;
        char *pSignature = (char*)malloc( ( len + 1 ) * sizeof( char ) );
        memcpy( pSignature, pbyte, len * sizeof(char) );
        pSignature[len] = '\0';

        (*env)->ReleaseByteArrayElements( env, byteArray, pbyte, 0 );

        (*env)->DeleteLocalRef( env, signatures );
        (*env)->DeleteLocalRef( env, clazz_Signature );
        (*env)->DeleteLocalRef( env, clazz_ContextWrapper );
        (*env)->DeleteLocalRef( env, clazz_PM );

        return (char*) pSignature;
    }

    return NULL;
    **/
}
