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

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 该类主要是对微博 SDK 中的资源进行管理，如字符串、图片、布局大小等。
 * 
 * @author SINA
 * @since 2013-11-10
 */
public class ResourceManager {
    private static final String TAG = ResourceManager.class.getName();
    
    /** 图片所在文件夹列表 */
    private static final String DRAWABLE        = "drawable";
    private static final String DRAWABLE_LDPI   = "drawable-ldpi";
    private static final String DRAWABLE_MDPI   = "drawable-mdpi";
    private static final String DRAWABLE_HDPI   = "drawable-hdpi";
    private static final String DRAWABLE_XHDPI  = "drawable-xhdpi";
    private static final String DRAWABLE_XXHDPI = "drawable-xxhdpi";
    private static final String[] PRE_INSTALL_DRAWBLE_PATHS = {
        DRAWABLE_XXHDPI, 
        DRAWABLE_XHDPI, 
        DRAWABLE_HDPI, 
        DRAWABLE_MDPI, 
        DRAWABLE_LDPI, 
        DRAWABLE
    };
    
    /**
     * 根据手机语言设置, 获取对应的字符串。
     * @param context
     * @param en 英文
     * @param ch 简体中文
     * @param tw 繁体中文
     * @return
     */
    public static String getString(Context context, String en, String cn, String tw) {
        Locale locale = getLanguage();
        if (Locale.SIMPLIFIED_CHINESE.equals(locale)) {
            return cn;
        } else if (Locale.TRADITIONAL_CHINESE.equals(locale)) {
            return tw;
        } else {
            return en;
        }
    }

    /**
     * 根据图片 ID 获取对应的图片。
     * 
     * @param context 应用程序上下文环境
     * @param id      图片 ID 
     * 
     * @return 返回 Drawable
     */
    public static Drawable getDrawable(Context context, String fileName) {        
        String path = getAppropriatePathOfDrawable(context, fileName);
        return getDrawableFromAssert(context, path, false);
    }

    /**
     * 根据图片 ID 获取对应的 NinePatch 图片。
     * 
     * @param context 应用程序上下文环境
     * @param id      图片 ID 
     * 
     * @return  返回图片 ID 对应的 NinePatch 图片
     */
    public static Drawable getNinePatchDrawable(Context context, String fileName) {
        String path = getAppropriatePathOfDrawable(context, fileName);
        return getDrawableFromAssert(context, path, true);
    }
    
    /**
     * 获取当前语言环境。
     * 
     * @return 当前语言环境
     */
    public static Locale getLanguage() {
        Locale locale = Locale.getDefault();
        if (Locale.SIMPLIFIED_CHINESE.equals(locale) 
                || Locale.TRADITIONAL_CHINESE.equals(locale)) {
            return locale;
        } else {
            return Locale.ENGLISH;
        }
    }
    
    /**
     * 根据文件名，从 Assets 里面获取其相对路径。
     * 注意：如果找不到对应路径，则会按 DPI 的大小，由高到低进行查找。即，对于图 xxx.png，当前为xhdpi，
     * 那么，我们首先会从 drawable-xhdpi 目录下查找，如果查找不到，再从 drawable-hdpi 目录中进行查找。
     * 
     * @param context  应用程序上下文环境
     * @param fileName 文件名
     * 
     * @return 如果找不到对应的文件的路径，则返回 null；否则，返回其相对路径。
     */
    private static String getAppropriatePathOfDrawable(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            LogUtil.e(TAG, "id is NOT correct!");
            return null;
        }
        
        String pathPrefix = getCurrentDpiFolder(context);
        String path = pathPrefix + "/" + fileName;
        LogUtil.i(TAG, "Maybe the appropriate path: " + path);
        if (isFileExisted(context, path)) {
            return path;
        } else {
            LogUtil.d(TAG, "Not the correct path, we need to find one...");
            
            int ix = 0;
            boolean bFound = false;
            for (ix = 0; ix < PRE_INSTALL_DRAWBLE_PATHS.length; ix++) {
                if (!bFound) {
                    if (pathPrefix.equals(PRE_INSTALL_DRAWBLE_PATHS[ix])) {
                        bFound = true;
                        LogUtil.i(TAG, "Have Find index: " + ix + ", " + PRE_INSTALL_DRAWBLE_PATHS[ix]);
                    }
                } else {
                    path = PRE_INSTALL_DRAWBLE_PATHS[ix] + "/" + fileName;
                    if (isFileExisted(context, path)) {
                        return path;
                    }
                }
            }
        }
        
        LogUtil.e(TAG, "Not find the appropriate path for drawable");
        // Not find the appropriate path for drawable
        return null;
    }
    
    /**
     * 从 Assert 中获取指定的 Drawable。
     * 
     * @param context      应用程序上下文环境
     * @param relativePath 图片文件的相对路径，如 drawable/xxx.png
     * @param isNinePatch  图片是否是 NinePatch 文件
     * 
     * @return 如果图片不存在，则返回 null；
     *         如果图片是 NinePatch 文件，则返回 {@link NinePatchDrawable}；
     *         否则，返回 {@link BitmapDrawable}；。
     */
    private static Drawable getDrawableFromAssert(Context context, String relativePath, boolean isNinePatch) {
        Drawable rtDrawable = null;
        AssetManager asseets = context.getAssets();
        InputStream is = null;
        try {
            is = asseets.open(relativePath);
            if (is != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                if (isNinePatch) {
                    Configuration config = context.getResources().getConfiguration();
                    Resources res = new Resources(context.getAssets(), metrics, config);
                    rtDrawable = new NinePatchDrawable(
                            res, bitmap, bitmap.getNinePatchChunk(), new Rect(0, 0, 0, 0), null);
                } else {
                    bitmap.setDensity(metrics.densityDpi);
                    rtDrawable = new BitmapDrawable(context.getResources(), bitmap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
    
                is = null;
            }
        }
    
        return rtDrawable;
    }    

    /**
     * 检查 Assert 中，指定文件路径的文件是否存在。
     * 
     * @param context  应用程序上下文环境
     * @param filePath 文件的相对路径，如 drawable/xxx.png
     * @return
     */
    private static boolean isFileExisted(Context context, String filePath) {
        if (null == context || TextUtils.isEmpty(filePath)) {
            return false;
        }

        AssetManager asseets = context.getAssets();
        InputStream is = null;
        try {
            is = asseets.open(filePath);
            LogUtil.d(TAG, "file [" + filePath + "] existed");
            return true;
        } catch (IOException e) {
            //e.printStackTrace();
            LogUtil.d(TAG, "file [" + filePath + "] NOT existed");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                is = null;
            }
        }
        
        return false;
    }
    
    /**
     * 获取当前合适的 DPI 文件夹。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 返回当前合适的 DPI 文件夹
     */
    private static String getCurrentDpiFolder(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int density = dm.densityDpi;
        if (density <= 120) {
            return DRAWABLE_LDPI;
        } else if (density > 120 && density <= 160) {
            return DRAWABLE_MDPI;
        } else if (density > 160 && density <= 240) {
            return DRAWABLE_HDPI;
        } else if (density > 240 && density <= 320) {
            return DRAWABLE_XHDPI;
        } else /*if (density > 320)*/ {
            return DRAWABLE_XXHDPI;
        }
    }

    @SuppressWarnings("unused")
    private static View extractView(Context context, String fileName, ViewGroup root)
            throws Exception {
        XmlResourceParser parser = context.getAssets().openXmlResourceParser(fileName);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(parser, root);
    }

    @SuppressWarnings("unused")
    private static Drawable extractDrawable(Context context, String fileName) throws Exception {
        InputStream inputStream = context.getAssets().open(fileName);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        TypedValue value = new TypedValue();
        /** 传入TypeValue，指定资源的像素密度是基于320*480屏幕的 */
        value.density = dm.densityDpi;
        /** 传入Resources，以获取目标屏幕像素密度 */
        Drawable drawable = Drawable.createFromResourceStream(
                context.getResources(), value, inputStream, fileName);
        inputStream.close();
        return drawable;
    }
    
    /**
     * 把 dp 转换成 px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int px = (int) (dp * dm.density + 0.5);
        return px;
    }
    
    public static ColorStateList createColorStateList(int normal, int pressed) {
        int[] colors = new int[] { pressed, pressed, pressed, normal };
        int[][] states = new int[4][];
        states[0] = new int[] { android.R.attr.state_pressed }; 
        states[1] = new int[] { android.R.attr.state_selected }; 
        states[2] = new int[] { android.R.attr.state_focused };
        states[3] = StateSet.WILD_CARD;
        return new ColorStateList(states, colors);
    }
    
    public static StateListDrawable createStateListDrawable(Context context, String normalPicName, String pressedPicName) {
        Drawable normalDrawable = null;
        if (normalPicName.indexOf(".9") > -1) {
            normalDrawable = getNinePatchDrawable(context, normalPicName);
        } else {
            normalDrawable = getDrawable(context, normalPicName);
        }
        Drawable pressedDrawable = null;
        if (pressedPicName.indexOf(".9") > -1) {
            pressedDrawable = getNinePatchDrawable(context, pressedPicName); 
        } else {
            pressedDrawable = getDrawable(context, pressedPicName);
        }
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { android.R.attr.state_pressed }, pressedDrawable);
        drawable.addState(new int[] { android.R.attr.state_selected }, pressedDrawable);
        drawable.addState(new int[] { android.R.attr.state_focused }, pressedDrawable);
        drawable.addState(StateSet.WILD_CARD, normalDrawable);
        return drawable;
    }
    
    
    
}