#ifndef _NET_ENG_HELPER_H
#define _NET_ENG_HELPER_H_

#define ASSETS_PRIVATE_DAT  "private.dat" //assets目录下的private.dat文件(zip)
#define ASSETS_PRIVATE  "private" //assets/private.dat中的加密private.dex
#define ASSETS_PRIVATE_SO_ARM  "private1" //assets/private.dat
#define ASSETS_PRIVATE_SO_X86  "private2" //assets/private.dat
#define ASSETS_PRIVATE_SO_MIPS  "private3" //assets/private.dat

#define TMP_DIR  "tmp"
#define OUTTMP_DIR  "outtmp"

#define TMP_PRIVATE_DEX  "private.jar"
#define OUTTMP_SO  "libweibosdkcore.so"

#define JNIUTILS_CLASS  "com/sina/weibo/sdk/utils/JniUtils"

jobject decryptFile(JNIEnv *env, jobject context);

jobject loadDeviceIdClass(JNIEnv *env, jobject context, jobject dexFileObj, jint cpu);

void clearTmpDirectory( JNIEnv *env, jobject context );

jobject getDirFileObj( JNIEnv *env, jobject context, char *dirName );

jobject getFileObj( JNIEnv *env, jobject dirObj, char *fileName );

#endif
