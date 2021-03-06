

/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2020 Lucius Chee Zihan
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

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.StringRes;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {
    public static final String TAG = "App";
    private static WeakReference<Resources> res;

    public static ExecutorService globalExService;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        globalExService = Executors.newFixedThreadPool(processors);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        res = new WeakReference<>(getResources());
    }


    public static String getStringGlobal(@StringRes int resId, String fallback) {
        if (res != null && res.get() != null) {
            return res.get().getString(resId);
        } else {
            Log.e(TAG, "Warning: Resources invalid. String resources could not be obtained. Defaulting to fallback string");
            return fallback;
        }
    }
}
