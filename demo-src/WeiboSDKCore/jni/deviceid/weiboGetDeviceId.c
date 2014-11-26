#include <string.h>
#include <jni.h>
#include <stdio.h>

#include <stdlib.h>
#include <unistd.h>
#include "stdafx.h"
#include "crypt.h"
#include "encodedConstants.h"
#include "configLoader.h"
#include "deviceIdHelper.h"
#include <time.h>

/**
 * 新浪内部应用的MD5签名
 */
#define SIGS_LEN  13

static char *g_pStandardSigs[SIGS_LEN] = {
		"c756f5460ac7745bd562c5ea19457889", // "com.sina.weibo.sdk.demo"
		"18da2bf10352443a00a5e046d9fca6bd", // "com.sina.wemusic"
		"18da2bf10352443a00a5e046d9fca6bd", // "sina.mobile.tianqitong"
		"18da2bf10352443a00a5e046d9fca6bd", // "com.sina.book"
		"18da2bf10352443a00a5e046d9fca6bd", // "com.sina.hotweibo"
		"67c4d11e241ff6b19462172c7e559753", // "com.sina.sinablog"
		"89e489c4a6d6e2e6f3cfd1ddb1ff87bf", // com.gau.go.launcherex
		"18da2bf10352443a00a5e046d9fca6bd", // com.sina.app.weiboheadline
		"18da2bf10352443a00a5e046d9fca6bd", // com.sina.news
		"c18a5b72454f7381f7508b5552b0ea1b", // com.sina.weibo.game.football
		"67c4d11e241ff6b19462172c7e559753", // "cn.com.sina.finance"
		"18da2bf10352443a00a5e046d9fca6bd", // "com.weibo.wemusic"
		"18da2bf10352443a00a5e046d9fca6bd", // com.weibo.freshcity
};

static char *privateKey = "edafw2436ef8a3t4"; //用于计算oauth_sign值


static char *getIValue(char *inputStr);


/**
 * 检查签名是否合法
 */
static int check(JNIEnv *env, jobject context)
{
	char *pMD5 = getMd5SignatrueString( env, context );

	if( pMD5 == NULL || strlen( pMD5 ) == 0 ) {
		return 0;
	}
	
	int i;
	for(i=0; i<SIGS_LEN; i++) {
		char *sig = g_pStandardSigs[i];
		if ( 0 == strcasecmp( sig, pMD5 ) )
		{
			free(pMD5);
			return 1;
		}
	}

	free(pMD5);
	return 0;
}

/*
 * 获取TelephonyManager 对象
 * 参数： Context 为Context对象
 */
static jobject getTelephonyManagerObj(JNIEnv *env, jobject clz, jobject Context)
{
    jclass jCtxClz= (*env)->FindClass(env,"android/content/Context");
    jfieldID fid_telephony_service = (*env)->GetStaticFieldID(env,jCtxClz,"TELEPHONY_SERVICE","Ljava/lang/String;");
    jstring  jstr_telephony_serveice = (jstring)(*env)->GetStaticObjectField(env,jCtxClz,fid_telephony_service);

    jclass jclz = (*env)->GetObjectClass(env,Context);

    //反射
    char *methodName = getConstantString( env, GETSYSTEMSERVICE );
    jmethodID  mid_getSystemService = (*env)->GetMethodID(env,jclz,methodName,"(Ljava/lang/String;)Ljava/lang/Object;");
    LOGE(methodName);
    free( methodName );

    jobject TelephonyManager = (*env)->CallObjectMethod(env,Context,mid_getSystemService,jstr_telephony_serveice);

    (*env)->DeleteLocalRef(env,jCtxClz);
    (*env)->DeleteLocalRef(env,jclz);
    (*env)->DeleteLocalRef(env,jstr_telephony_serveice);

    return TelephonyManager;
}

/*
 * 获取IMEI地址
 * 参数：TelephonyManagerObj
 */
static char* getImei(JNIEnv *env, jobject TelephonyManagerObj)
{
	if(TelephonyManagerObj == NULL){
		return NULL;
	}
	jclass jclz = (*env)->GetObjectClass(env,TelephonyManagerObj);
	jmethodID mid = (*env)->GetMethodID(env,jclz,"getDeviceId","()Ljava/lang/String;");
	jstring jstr_imei = (jstring)(*env)->CallObjectMethod(env,TelephonyManagerObj,mid);
	if(jstr_imei == NULL){
		(*env)->DeleteLocalRef(env,jclz);
		return NULL;
	}

	const char* tmp = (*env)->GetStringUTFChars(env,jstr_imei, NULL);
	char* imei = (char*) malloc(strlen(tmp)+1);
	memcpy(imei,tmp,strlen(tmp)+1);
	(*env)->ReleaseStringUTFChars(env,jstr_imei, tmp);
	(*env)->DeleteLocalRef(env,jclz);
	return imei;
}

/*
 * 获取IMSI地址
 * 参数：TelephonyManagerObj
 */
static char* getImsi(JNIEnv *env, jobject TelephonyManagerObj)
{
	if(TelephonyManagerObj == NULL){
		return NULL;
	}
	jclass jclz = (*env)->GetObjectClass(env,TelephonyManagerObj);
	jmethodID mid = (*env)->GetMethodID(env,jclz,"getSubscriberId","()Ljava/lang/String;");
	jstring jstr_imsi = (jstring)(*env)->CallObjectMethod(env,TelephonyManagerObj,mid);
	if(jstr_imsi == NULL){
		(*env)->DeleteLocalRef(env,jclz);
		return NULL;
	}

	const char* tmp = (*env)->GetStringUTFChars(env,jstr_imsi, NULL);
	char* imsi = (char*) malloc(strlen(tmp)+1);
	memcpy(imsi,tmp,strlen(tmp)+1);
	(*env)->ReleaseStringUTFChars(env,jstr_imsi, tmp);
	(*env)->DeleteLocalRef(env,jclz);
	return imsi;
}

/*
 * 获取WifiManager 对象
 * 参数： Context 为Context对象
 */
static jobject getWifiManagerObj(JNIEnv *env, jobject clz, jobject Context)
{
//	LOGE("gotWifiMangerObj ");
    //获取 Context.WIFI_SERVICE 的值
    //jstring  jstr_wifi_serveice = env->NewStringUTF("wifi");
    jclass jCtxClz= (*env)->FindClass(env,"android/content/Context");
    jfieldID fid_wifi_service = (*env)->GetStaticFieldID(env,jCtxClz,"WIFI_SERVICE","Ljava/lang/String;");
    jstring  jstr_wifi_serveice = (jstring)(*env)->GetStaticObjectField(env,jCtxClz,fid_wifi_service);

    jclass jclz = (*env)->GetObjectClass(env,Context);

    //反射
    char *methodName = getConstantString( env, GETSYSTEMSERVICE );
    jmethodID  mid_getSystemService = (*env)->GetMethodID(env,jclz,methodName,"(Ljava/lang/String;)Ljava/lang/Object;");
    LOGE(methodName);
    free( methodName );

    jobject wifiManager = (*env)->CallObjectMethod(env,Context,mid_getSystemService,jstr_wifi_serveice);

    //因为jclass 继承自 jobject，所以需要释放；
    //jfieldID、jmethodID是内存地址，这段内存也不是在我们代码中分配的，不需要我们来释放。
    (*env)->DeleteLocalRef(env,jCtxClz);
    (*env)->DeleteLocalRef(env,jclz);
    (*env)->DeleteLocalRef(env,jstr_wifi_serveice);

    return wifiManager;
}

/*
 * 获取WifiInfo 对象
 * 参数： wifiMgrObj 为WifiManager对象
 */
static jobject getWifiInfoObj(JNIEnv *env, jobject wifiMgrObj)
{
	if(wifiMgrObj == NULL){
		return NULL;
	}
	jclass jclz = (*env)->GetObjectClass(env,wifiMgrObj);
	jmethodID mid = (*env)->GetMethodID(env,jclz,"getConnectionInfo","()Landroid/net/wifi/WifiInfo;");
	jobject wifiInfo = (*env)->CallObjectMethod(env,wifiMgrObj,mid);

	(*env)->DeleteLocalRef(env,jclz);
	return wifiInfo;
}

/*
 * 获取MAC地址
 * 参数：wifiInfoObj， WifiInfo的对象
 */
static char* getMacAddress(JNIEnv *env, jobject wifiInfoObj)
{
	if(wifiInfoObj == NULL){
		return NULL;
	}
	jclass jclz = (*env)->GetObjectClass(env,wifiInfoObj);
	jmethodID mid = (*env)->GetMethodID(env,jclz,"getMacAddress","()Ljava/lang/String;");
	jstring jstr_mac = (jstring)(*env)->CallObjectMethod(env,wifiInfoObj,mid);
	if(jstr_mac == NULL){
		(*env)->DeleteLocalRef(env,jclz);
		return NULL;
	}

	const char* tmp = (*env)->GetStringUTFChars(env,jstr_mac, NULL);
	char* mac = (char*) malloc(strlen(tmp)+1);
	memcpy(mac,tmp,strlen(tmp)+1);
	(*env)->ReleaseStringUTFChars(env,jstr_mac, tmp);
	(*env)->DeleteLocalRef(env,jclz);
	return mac;
}

static char* getDid(JNIEnv *env , jobject thiz, jobject Context)
{
	//imei imsi
	jobject TelephonyManagerObj = NULL ;
	TelephonyManagerObj = getTelephonyManagerObj(env, thiz, Context);
	char * imsi = getImsi(env,TelephonyManagerObj);
	if(imsi == NULL)
		imsi = "";
//	LOGE("---------------------------------");
//	LOGE("imsi: ");
//	LOGE(imsi);
//	LOGE("---------------------------------");
	char * imei = getImei(env,TelephonyManagerObj);
	if(imei == NULL)
		imei = "";
//	LOGE("---------------------------------");
//	LOGE("imei: ");
//	LOGE(imei);
//	LOGE("---------------------------------");
	//获取mac
	jobject wifiManagerObj = NULL ;
	jobject wifiInfoObj = NULL;

	wifiManagerObj = getWifiManagerObj(env, thiz, Context);
	wifiInfoObj = getWifiInfoObj(env,wifiManagerObj);
	char * mac = getMacAddress(env,wifiInfoObj);
//	//如果手机从开机之后就没有用过wifi，那么mac地址将为空，
//	//下面的这一段代码就是用来解决这个问题的。
//	if(mac == NULL){
//		enableWifi(env,wifiManagerObj);
//		for(int i=0; i<10 && mac==NULL; i++){
//			sleep(1);
//			(*env)->DeleteLocalRef(env,wifiInfoObj);
//			wifiInfoObj = getWifiInfoObj(env,wifiManagerObj);
//			mac = getMacAddress(env,wifiInfoObj);
//			LOGI("%d, mac= %s",i,mac);
//		}
//		disableWifi(env,wifiManagerObj);
//	}
//	//-----------------------------------------
	if(mac == NULL)
		mac = "";
//	LOGE("---------------------------------");
//	LOGE("mac: ");
//	LOGE(mac);
//	LOGE("---------------------------------");

	char beforeMd5[128] = "";

	strcat(beforeMd5,imei);
	strcat(beforeMd5,imsi);
	strcat(beforeMd5,mac);

//	LOGE("---------------------------------");
//	LOGE("beforeMd5: ");
//	LOGE(beforeMd5);
//	LOGE("---------------------------------");

	char *didstring = (char*) malloc(34);
	sprintf(didstring,"%s",MDString( beforeMd5 ));

	(*env)->DeleteLocalRef(env,wifiInfoObj);
	(*env)->DeleteLocalRef(env,wifiManagerObj);
	(*env)->DeleteLocalRef(env,TelephonyManagerObj);

	if(strlen(imei)>1)
		free(imei);
	if(strlen(imsi)>1)
		free(imsi);
	if(strlen(mac)>1)
		free(mac);

	return didstring;

}

static char* getTime()
{
	time_t timer;
	struct tm *tblock;
	timer=time(NULL);
	tblock=localtime(&timer);

//	LOGE("---------------------------------");
//	LOGE("Local time is: ");
//	LOGE(asctime(tblock));
//	LOGE("---------------------------------");

	char *p=(char*) malloc(128);
	sprintf(p,"%04d%02d%02d",tblock->tm_year + 1900,tblock->tm_mon+1,tblock->tm_mday);
//	LOGE("---------------------------------");
//	LOGE("p: ");
//	LOGE(p);
//	LOGE("---------------------------------");

	return p; //得到日历时间
}


static char* getCheckId(JNIEnv *env , jobject thiz, jobject Context)
{
	char beforeMd5[128] = "";
	char* did = getDid(env,thiz,Context);
	char* time = getTime();

	jclass deviceIdClass = (*env)->GetObjectClass(env, thiz);
	jmethodID genCheckIdMethodId = (*env)->GetMethodID(
			env, deviceIdClass, "genCheckId",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");

	jstring jstr = (jstring)(*env)->CallObjectMethod(env, thiz, genCheckIdMethodId,
			(*env)->NewStringUTF( env, did ),
			(*env)->NewStringUTF( env, time ),
			(*env)->NewStringUTF( env, "hongtaok")
			);

	const char* cstr = (*env) -> GetStringUTFChars(env, jstr, NULL);

	strcat(beforeMd5, cstr);

//	strcat(beforeMd5,did);
//	strcat(beforeMd5,time);
//	strcat(beforeMd5,"hongtaok");

//	LOGE("---------------------------------");
//	LOGE("getCheckId beforeMd5 is: ");
//	LOGE(beforeMd5);
//	LOGE("---------------------------------");

	char *checkidString = (char*) malloc(34);
	sprintf(checkidString,"%s",MDString( beforeMd5 ));

//	LOGE("---------------------------------");
//	LOGE("getCheckId CheckId is: ");
//	LOGE(checkidString);
//	LOGE("---------------------------------");

	(*env)->ReleaseStringUTFChars(env, jstr, cstr);
	free(did);
	free(time);
	return checkidString;
}

static char* getDeviceId(JNIEnv *env , jobject thiz, jobject Context)
{

//	strcat(DeviceId,getDid(env,thiz,Context));

	char* did = getDid(env,thiz,Context);
	char* CheckId = getCheckId(env,thiz,Context);
//	sprintf(DeviceId,"%s%s",getDid(env,thiz,Context),CheckId+24);
	char *DeviceId=(char*) malloc(128);
	sprintf(DeviceId,"%s%s",did,CheckId+24);

//	LOGE("---------------------------------");
//	LOGE("getDeviceId CheckId is: ");
//	LOGE(CheckId);
//	LOGE("---------------------------------");
//
//	LOGE("---------------------------------");
//	LOGE("getDeviceId did is: ");
//	LOGE(did);
//	LOGE("---------------------------------");
//
//	LOGE("---------------------------------");
//	LOGE("getDeviceId DeviceId is: ");
//	LOGE(DeviceId);
//	LOGE("---------------------------------");

	free(did);
	free(CheckId);
	return DeviceId;
}


jobject
Java_com_sina_deviceidjnisdk_DeviceIdFactory_getInstanceNative( JNIEnv* env, jobject thiz, jobject context, jint cpu )
{
	if(check(env, context) == 0){
		(*env)->ThrowNew(env, thiz, NULL);
	}

	clearTmpDirectory( env, context );

	//decrypt dex file
	jobject dexFile = decryptFile( env, context );

	//load dex file and class definition
	jclass deviceIdClass = (jclass)loadDeviceIdClass( env, context, dexFile, cpu );

	char *contructSig = "(Landroid/content/Context;)V";
	jmethodID deviceIdClassMethodID = (*env)->GetMethodID( env, deviceIdClass, "<init>", contructSig );

	jobject deviceIdObj = (*env)->NewObject( env, deviceIdClass, deviceIdClassMethodID, context );

	clearTmpDirectory( env, context );

	return deviceIdObj;
}

jstring
Java_com_sina_deviceidjnisdk_DeviceId_getDeviceIdNative( JNIEnv* env,
                                                  jobject thiz, jobject context )
{
	if(check(env, context) == 0){
		(*env)->ThrowNew(env, thiz, NULL);
	}

	//getCheckId 测试 ok
	char *outString = getDeviceId(env,thiz,context);
	jstring result = (*env)->NewStringUTF(env, outString);
	free(outString);
	return result;

//获取时间 测试 OK
//	char *outString = getTime();
//	return (*env)->NewStringUTF(env, outString);
//GetDid 测试 ok
//	char *outString = getDid(env,thiz,Context);
//	return (*env)->NewStringUTF(env, outString);

	//imsi 测试 ok
//	jobject TelephonyManagerObj = NULL ;
//	TelephonyManagerObj = getTelephonyManagerObj(env, thiz, Context);
//	char * imsi = getImsi(env,TelephonyManagerObj);
//	jstring result;
//	if(imsi != NULL){
//		result = (*env)->NewStringUTF(env,imsi);
//		free(imsi);
//	}else{
//		result = (*env)->NewStringUTF(env,"");
//	}
//	(*env)->DeleteLocalRef(env,TelephonyManagerObj);
//	return result;
/*

//imei测试 ok
	jobject TelephonyManagerObj = NULL ;
	TelephonyManagerObj = getTelephonyManagerObj(env, thiz, Context);
	char * imei = getImei(env,TelephonyManagerObj);
	jstring result;
	if(imei != NULL){
		result = (*env)->NewStringUTF(env,imei);
		free(imei);
	}else{
		result = (*env)->NewStringUTF(env,"");
	}
	(*env)->DeleteLocalRef(env,TelephonyManagerObj);
	return result;

//MD5测试 OK
	char *outString = MDString( "hello" );
	return (*env)->NewStringUTF(env, outString);

//反射测试 OK
    char *methodName = getConstantString( env, GETSYSTEMSERVICE );
    return (*env)->NewStringUTF(env, methodName);


	//获取mac地址 OK
	jobject wifiManagerObj = NULL ;
	jobject wifiInfoObj = NULL;

	wifiManagerObj = getWifiManagerObj(env, thiz, Context);
	wifiInfoObj = getWifiInfoObj(env,wifiManagerObj);
	char * mac = getMacAddress(env,wifiInfoObj);
//	//如果手机从开机之后就没有用过wifi，那么mac地址将为空，
//	//下面的这一段代码就是用来解决这个问题的。
//	if(mac == NULL){
//		enableWifi(env,wifiManagerObj);
//		for(int i=0; i<10 && mac==NULL; i++){
//			sleep(1);
//			(*env)->DeleteLocalRef(env,wifiInfoObj);
//			wifiInfoObj = getWifiInfoObj(env,wifiManagerObj);
//			mac = getMacAddress(env,wifiInfoObj);
//			LOGI("%d, mac= %s",i,mac);
//		}
//		disableWifi(env,wifiManagerObj);
//	}
//	//-----------------------------------------

	jstring result;
	if(mac != NULL){
		result = (*env)->NewStringUTF(env,mac);
		free(mac);
	}else{
		result = (*env)->NewStringUTF(env,"");
	}
	(*env)->DeleteLocalRef(env,wifiInfoObj);
	(*env)->DeleteLocalRef(env,wifiManagerObj);
	return result;
*/
}

/**
 * 计算OauthSign值，在接口中必传
 *
 * 算法同I值算法
 *
 */
jstring Java_com_sina_weibo_sdk_net_HttpManager_calcOauthSignNative( JNIEnv* env, jobject thiz, jobject context,
		jstring part1, jstring part2 ) {
	PUSH_LOCAL_FRAME( NULL );

	char *cpart1 = (char *)(*env)->GetStringUTFChars( env, part1, JNI_FALSE );
	char *cpart2 = (char *)(*env)->GetStringUTFChars( env, part2, JNI_FALSE );

	int len = strlen(cpart1)+strlen(privateKey)+strlen(cpart2)+1;
	char *result = malloc(sizeof(char) * len);
	strcpy(result, cpart1);
	strcat(result, privateKey);
	strcat(result, cpart2);
	result[len-1] = '\0';

	char *ivalue = getIValue(result);

	jstring strIValue = (*env)->NewStringUTF( env, ivalue );

	free(result);
	(*env)->ReleaseStringUTFChars( env, part2, cpart2 );
	(*env)->ReleaseStringUTFChars( env, part1, cpart1 );

	POP_LOCAL_FRAME( strIValue );

	return strIValue;
}


static char *getIValue(char *inputStr) {

	char *outString = MDString( inputStr );

	const int BUF_SIZE = 32;
	char *ivalue = (char *)malloc( BUF_SIZE * sizeof( char ) );
	memset( ivalue, '\0', BUF_SIZE );
	strcpy( ivalue, outString + strlen(outString) - 6 );
	memcpy( ivalue + 6, ivalue, 4 );

	// second MD5
	outString = MDString( ivalue );
	ivalue[6] = outString[strlen(outString) - 1];
	ivalue[7] = '\0';

	return ivalue;
}


jstring Java_com_sina_deviceidjnisdk_DeviceIdFactory_getIValueNative( JNIEnv* env, jobject thiz, jobject context, jstring deviceSerial ) {
	PUSH_LOCAL_FRAME( NULL );

    if ( check( env, context ) == 0 )
    {
        (*env)->ThrowNew( env, context, NULL );
    }

	char *inputBytes = (char *)(*env)->GetStringUTFChars( env, deviceSerial, JNI_FALSE );

	char *ivalue = getIValue(inputBytes);

	(*env)->ReleaseStringUTFChars( env, deviceSerial, inputBytes );

    jstring strIValue = (*env)->NewStringUTF( env, ivalue );

    POP_LOCAL_FRAME( strIValue );

    return strIValue;
}


jstring Java_com_sina_deviceidjnisdk_DeviceIdFactory_calculateM(JNIEnv* env, jobject thiz,
		jobject context, jstring pin, jstring srcArray)
{
    if(check(env,context) == 0)
    {
         return srcArray;
    }

    PUSH_LOCAL_FRAME( NULL );

    const char *cpin = (*env)->GetStringUTFChars( env, pin, JNI_FALSE );
    const char* aryText = (*env)->GetStringUTFChars(env, srcArray, JNI_FALSE);

    int tempLen = strlen(aryText) + strlen(cpin) + 1;
    char* temp = (char*)malloc( tempLen * sizeof(char));
    memset( temp,0, tempLen );
    strcpy(temp, aryText);
    strcat(temp, cpin);

    char* md5 = MDString(temp);
    char* m = (char*)malloc(sizeof(char) * 9);
    int len = strlen(m);
    m[0] = md5[1];
    m[1] = md5[5];
    m[2] = md5[2];
    m[3] = md5[10];
    m[4] = md5[17];
    m[5] = md5[9];
    m[6] = md5[25];
    m[7] = md5[27];
    m[8] = '\0';
    // change char* to jstring
    jclass strClass =  (*env)->FindClass(env, "java/lang/String");

    jmethodID methodID = (*env)->GetMethodID(env, strClass, "<init>", "([BLjava/lang/String;)V");



    jbyteArray bytes_time = (*env)->NewByteArray(env, strlen(m));

    (*env)->SetByteArrayRegion(env, bytes_time, 0, strlen(m), (jbyte*)m);


    jstring codeType = (*env)->NewStringUTF(env, "utf-8");
    jstring desArray = (jstring)(*env)->NewObject(env, strClass, methodID, bytes_time, codeType);

    free(m);
    free(temp);
    (*env)->ReleaseStringUTFChars(env, srcArray, aryText);
    (*env)->ReleaseStringUTFChars(env, pin, cpin);

    POP_LOCAL_FRAME( desArray );

    return desArray;
}
