

/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2022 czlucius
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

import com.google.android.material.color.DynamicColors;

import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.config.Configuration;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfiguration;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
        DynamicColors.applyToActivitiesIfAvailable(this);


        // ACRA Crash Reporting
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder();
        //core configuration:
        builder
                .withBuildConfigClass(BuildConfig.class)
                .withReportFormat(StringFormat.JSON);

        // Plugin configs
        ArrayList<Configuration> acraConfigs = new ArrayList<>();
        String emailBody = "Crash report for Code Scanner\n\n" +
                "Code Scanner just crashed. Send this email with the log files attached to the developers for diagnosis.\n" +
                "This will help us improve the app and make it better!\n" +
                "Sending the log files is entirely at your discretion, if you don't feel comfortable sharing them, you can choose to discard this email draft.\n" +
                "If you don't wish to use your email address, you can email this to yourself, and upload the attached file here: https://forms.gle/2oHZ17SNaYWmzRXTA\n" +
                "Alternatively, if you know how to use GitHub to report issues, you may file an issue here: https://github.com/czlucius/code-scanner/issues (with the log file). \n" +
                "Thanks!\n";

        MailSenderConfiguration mailSenderConfig = new MailSenderConfigurationBuilder()
                .withMailTo(getString(R.string.contact_email))
                .withSubject("Bug report for Code Scanner")
                .withBody(emailBody)
                .withEnabled(true)
                .build();
        acraConfigs.add(mailSenderConfig);
        builder.setPluginConfigurations(acraConfigs);

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
