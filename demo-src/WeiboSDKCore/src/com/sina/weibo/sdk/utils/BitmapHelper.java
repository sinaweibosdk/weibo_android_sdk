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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * 用于处理 Bitmap 的工具类。
 * TODO：（To be refactor...）
 * 
 * @author SINA
 * @since 2013-11-06
 */
public final class BitmapHelper {

    /**
     * Make sure the color data size not more than 5M.
     */
    public static boolean makesureSizeNotTooLarge(Rect rect) {
        final int FIVE_M = 5 * 1024 * 1024;
        if (rect.width() * rect.height() * 2 > FIVE_M) {
            // 不能超过5M
            return false;
        }
        return true;
    }

    public static int getSampleSizeOfNotTooLarge(Rect rect) {
        final int FIVE_M = 5 * 1024 * 1024;
        double ratio = ((double) rect.width()) * rect.height() * 2 / FIVE_M;
        return ratio >= 1 ? (int) ratio : 1;
    }

    /**
     * 自适应屏幕大小得到最大的 smapleSize 同时达到此目标： 
     * 自动旋转 以适应view的宽高后, 不影响界面显示效果。
     */
    public static int getSampleSizeAutoFitToScreen(int vWidth, int vHeight, int bWidth, int bHeight) {
        if (vHeight == 0 || vWidth == 0) {
            return 1;
        }

        int ratio = Math.max(bWidth / vWidth, bHeight / vHeight);

        int ratioAfterRotate = Math.max(bHeight / vWidth, bWidth / vHeight);

        return Math.min(ratio, ratioAfterRotate);
    }

    /**
     * 检测是否可以解析成位图。
     */
    public static boolean verifyBitmap(byte[] datas) {
        return verifyBitmap(new ByteArrayInputStream(datas));
    }

    /**
     * 检测是否可以解析成位图。
     */
    public static boolean verifyBitmap(InputStream input) {
        if (input == null) {
            return false;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        input = input instanceof BufferedInputStream ? input : new BufferedInputStream(input);
        BitmapFactory.decodeStream(input, null, options);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return (options.outHeight > 0) && (options.outWidth > 0);
    }

    /**
     * 检测是否可以解析成位图。
     */
    public static boolean verifyBitmap(String path) {
        try {
            return verifyBitmap(new FileInputStream(path));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
