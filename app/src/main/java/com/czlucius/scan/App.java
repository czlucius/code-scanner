

/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2021 Lucius Chee Zihan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.czlucius.scan;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.StringRes;

import com.czlucius.scan.exceptions.IllegalPermissionsException;
import com.czlucius.scan.misc.monetization.AdStrategy2;
//import com.google.android.material.color.DynamicColors;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application C‌‍‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‍‍‌‌‌‌‍‌‍‍‌‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‌‌‍‌‌‍‍‌‍‌‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‍‍‍‌‌‍‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‍‌‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‌‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‍‌‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‌‌‌‍‍‌‍‍‌‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‍‌‌‌‌‍‍‌‍‍‍‌‍‍‌‌‌‍‌‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‍‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍lass.
 */
public class App extends Application {
    public static final String TAG = "App";
    private static final List<String> ILLEGAL_PERMISSIONS = Arrays.asList(
            "com.google.android.gms.permission.AD_ID",
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION);
    private static WeakReference<Resources> res;

    public static ExecutorService globalExService;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        globalExService = Executors.newFixedThreadPool(processors);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        checkIllegalPerms(ILLEGAL_PERMISSIONS);

        // Initialize Advertising (only for Google P‌‍‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‍‍‌‌‌‌‍‌‍‍‌‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‌‌‍‌‌‍‍‌‍‌‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‍‍‍‌‌‍‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‍‌‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‌‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‍‌‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‌‌‌‍‍‌‍‍‌‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‍‌‌‌‌‍‍‌‍‍‍‌‍‍‌‌‌‍‌‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‍‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍lay version), AdStrategy2
        AdStrategy2.getInstance(this)
                .initialize();

        res = new WeakReference<>(getResources());
    }


    // Get Strings Globally while app is running, but if re‌‍‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‍‍‌‌‌‌‍‌‍‍‌‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‌‌‍‌‌‍‍‌‍‌‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‍‍‍‌‌‍‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‍‌‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‌‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‍‌‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‌‌‌‍‍‌‍‍‌‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‍‌‌‌‌‍‍‌‍‍‍‌‍‍‌‌‌‍‌‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‍‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍sources aren't available, return fallback.
    public static String getStringGlobal(@StringRes int resId, String fallback) {
        if (res != null && res.get() != null) {
            return res.get().getString(resId);
        } else {
            Log.e(TAG, "Warning: Resources invalid. String resources could not be obtained. Defaulting to fallback string");
            return fallback;
        }
    }



    /***
     * Checks for sensitive permissions that are not sup‌‍‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‍‍‌‌‌‌‍‌‍‍‌‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‌‌‍‌‌‍‍‌‍‌‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‍‍‍‌‌‍‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‍‌‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‌‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‍‌‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‌‌‌‍‍‌‍‍‌‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‍‌‌‌‌‍‍‌‍‍‍‌‍‍‌‌‌‍‌‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‍‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍posed to be added in the app but are due to 3rd party libraries
     * If such permissions are available, cr‌‍‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‍‍‌‌‌‌‍‌‍‍‌‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‌‌‍‌‌‍‍‌‍‌‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‍‍‍‌‌‍‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‍‌‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‌‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‍‌‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‌‌‌‍‍‌‍‍‌‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‍‌‌‌‌‍‍‌‍‍‍‌‍‍‌‌‌‍‌‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‍‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍ash the app and do not let the app run.
     * This is in compliance with Google Play Program Policies.
     */
    public void checkIllegalPerms(List<String> perms) {
        PackageManager pm = getPackageManager();

        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            List<String> requestedPermissions = null;
            if (packageInfo != null) {
                requestedPermissions = Arrays.asList(packageInfo.requestedPermissions);
            }


            if (requestedPermissions != null && requestedPermissions.size() > 0) {
                boolean doesNotContainIllegalPms = Collections.disjoint(requestedPermissions, perms);
                if (!doesNotContainIllegalPms) {
                    throw new IllegalPermissionsException("App holds illegal permissions that is not supposed to be in the app.");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
