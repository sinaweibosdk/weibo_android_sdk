#ifndef _ENCODE_CONSTANTS_H
#define _ENCODE_CONSTANTS_H

typedef const char * ConstantItem;

typedef enum
{
    GETSYSTEMSERVICE, // 0 getSystemService
    GETASSETS, // 1 getAssets
    __LANDROID_CONTENT_RES_ASSETMANAGER, // 8 ()Landroid/content/res/AssetManager;
} EncodedStringIndex;

char* getConstantString( JNIEnv * env, EncodedStringIndex index );

typedef enum
{
    HEX_CHAR_LIST, // 0 axblcsd3eBfng8h4iSkElDm
    HEX_JAVAX_CRYPTO_SPEC_SECRETKEYSPEC, // 1 javax/crypto/spec/SecretKeySpec
    HEX_JAVAX_CRYPTO_CIPHER, // 2 javax/crypto/Cipher
    HEX_GETINSTANCE, // 3 getInstance
    HEX__LJAVA_LANG_STRING_LJAVAX_CRYPTO_CIPHER, // 4 (Ljava/lang/String;)Ljavax/crypto/Cipher;
    HEX_DECRYPT_MODE, // 5 DECRYPT_MODE
    HEX_INIT, // 6 init
    HEX__ILJAVA_SECURITY_KEY_V, // 7 (ILjava/security/Key;)V
    HEX_DOFINAL, // 8 doFinal
    HEX_ENCRYPT_MODE, // 9 DECRYPT_MODE
} HexEncodedStringIndex;

char* getConstantString2( JNIEnv * env, HexEncodedStringIndex index );

#endif
