#ifndef _CRYPT_H
#define _CRYPT_H

/**
 * 获取解密工具类
 * para env
 * para cstrKey 解密时所需的key
 * return 加密工具类
 */
//jobject getCipher( JNIEnv *env, const char * cstrKey );
jobject getCipher( JNIEnv *env, const char * cstrKey , const int isDecrypt);

/**
 * 使用解密工具，对数据进行解密
 */
//jbyteArray decryptByteArray( JNIEnv *env, jobject cipherObj, jbyteArray inputByteArray, jint dataSize );
jbyteArray decryptByteArray( JNIEnv *env, jobject cipherObj, jbyteArray inputByteArray, jint dataSize );

/**
 * 解密字符串 返回值为jstring类型
 */
//jstring decryptString ( JNIEnv * env, jstring inputStr );
jstring decryptString( JNIEnv * env, jobject context, jstring inputStr );

/**
 * 使用常量字符串的key, 解密字符串, 返回值为char*类型
 */
//char* decryptStringForC ( JNIEnv * env, const char* input );
char* decryptStringForC ( JNIEnv * env, const char* input );

// 解码字符串
//char* decryptStringForC2( JNIEnv * env, const char* input );
char* decryptStringForC2( JNIEnv * env, const char* input );

// 解密字符串 使用dex解密所使用的key
//char* decryptePtrString( JNIEnv * env, const char * cstrInput )
char* decryptePtrString( JNIEnv * env, jobject context, const char * cstrInput );

const char* getDecryptFileKey(JNIEnv *env, jobject jthis);

#endif
