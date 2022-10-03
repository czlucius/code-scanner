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
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.czlucius.scan.R;
import com.czlucius.scan.callbacks.Callback;
import com.czlucius.scan.callbacks.Observer;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;

/**
 * Facade handling rewarded advertisements for {@link com.czlucius.scan.ui.InfoFragment}
 */
public class RewardedAdsHelper {
    private static final String TAG = "RewardedAdsHelper";
    private final RewardedAd rewardedAd;
    private final WeakReference<Activity> activityWeakReference;
    private final View root;
    private Observer<Boolean> rewardObserver;

    // This class is only used within misc.monetization, so we set the visibility to internal.
    RewardedAdsHelper(RewardedAd rewardedAd, View root, Activity activityInstance) {
        this.rewardedAd = rewardedAd;
        this.activityWeakReference = new WeakReference<>(activityInstance);
        this.root = root; // TODO this may cause memory leaks!

    }

    /**
     * Start showing ads
     */
    public void startRewardedAds(Callback onShow) {
        initialise(onShow);
        show();
    }

    public void setRewardObserver(Observer<Boolean> rewardObserver) {
        this.rewardObserver = rewardObserver;
    }




    private void show() {
        if (rewardedAd != null) {
            Activity activityContext = activityWeakReference.get();
            rewardedAd.show(activityContext, rewardItem -> {
                // Handle the reward.
                Log.d(TAG, "The user earned the reward.");
                // We do not need the no. of coins, etc. because the user has already earned the reward.
                rewardObserver.valueChanged(true);
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
            snackbar(R.string.rewarded_ad_not_ready);
        }
    }




    private void initialise(Callback onShow) {
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            // Both of these methods are methods called when ads do not load to completion.
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                Log.e(TAG, "An error occurred: " + adError.getMessage());
                snackbar(R.string.error);
                onShow.doAction();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                onShow.doAction();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                onShow.doAction();
            }

        });
    }

    private void snackbar(@StringRes int resId) {
        Snackbar.make(root, resId, Snackbar.LENGTH_SHORT)
                .show();
    }
}
