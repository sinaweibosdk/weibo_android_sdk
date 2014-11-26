/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sina.weibo.sdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * 该类提供上传图片压缩的方法。
 * 
 * @author SINA
 * @since 2013-10-16
 */
public class ImageUtils {

    /**
     * 在有wifi情况下对图片进行压缩
     * 
     * @param picfile 图片路径
     * @param size    图片压缩后的最大尺寸
     * @param quality 保真度，0最小，100最大
     */
    private static void revitionImageSizeHD(String picfile, int size, int quality) throws IOException {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0!");
        }
        if (!isFileExisted(picfile)) {
            throw new FileNotFoundException(picfile == null ? "null" : picfile);
        }

        if (!BitmapHelper.verifyBitmap(picfile)) {
            throw new IOException("");
        }

        int photoSizesOrg = 2 * size;
        FileInputStream input = new FileInputStream(picfile);
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, opts);
        try {
            input.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        int rate = 0;
        for (int i = 0;; i++) {
            if ((opts.outWidth >> i <= photoSizesOrg && (opts.outHeight >> i <= photoSizesOrg))) {
                rate = i;
                break;
            }
        }

        opts.inSampleSize = (int) Math.pow(2, rate);
        opts.inJustDecodeBounds = false;

        Bitmap temp = safeDecodeBimtapFile(picfile, opts);

        if (temp == null) {
            throw new IOException("Bitmap decode error!");
        }

        deleteDependon(picfile);
        makesureFileExist(picfile);

        int org = temp.getWidth() > temp.getHeight() ? temp.getWidth() : temp.getHeight();
        float rateOutPut = size / (float) org;

        if (rateOutPut < 1) {
            Bitmap outputBitmap;
            while (true) {
                try {
                    outputBitmap = Bitmap.createBitmap(((int) (temp.getWidth() * rateOutPut)),
                            ((int) (temp.getHeight() * rateOutPut)), Bitmap.Config.ARGB_8888);
                    break;
                } catch (OutOfMemoryError e) {
                    System.gc();
                    rateOutPut = (float) (rateOutPut * 0.8);
                }
            }
            if (outputBitmap == null) {
                temp.recycle();
            }
            Canvas canvas = new Canvas(outputBitmap);
            Matrix matrix = new Matrix();
            matrix.setScale(rateOutPut, rateOutPut);
            canvas.drawBitmap(temp, matrix, new Paint());
            temp.recycle();
            temp = outputBitmap;
        }
        final FileOutputStream output = new FileOutputStream(picfile);
        if (opts != null && opts.outMimeType != null && opts.outMimeType.contains("png")) {
            temp.compress(Bitmap.CompressFormat.PNG, quality, output);
        } else {
            temp.compress(Bitmap.CompressFormat.JPEG, quality, output);
        }
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        temp.recycle();
    }

    /**
     * 在本地对图片进行压缩
     * 
     * @param picfile 图片路径
     * @param size    图片压缩后的最大尺寸
     * @param quality 保真度，0最小，100最大
     */
    private static void revitionImageSize(String picfile, int size, int quality) throws IOException {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0!");
        }

        if (!isFileExisted(picfile)) {
            throw new FileNotFoundException(picfile == null ? "null" : picfile);
        }

        if (!BitmapHelper.verifyBitmap(picfile)) {
            throw new IOException("");
        }

        FileInputStream input = new FileInputStream(picfile);
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, opts);
        try {
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int rate = 0;
        for (int i = 0;; i++) {
            if ((opts.outWidth >> i <= size) && (opts.outHeight >> i <= size)) {
                rate = i;
                break;
            }
        }

        opts.inSampleSize = (int) Math.pow(2, rate);
        opts.inJustDecodeBounds = false;

        Bitmap temp = safeDecodeBimtapFile(picfile, opts);

        if (temp == null) {
            throw new IOException("Bitmap decode error!");
        }

        deleteDependon(picfile);
        makesureFileExist(picfile);
        final FileOutputStream output = new FileOutputStream(picfile);
        if (opts != null && opts.outMimeType != null && opts.outMimeType.contains("png")) {
            temp.compress(Bitmap.CompressFormat.PNG, quality, output);
        } else {
            temp.compress(Bitmap.CompressFormat.JPEG, quality, output);
        }
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        temp.recycle();
    }

    /**
     * 对图片进行压缩。
     * 
     * @param Context 上下文
     * @param picfile 图片路径 
     *                注意：该版不提供api访问，不涉及图片相关函数调用
     *                
     * @return 成功返回 true，失败返回 false
     */
    public static boolean revitionPostImageSize(Context context, String picfile) {
        try {
            if (NetworkHelper.isWifiValid(context)) {
                revitionImageSizeHD(picfile, 1600, 75);
            } else {
                revitionImageSize(picfile, 1024, 75);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 如果加载时遇到OutOfMemoryError,则将图片加载尺寸缩小一半并重新加载。
     * 
     * @param bmpFile 文件路径
     * @param opts    注意：opts.inSampleSize 可能会被改变
     * 
     * @return 返回压缩后的图片对象
     */
    private static Bitmap safeDecodeBimtapFile(String bmpFile, BitmapFactory.Options opts) {
        BitmapFactory.Options optsTmp = opts;
        if (optsTmp == null) {
            optsTmp = new BitmapFactory.Options();
            optsTmp.inSampleSize = 1;
        }

        Bitmap bmp = null;
        FileInputStream input = null;

        final int MAX_TRIAL = 5;
        for (int i = 0; i < MAX_TRIAL; ++i) {
            try {
                input = new FileInputStream(bmpFile);
                bmp = BitmapFactory.decodeStream(input, null, opts);
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                optsTmp.inSampleSize *= 2;
                try {
                    input.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                break;
            }
        }

        return bmp;
    }

    /**
     * 删除文件。
     * 
     * @param file 文件
     */
    private static void delete(File file) {
        if ((file != null) && (file.exists()) && (!(file.delete()))) {
            throw new RuntimeException(file.getAbsolutePath() + " doesn't be deleted!");
        }

    }

    /**
     * 删除文件路径下的所有文件。
     * 
     * @param filepath  文件路径
     */
    private static boolean deleteDependon(String filepath) {
        if (TextUtils.isEmpty(filepath))
            return false;
        File file = new File(filepath);
        int retryCount = 1, maxRetryCount = 0;
        maxRetryCount = (maxRetryCount < 1) ? 5 : maxRetryCount;
        boolean isDeleted = false;
        if (file != null) {
            while ((!(isDeleted)) && (retryCount <= maxRetryCount) && (file.isFile()) && (file.exists()))
                if (!((isDeleted = file.delete()))) {
                    ++retryCount;
                }
        }
        return isDeleted;
    }

    /**
     * 判断文件是否存在。
     * 
     * @param filepath  文件路径
     */
    private static boolean isFileExisted(String filepath) {
        if (TextUtils.isEmpty(filepath))
            return false;
        File file = new File(filepath);
        if (file != null && file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件的父亲路径是否存在。
     * 
     * @param file  文件
     */
    private static boolean isParentExist(File file) {
        if (file == null) {
            return false;
        } else {
            File parent = file.getParentFile();
            if ((parent != null) && (!(parent.exists()))) {
                if ((!(file.exists())) && (!(file.mkdirs()))) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 根据文件路径建立文件。
     * 
     * @param filePath      文件路径
     */
    private static void makesureFileExist(String filePath) {
        if (filePath == null)
            return;
        File file = new File(filePath);
        if (file != null && !(file.exists())) {
            if (isParentExist(file)) {
                if (file.exists())
                    delete(file);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 断当前网络是否为wifi。
     * 
     * @param mContext  上下文
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
