package com.unity.easykt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.Keep;

import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

@Keep
public class LaunchApp {
    public void  jump(Activity mainActivity, String appPkg, String marketPkg)
    {
        try {
            if (TextUtils.isEmpty(appPkg)) return;

            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GetMCC()
    {
        //TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //telManager.getSubscriberId();

        return "";
    }

    public static boolean hasInstall(Activity mainActivity, String pkgName) {
        PackageManager pManager = mainActivity.getApplicationContext().getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> apklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < apklist.size(); i++) {
            PackageInfo apk = (PackageInfo) apklist.get(i);
            //判断是否为非系统预装的应用程序
            if (pkgName == apk.packageName) {
               return  true;
            }
        }
        return false;
    }
}
