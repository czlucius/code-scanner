

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

import org.acra.ACRA;
import org.acra.config.Configuration;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfiguration;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
    private static WeakReference<Resources> res;

    public static ExecutorService globalExService;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        globalExService = Executors.newFixedThreadPool(processors);
    }

    @Override
    public void onCreate() {
        super.onCreate();



        // Initialize Advertising (only for Google P‌‍‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‍‍‌‌‌‌‍‌‍‍‌‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‌‌‍‌‌‍‍‌‍‌‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‍‍‍‌‌‍‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‍‌‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‌‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‍‌‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‌‌‌‍‍‌‍‍‌‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‍‌‌‌‌‍‍‌‍‍‍‌‍‍‌‌‌‍‌‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‍‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍lay version), AdStrategy2
        AdStrategy2.getInstance(this)
                .initialize();


        // ACRA Crash Reporting
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder();
        //core configuration:
        builder
                .withBuildConfigClass(BuildConfig.class)
                .withReportFormat(StringFormat.JSON);

        // Plugin configs
        ArrayList<Configuration> acraConfigs = new ArrayList<>();
        MailSenderConfigurationBuilder mailSenderConfig = 
        builder.setPluginConfigurations();

        builder.getPluginConfigurationBuilder(ToastConfigurationBuilder.class)
                .withResText(R.string.acra_toast_text)
                //make sure to enable all plugins you want to use:
                .withEnabled(true);
        ACRA.init(this, builder);

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

    public static String getStringGlobalWithArgs(@StringRes int resId, Object... formatArgs) {
        if (res != null && res.get() != null) {
            return res.get().getString(resId, formatArgs);
        } else {
            throw new IllegalAccessError("App context not available!!!!!");
        }
    }



}
