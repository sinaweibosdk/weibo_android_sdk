#include "stdafx.h"
#include "crypt.h"
#include "encodedConstants.h"
#include "utils.h"
#include "configLoader.h"

#define GET_SIGNATURES 0x00000040
#define DECRYPT_KEY_LEN 8

// 提取解密常量字符串所需的key
static char* getKey( JNIEnv *env ) //xls3Bn84
{
    char * CHAR_LIST = getConstantString2( env, HEX_CHAR_LIST );//axblcsd3eBfng8h4iSkElDm

    const int BUF_SIZE = 9;
    char * keyBuf = ( char* ) malloc( BUF_SIZE * sizeof( char ) );
    keyBuf[0] = CHAR_LIST[1];
    keyBuf[1] = CHAR_LIST[3];
    keyBuf[2] = CHAR_LIST[5];
    keyBuf[3] = CHAR_LIST[7];
    keyBuf[4] = CHAR_LIST[9];
    keyBuf[5] = CHAR_LIST[11];
    keyBuf[6] = CHAR_LIST[13];
    keyBuf[7] = CHAR_LIST[15];
    keyBuf[8] = '\0';

    free( CHAR_LIST );
    return keyBuf;
}

// 提取标识解密方式的字符串
static char* getType( JNIEnv *env ) //DES
{
    char * CHAR_LIST = getConstantString2( env, HEX_CHAR_LIST ); //axblcsd3eBfng8h4iSkElDm

    const int BUF_SIZE = 4;
    char *buf = ( char* )malloc( BUF_SIZE * sizeof( char ) );
    buf[0] = CHAR_LIST[21];
    buf[1] = CHAR_LIST[19];
    buf[2] = CHAR_LIST[17];
    buf[3] = '\0';

    free( CHAR_LIST );

    return buf;
}

/**
 * 得到签名，MD5之后，取最后8位
 */
_NO_EXPORT
const char* getDecryptFileKey(JNIEnv *env, jobject jthis)
{
#ifdef _DEBUG
    static char decryptKey[] = "704e6c1b";
    return decryptKey;
#else
    static char decryptKey[DECRYPT_KEY_LEN + 1];

    if( strlen( decryptKey ) != 0 ) {
        return decryptKey;
    }

    char *pMD5 = getMd5SignatrueString( env, jthis );

    int len = strlen( pMD5 );
    if ( len != 0 )
    {
        int i;
        for( i = 0; i < len; ++i )
        {
            pMD5[i] = tolower( pMD5[i] );
        }

        strcpy( decryptKey, pMD5 + len - DECRYPT_KEY_LEN );

        free(pMD5);

        return decryptKey;
    }
    else {
        (*env)->ThrowNew( env, jthis, NULL );
    }

    free(pMD5);

    return NULL;
#endif
}

// 获取加密工具类
//jobject getCipher( JNIEnv *env, const char * cstrKey )
_NO_EXPORT
jobject getCipher( JNIEnv *env, const char * cstrKey ,const int isDecrypt)
{
    PUSH_LOCAL_FRAME( NULL );

    LOGE( "getCipher in");
    // 解密的key
    int keyLen = strlen( cstrKey );
    jbyteArray key = (*env)->NewByteArray( env, keyLen );
    (*env)->SetByteArrayRegion( env, key, 0, keyLen, (jbyte*)cstrKey );

    // 获取解密方式
    char * decryptType = getType( env ); //DES
    jstring cryptType = (*env)->NewStringUTF( env, decryptType );
    free( decryptType );

    // 加载类javax/crypto/spec/SecretKeySpec
    char *clsName = getConstantString2( env, HEX_JAVAX_CRYPTO_SPEC_SECRETKEYSPEC ); // javax/crypto/spec/SecretKeySpec
    jclass secretKeySpecCls = (*env)->FindClass( env, clsName );
    free( clsName );
    if ( (*env)->ExceptionOccurred( env ) != NULL )
    {
        (*env)->ExceptionClear( env );
        LOGE( "getCipher exception" );
    }

    // 获取构造函数之method ID
    jmethodID secretKeySpecCtrMethodID = (*env)->GetMethodID( env, secretKeySpecCls, "<init>",
            "([BLjava/lang/String;)V" );
    jobject secretKeySpecObj = (*env)->NewObject( env, secretKeySpecCls, secretKeySpecCtrMethodID, key, cryptType );

    // 加载类javax/crypto/Cipher
    clsName = getConstantString2( env, HEX_JAVAX_CRYPTO_CIPHER );
    jclass cipherCls = (*env)->FindClass( env, clsName );
    free( clsName );
    char *methodName = getConstantString2( env, HEX_GETINSTANCE ); //"getInstance"
    char *methodSig = getConstantString2( env, HEX__LJAVA_LANG_STRING_LJAVAX_CRYPTO_CIPHER ); //"(Ljava/lang/String;)Ljavax/crypto/Cipher;"
    jmethodID cipherGetInstatnceMethodID = (*env)->GetStaticMethodID( env, cipherCls, methodName,
            methodSig );
    free( methodName );
    free( methodSig );
    // 构造cipher对象
    jobject cipherObj = (*env)->CallStaticObjectMethod( env, cipherCls, cipherGetInstatnceMethodID, cryptType );

    char *mode;
    if(isDecrypt)
    		{
    		mode = getConstantString2( env, HEX_DECRYPT_MODE );//"DECRYPT_MODE"
    		}
    else
    		{
    		mode = getConstantString2( env, HEX_ENCRYPT_MODE );//"ENCRYPT_MODE"
    		}
    jfieldID modeFid = (*env)->GetStaticFieldID(env, cipherCls, mode, "I");
    free( mode );
    const jint CIPHER_MODE = (*env)->GetStaticIntField( env, cipherCls, modeFid );

    // 调用cipher.init, 执行初始化
    methodName = getConstantString2( env, HEX_INIT ); // "init"
    methodSig = getConstantString2( env, HEX__ILJAVA_SECURITY_KEY_V );// "(ILjava/security/Key;)V"
    jmethodID cipherInitMethodID = (*env)->GetMethodID( env, cipherCls, methodName, methodSig );
    free( methodName );
    free( methodSig );
    (*env)->CallVoidMethod( env, cipherObj, cipherInitMethodID, CIPHER_MODE, secretKeySpecObj );

    POP_LOCAL_FRAME( cipherObj );

    LOGE( "getCipher out");
    return cipherObj;
}

/**decrypt binary stream
 * return jbyteArray output byte array
 */
//jbyteArray decryptByteArray( JNIEnv *env, jobject cipherObj, jbyteArray inputByteArray, jint dataSize )
_NO_EXPORT
jbyteArray decryptByteArray( JNIEnv *env, jobject cipherObj, jbyteArray inputByteArray, jint dataSize )
{
    PUSH_LOCAL_FRAME( NULL );

    LOGE( "decryptByteArray in");
    // 获取cipher
    jclass cipherCls = (*env)->GetObjectClass( env, cipherObj );

    // 获取解密方法之ID
    char *methodName = getConstantString2( env, HEX_DOFINAL );
    jmethodID cipherDofinalMethodID = (*env)->GetMethodID( env, cipherCls, methodName,
            "([BII)[B" );
    free( methodName );
    // 解密
    jbyteArray decodedByteArray =
            (*env)->CallObjectMethod( env, cipherObj, cipherDofinalMethodID, inputByteArray, 0, dataSize );

    POP_LOCAL_FRAME( decodedByteArray );

    LOGE( "decryptByteArray out");

    return decodedByteArray;
}

// 对16进制编码的字符串 进行解码 返回值为byte
static jbyteArray hex2byteNative( JNIEnv * env, jbyteArray input )
{
    LOGE( "hex2byte2 in");
    jsize length = (*env)->GetArrayLength( env, input );
    if ( length % 2 != 0 )
        return NULL;

    char* srcBuf = (char*)(*env)->GetByteArrayElements( env, input, JNI_FALSE );
    char *dstBuf = (char*)malloc( ( length / 2 + 1 ) * sizeof( char ) );
    char *tmpBuf = ( char* ) malloc( 3 * sizeof( char ) );
    tmpBuf[2] = '\0';

    jsize i = 0;
    char *p = srcBuf;
    for( ; i < length; i += 2, p += 2 )
    {
        memcpy( tmpBuf, p, 2 );
        long tmpL = strtol( tmpBuf, NULL, 16);
        dstBuf[ i / 2 ] = (char)tmpL;
    }
    free( tmpBuf );
    (*env)->ReleaseByteArrayElements( env, input, (jbyte*)srcBuf, 0 );

    dstBuf[ length / 2 ] = '\0';
    LOGE( dstBuf );

    jbyteArray dstByteArray = (*env)->NewByteArray( env, length / 2 );
    (*env)->SetByteArrayRegion( env, dstByteArray, 0, length/2, (jbyte*)dstBuf );

    free( dstBuf );
    LOGE( "hex2byte2 out");

    return dstByteArray;
}

// 解密字符串
//char* decryptePtrString( JNIEnv * env, const char * cstrInput )
_NO_EXPORT
char* decryptePtrString( JNIEnv * env, jobject context, const char * cstrInput )
{
    PUSH_LOCAL_FRAME( NULL );

    int srcLen = strlen( cstrInput );
    jbyteArray srcByteArray = (*env)->NewByteArray( env, srcLen );
    (*env)->SetByteArrayRegion( env, srcByteArray, 0, srcLen, cstrInput );

    jbyteArray encodedByteArray = hex2byteNative( env, srcByteArray );
    jsize encodedBytesLen = (*env)->GetArrayLength( env, encodedByteArray );

    const char *key = getDecryptFileKey( env, context );
    jobject cipher = getCipher( env, key ,1);
    jbyteArray decodedByteArray = decryptByteArray( env, cipher, encodedByteArray, encodedBytesLen );

    int dstLen = (*env)->GetArrayLength( env, decodedByteArray );
    jbyte* jbyteBuf = (*env)->GetByteArrayElements( env, decodedByteArray, JNI_FALSE );

    char *pRet = ( char* ) malloc( ( dstLen + 1 ) * sizeof( char ) );
    memcpy( pRet, jbyteBuf, dstLen );
    pRet[ dstLen ] = '\0';

    (*env)->ReleaseByteArrayElements( env, decodedByteArray, jbyteBuf, 0 );

    jstring outputStr = (*env)->NewStringUTF( env, pRet );

    LOGE( pRet );

    POP_LOCAL_FRAME_NULL();

    return pRet;
}

// 解密字符串 返回jstring类型
//jstring decryptString ( JNIEnv * env, jstring inputStr )
_NO_EXPORT
jstring decryptString( JNIEnv * env, jobject context, jstring inputStr )
{
    PUSH_LOCAL_FRAME( NULL );

    LOGE( "decryptString in");

    const char *pSrc = (*env)->GetStringUTFChars( env, inputStr, JNI_FALSE );
    char *pRet = decryptePtrString( env, context, pSrc );
    jstring outputStr = (*env)->NewStringUTF( env, pRet );
    free( pRet );
    (*env)->ReleaseStringUTFChars( env, inputStr, pSrc );

    LOGE( "decryptString out");

    POP_LOCAL_FRAME( outputStr );

    return outputStr;
}

/**
 * 使用解密常量字符串的key 执行解密
 */
//char* decryptStringForC ( JNIEnv * env, const char* input )
_NO_EXPORT
char* decryptStringForC ( JNIEnv * env, const char* input )
{
    PUSH_LOCAL_FRAME( NULL );

    LOGE( "decryptStringForC in" );
    LOGE( input );

    int srcLen = strlen( input );
    jbyteArray srcByteArray = (*env)->NewByteArray( env, srcLen );
    (*env)->SetByteArrayRegion( env, srcByteArray, 0, srcLen, input );

    jbyteArray encodedByteArray = hex2byteNative( env, srcByteArray );
    jsize encodedBytesLen = (*env)->GetArrayLength( env, encodedByteArray );

    // 获取key
    char *decodeLitKey = getKey( env );
    // 获取解密工具类
    jobject cipher = getCipher( env, decodeLitKey ,1);
    free( decodeLitKey );

    // 执行解密
    jbyteArray decodedByteArray = decryptByteArray( env, cipher, encodedByteArray, encodedBytesLen );

    // 将解密结果拷贝到堆
    int dstlen = (*env)->GetArrayLength( env, decodedByteArray );
    jbyte* jbyteBuf = (*env)->GetByteArrayElements( env, decodedByteArray, JNI_FALSE );
    LOGE( ( char* ) jbyteBuf );

    char *pRet = ( char* ) malloc( ( dstlen + 1 ) * sizeof( char ) );
    memcpy( pRet, jbyteBuf, dstlen );
    (*env)->ReleaseByteArrayElements( env, decodedByteArray, jbyteBuf, 0 );
    pRet[ dstlen ] = '\0';

    POP_LOCAL_FRAME_NULL();

    LOGE( pRet );
    LOGE( "decryptStringForC out" );
    return pRet;
}

//char* decryptStringForC2( JNIEnv * env, const char* input )
_NO_EXPORT
char* decryptStringForC2( JNIEnv * env, const char* input )
{
    PUSH_LOCAL_FRAME( NULL );

    LOGE( "decryptStringForC2 in" );
    LOGE( input );
    int srcLen = strlen( input );
    jbyteArray srcByteArray = (*env)->NewByteArray( env, srcLen );
    (*env)->SetByteArrayRegion( env, srcByteArray, 0, srcLen, input );

    jbyteArray decodedByteArray = hex2byteNative( env, srcByteArray );

    int dstlen = (*env)->GetArrayLength( env, decodedByteArray );
    jbyte* jbyteBuf = (*env)->GetByteArrayElements( env, decodedByteArray, JNI_FALSE );
    LOGE( ( char* ) jbyteBuf );

    char *pRet = ( char* ) malloc( ( dstlen + 1 ) * sizeof( char ) );
    memcpy( pRet, jbyteBuf, dstlen );
    (*env)->ReleaseByteArrayElements( env, decodedByteArray, jbyteBuf, 0 );
    pRet[ dstlen ] = '\0';

    POP_LOCAL_FRAME_NULL();

    LOGE( pRet );

    LOGE( "decryptStringForC2 out" );
    return pRet;
}

