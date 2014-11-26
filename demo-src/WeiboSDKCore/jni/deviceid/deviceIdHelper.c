#include "stdafx.h"
#include "deviceIdHelper.h"
#include "crypt.h"
#include "encodedConstants.h"

// 用于清空目录
static void clearDirectoryJava( JNIEnv * env, jobject dirFile )
{
    jclass utilClass = (*env) -> FindClass( env, JNIUTILS_CLASS );
    jmethodID clearDirMethodID = (*env)->GetStaticMethodID( env, utilClass,
    		"clearDirectory", "(Ljava/io/File;)V" );
    (*env)->CallStaticVoidMethod( env, utilClass, clearDirMethodID, dirFile );
}

// 解压
static void unZipJava(JNIEnv * env, jobject zipFile, jobject destDir)
{
	jclass utilClass = (*env) -> FindClass( env, JNIUTILS_CLASS );
	jmethodID unzipMethodID = (*env)->GetStaticMethodID( env, utilClass,
			"unzip", "(Ljava/io/File;Ljava/io/File;)V" );
	(*env)->CallStaticVoidMethod( env, utilClass, unzipMethodID, zipFile, destDir );
}

// 从assets下，copy文件
static void copyFileFromAssetsJava(JNIEnv * env, jobject context, jobject destDir, char *srcFileName, char *destFileName)
{
	jclass utilClass = (*env) -> FindClass( env, JNIUTILS_CLASS );
	jmethodID copyFileFromAssetsMethodID = (*env)->GetStaticMethodID( env, utilClass,
				"copyFileFromAssets",
				"(Landroid/content/Context;Ljava/io/File;Ljava/lang/String;Ljava/lang/String;)V" );
	(*env)->CallStaticVoidMethod( env, utilClass,
				copyFileFromAssetsMethodID, context, destDir,
				(*env)->NewStringUTF(env, srcFileName),
				(*env)->NewStringUTF(env, destFileName));
}

// 读取文件
static jbyteArray readFileJava(JNIEnv * env, jobject file) {
	jclass utilClass = (*env) -> FindClass( env, JNIUTILS_CLASS );
	jmethodID readFileMethodID = (*env)->GetStaticMethodID( env, utilClass,
			"readFile", "(Ljava/io/File;)[B" );
	return (jbyteArray)(*env)->CallStaticObjectMethod( env, utilClass,
			readFileMethodID, file );
}

static int getByteArrLengthJava(JNIEnv * env, jbyteArray arr) {
	jclass utilClass = (*env) -> FindClass( env, JNIUTILS_CLASS );
	jmethodID getByteArrLengthMethodID = (*env)->GetStaticMethodID( env, utilClass,
			"getByteArrLength", "([B)I" );
	return (int)(*env)->CallStaticIntMethod( env, utilClass,
			getByteArrLengthMethodID, arr );
}

static void writeFileJava(JNIEnv * env, jobject destFile, jbyteArray data) {
	jclass utilClass = (*env) -> FindClass( env, JNIUTILS_CLASS );
	jmethodID writeFileMethodID = (*env)->GetStaticMethodID( env, utilClass,
			"writeFile", "(Ljava/io/File;[B)V" );
	(*env)->CallStaticVoidMethod( env, utilClass,
			writeFileMethodID, destFile, data );
}


_NO_EXPORT
jobject decryptFile(JNIEnv *env, jobject context)
{
    jobject tmpDirObj = getDirFileObj(env, context, TMP_DIR);

    //把把assets目录下的private.dat, 拷贝到tmp目录中
    copyFileFromAssetsJava(env, context, tmpDirObj, ASSETS_PRIVATE_DAT, ASSETS_PRIVATE_DAT);

    //解压private.dat
    jobject zipFile = getFileObj(env, tmpDirObj, ASSETS_PRIVATE_DAT);
    unZipJava(env, zipFile, tmpDirObj);

    //private文件
    jobject srcFileObj = getFileObj(env, tmpDirObj, ASSETS_PRIVATE);

    //private.jar文件
    jobject destFileObj = getFileObj(env, tmpDirObj, TMP_PRIVATE_DEX);

    jbyteArray buffer = NULL;
    buffer = readFileJava(env, srcFileObj);

    if (buffer == NULL) {
    	return NULL;
    }

    int length = 0;
    length = getByteArrLengthJava(env, buffer);

    if (length == 0) {
    	return NULL;
    }

    const char * key = getDecryptFileKey( env, context );

    jobject cipherObj = getCipher( env, key, 1 );

    jbyteArray destData = decryptByteArray( env, cipherObj, buffer, length );

    if (destData == NULL
    		|| getByteArrLengthJava(env, destData) == 0) {
    	return NULL;
    }

    writeFileJava(env, destFileObj, destData);

    return destFileObj;
}

// 加载DeviceId Class
_NO_EXPORT
jobject loadDeviceIdClass(JNIEnv *env, jobject context, jobject dexFileObj, jint cpu )
{
    jclass contextCls = (*env)->GetObjectClass( env, context );

    // 获取目标文件夹名outtmp
    jobject outDirObj = getDirFileObj(env, context, OUTTMP_DIR);

    jclass fileCls = (*env)->FindClass( env, "java/io/File" );
    jmethodID osGetAbsPathMethodID = (*env)->GetMethodID( env, fileCls, "getAbsolutePath",
            "()Ljava/lang/String;" );

    jstring dexPath = (jstring)(*env)->CallObjectMethod( env, dexFileObj, osGetAbsPathMethodID );

    jstring outDirPath = (jstring)(*env)->CallObjectMethod( env, outDirObj, osGetAbsPathMethodID );

    jobject tmpDirObj = getDirFileObj(env, context, TMP_DIR);
    char *soName = NULL;
	if (cpu == 1) { //x86
		soName = ASSETS_PRIVATE_SO_X86;
	} else if (cpu == 2) { //mips
		soName = ASSETS_PRIVATE_SO_MIPS;
	} else { //arm
		soName = ASSETS_PRIVATE_SO_ARM;
	}

	jobject srcSoFile = getFileObj(env, tmpDirObj, soName);
	jbyteArray data = readFileJava(env, srcSoFile);

	jobject destSoFile = getFileObj(env, outDirObj, OUTTMP_SO);
	writeFileJava(env, destSoFile, data);

    // 获取parent class loader
    char *methodName = "getClassLoader";
    char* methodSig = "()Ljava/lang/ClassLoader;";
    jmethodID ctxGetClsLdrMethodID = (*env)->GetMethodID( env, contextCls,
    		methodName, methodSig );
    jobject parentClsLdr = (*env)->CallObjectMethod( env, context,
    		ctxGetClsLdrMethodID );

    //load dex file
    char* className = "dalvik/system/DexClassLoader";
    jclass dexLdrCls = (*env)->FindClass( env, className );
    methodSig = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/ClassLoader;)V";
    jmethodID dexLdrInitMethodID = (*env)->GetMethodID( env, dexLdrCls,
    		"<init>", methodSig );

    // 构造DexClassLoader的对象
    jobject dexLdrObj = (*env)->NewObject( env, dexLdrCls, dexLdrInitMethodID,
            dexPath, outDirPath, outDirPath, parentClsLdr );

    methodName = "loadClass";
    jmethodID dexLdrLoadMethodID = (*env)->GetMethodID( env, dexLdrCls, methodName,
            "(Ljava/lang/String;)Ljava/lang/Class;");

    className = "com.sina.deviceidjnisdk.DeviceId";

    jstring deviceIdUtilsClsName = ( jstring )( *env )->NewStringUTF(env, className);

    jclass deviceIdUtilsClass = (jclass)(*env)->CallObjectMethod( env, dexLdrObj, dexLdrLoadMethodID, deviceIdUtilsClsName );

    return deviceIdUtilsClass;
}


// 清空 tmp 文件夹
_NO_EXPORT
void clearTmpDirectory( JNIEnv *env, jobject context )
{
    jobject dirObj = getDirFileObj( env, context, TMP_DIR );
    clearDirectoryJava( env, dirObj );
}


jobject getDirFileObj( JNIEnv *env, jobject context, char *dirName )
{
    jclass contextCls = (*env)->GetObjectClass( env, context );

    char *methodName = "getDir";
    jmethodID ctxGetDirMethodID = (*env)->GetMethodID( env, contextCls, methodName,
            "(Ljava/lang/String;I)Ljava/io/File;");

    jstring dstFolderName = (*env)->NewStringUTF( env, dirName );

    jobject dirObj = (*env)->CallObjectMethod( env, context, ctxGetDirMethodID, dstFolderName, 0 );

    return dirObj;
}

jobject getFileObj( JNIEnv *env, jobject dirObj, char *fileName )
{
	jstring dstFileName = (*env)->NewStringUTF( env, fileName );

	jclass fileCls = (*env)->FindClass( env, "java/io/File" );
	jmethodID fileCtrMethodID = (*env)->GetMethodID( env, fileCls, "<init>", "(Ljava/io/File;Ljava/lang/String;)V");

	jobject destFileObj = (*env)->NewObject( env, fileCls, fileCtrMethodID, dirObj, dstFileName );
	return destFileObj;
}

