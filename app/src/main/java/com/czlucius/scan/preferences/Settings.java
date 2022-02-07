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

package com.czlucius.scan.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.czlucius.scan.misc.monetization.AdStrategy2;

/**
 * Wrapper class for SharedPreferences to make interacting with it easier.
 */
public class Settings {
    private final SharedPreferences globalPrefs;
    private static Settings INSTANCE;

    private static final String SHOULD_SHOW_ONBOARDING = "SHOULD_SHOW_ONBOARDING";

    private Settings(Context context) {
        this.globalPrefs = context.getApplicationContext().getSharedPreferences("APP_PREF_VALS", Context.MODE_PRIVATE);
    }

    public static Settings getInstance(Context appCtx) {
        if (INSTANCE == null) {
            INSTANCE = new Settings(appCtx);
        }
        return INSTANCE;
    }


    public boolean getShouldShowOnboarding() {
        return globalPrefs.getBoolean(SHOULD_SHOW_ONBOARDING, true);
    }

    public void setShouldShowOnboarding(boolean value) {
        globalPrefs.edit()
                .putBoolean(SHOULD_SHOW_ONBOARDING, value)
                .apply();
    }


}
