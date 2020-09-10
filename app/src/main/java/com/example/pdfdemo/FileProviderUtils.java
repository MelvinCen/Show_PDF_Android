package com.example.pdfdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.io.File;

import androidx.core.content.FileProvider;

public class FileProviderUtils {
    /**
     * 获取合适的File Uri
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * 获取File Uri from 安卓7及以上版本
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile24(Context context, File file) {
        return FileProvider.getUriForFile(context,
                "com.example.pdfdemo.fileProvider",
                file);
    }
}
