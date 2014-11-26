#include "stdafx.h"
#include "crypt.h"
#include "encodedConstants.h"

static const ConstantItem CONSTANTS[] = {
        "B537F09C42DD4AD259E9669784028C4B05E88F9A38E34434",    // getSystemService
		"EB3D0CD91C91E70F08D654A47283B179",    // getAssets
        "4C647531B651AA73ABC0B98BC7C9E86A0031836A8F754A6DB1CBA43DFCF37C0962BE5948FF19B9E0"    // ()Landroid/content/res/AssetManager;
    };

_NO_EXPORT
char* getConstantString( JNIEnv * env, EncodedStringIndex index ) {
//    LOGE( "getConstantString in" );
//    LOGE( CONSTANTS[ index ] );
    char * ret = decryptStringForC( env, CONSTANTS[ index ] );
//    LOGE( ret );
//    LOGE( "getConstantString out" );
    return ret;
}

static const ConstantItem CONSTANTS2[] =
{
    "6178626C637364336542666E6738683469536B456C446D"    // 0 axblcsd3eBfng8h4iSkElDm
    , "6A617661782F63727970746F2F737065632F5365637265744B657953706563"    // 1 javax/crypto/spec/SecretKeySpec
    , "6A617661782F63727970746F2F436970686572"    // 2 javax/crypto/Cipher
    , "676574496E7374616E6365"    // 3 getInstance
    , "284C6A6176612F6C616E672F537472696E673B294C6A617661782F63727970746F2F4369706865723B"    // 4 (Ljava/lang/String;)Ljavax/crypto/Cipher;
    , "444543525950545F4D4F4445"    // 5 DECRYPT_MODE
    , "696E6974"    // 6 init
    , "28494C6A6176612F73656375726974792F4B65793B2956"    // 7 (ILjava/security/Key;)V
    , "646F46696E616C"    // 8 doFinal
};

_NO_EXPORT
char* getConstantString2( JNIEnv * env, HexEncodedStringIndex index ) {
//    LOGE( "getConstantString2 in" );
//    LOGE( CONSTANTS2[ index ] );
    char * ret = decryptStringForC2( env, CONSTANTS2[ index ] );
//    LOGE( ret );
//    LOGE( "getConstantString2 out" );
    return ret;
}
