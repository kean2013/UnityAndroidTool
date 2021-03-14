package com.unity.easykt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Keep;

@Keep
public class LaunchApp {
    public void  Jump(Activity mainActivity, String appPkg, String marketPkg)
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
}
