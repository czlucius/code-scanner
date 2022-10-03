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

package com.czlucius.scan.misc.monetization;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.arch.core.util.Function;

import com.czlucius.scan.callbacks.Callback;

public class AdStrategy2 {

    private AdStrategy2() { /* stub */ }

    public static AdStrategy2 getInstance(Context appCtx) {
        return new AdStrategy2();
    }

    public void initialize() { /* stub */ }

    public void loadAdView(Function<Integer, View> findViewByIdProducer) { /* stub */ }

    public void addAdViewTo(ViewGroup parent) {}

    public void loadRewardedAdVideo(Activity activity, View root, Callback resetCallback) { /* stub */ }

    @Deprecated
    public void initialiseRewardedAds(Activity activity, View root) { /* stub */ }




}
