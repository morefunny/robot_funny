package com.luojituili.morefunny;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by sherlockhua on 2016/11/28.
 */

public class Util {

    public static String _imei;
    private static HashMap<Integer, Long> _categoryUpdateMap = new HashMap<>(128);

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
            Log.e("cache sd", cachePath);
        } else {
            cachePath = context.getCacheDir().getPath();
            Log.e("cache", cachePath);
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }

    public static void setImei(String imei) {
        _imei = imei;
    }

    public static String getImei() {
        return _imei;
    }

    public static boolean isNeedUpdate(int categoryId) {
        Integer key = Integer.valueOf(categoryId);
        long now = System.currentTimeMillis()/1000;
        if (!_categoryUpdateMap.containsKey(key)) {
            Long val = Long.valueOf(now);
            _categoryUpdateMap.put(key, val);
            Log.e("update", String.format("not exits:%d", now));
            return true;
        }

        Long val = _categoryUpdateMap.get(key);
        long last = val.longValue();

        Log.e("update", String.format("now:%d last:%d", now, last));
        if (now - last > 600) {
            _categoryUpdateMap.put(key, Long.valueOf(now));
            return true;
        }

        return false;
    }

}
