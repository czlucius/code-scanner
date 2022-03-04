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

package com.czlucius.scan.misc.monetization;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.arch.core.util.Function;

import com.czlucius.scan.callbacks.Callback;

// Uncomment when you want to implement ads
///** Singleton for managing ads. */
//public class AdStrategy2 {
//
//
//    private final WeakReference<Context> contextWeakReference;
//    private boolean shouldShowAds = true; // Used for rewarded ads or IAP (todo)
//
//
//
//    private static AdStrategy2 INSTANCE;
//    private AdStrategy2(Context appCtx) {
//        contextWeakReference = new WeakReference<>(appCtx);
//    }
//    /**
//     * Get an instance of {@link AdStrategy2}
//     * @param appCtx Application context. Do not pass in other contexts
//     */
//    public static AdStrategy2 getInstance(Context appCtx) {
//        if (INSTANCE == null) {
//            INSTANCE = new AdStrategy2(appCtx);
//        }
//        return INSTANCE;
//    }
//
//
//
//    /**
//     * Initialisation method.
//     * ONLY TO BE CALLED ONCE.
//     */
//    public void initialize() {
//
//        Bundle extras = new Bundle();
//        extras.putString("max_ad_content_rating", "G");
//        extras.putString("npa", "1"); // Request Non-Personalised Ads
//
//        // PLAY: How to request for child directed ads?
////        AdRequest request = new AdRequest.Builder()
////                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
////                .tagForChildDirectedTreatment(true).build();
//
//        RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
//                .toBuilder()
//                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
//                .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
//                .build();
//        MobileAds.setRequestConfiguration(requestConfiguration);
//
//        MobileAds.initialize(contextWeakReference.get());
//    }
//
//
//
//    public void loadAdView(Function<Integer, View> findViewByIdProducer) {
//
//        if (!shouldShowAds) {
//            View bannerLayout = findViewByIdProducer.apply(R.id.banner_layout);
//            if (bannerLayout != null) {
//                bannerLayout.setVisibility(View.GONE);
//            }
//            // If we should not show ads, we do not load them in the first place.
//            // Best possible method to disable them (other than removing the views)
//            // https://stackoverflow.com/questions/13323097/in-app-purchase-remove-ads
//            return;
//        }
//
//        // Get AdView
//        View view = findViewByIdProducer.apply(R.id.banner);
//
//
//        if (view instanceof AdView) {
//            AdView adView = (AdView) view;
//            AdRequest adRequest = new AdRequest.Builder().build();
//            adView.loadAd(adRequest);
//        } else {
//            throw new RuntimeException("View referenced is not AdView");
//        }
//    }
//
//    public void addAdViewTo(ViewGroup parent) {
//        if (!shouldShowAds) {
//            return;
//        }
//        Context localContext = parent.getContext();
//        LayoutInflater layoutInflater = LayoutInflater.from(localContext);
//        View adView = layoutInflater.inflate(R.layout.banner, parent);
//        loadAdView(parent::findViewById);
//    }
//
//    public void loadRewardedAdVideo(Activity activity, View root, Callback resetCallback) {
//        new MaterialAlertDialogBuilder(root.getContext(), R.style.Theme_App_AlertDialogTheme)
//                .setBackground(new ColorDrawable(Color.YELLOW))
//                .setTitle("This is not part of the app's content. This is an advertisement by Google AdMob")
//                .setMessage("This will take you to watch an advertisement from AdMob by Google Inc.\nBy pressing \"Next\", you agree the following screens are not part of this app, Code Scanner, and are NOT SUPPLIED BY THE DEVELOPER.\nThis dialog is to inform you that the content that you will see is from AdMob by Google.\nAdvertisements would be shown in accordance to our Privacy Policy.")
//                .setNegativeButton(R.string.cancel, null)
//                .setPositiveButton(R.string.next, (dialog, which) -> {
//                    Toast.makeText(activity, "This is NOT part of the app content.", Toast.LENGTH_LONG).show();
//
//
//
//                    AdRequest adRequest = new AdRequest.Builder().build();
//
//                    RewardedAd.load(contextWeakReference.get(), AdIdRetriever.retrieveRewardedId(),
//                            adRequest, new RewardedAdLoadCallback() {
//                                @Override
//                                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                                    super.onAdLoaded(rewardedAd);
//                                    // Rewarded ad loaded, pass on to RewardedAdsHelper.
//                                    RewardedAdsHelper helper = new RewardedAdsHelper(rewardedAd, root, activity);
//                                    helper.startRewardedAds(resetCallback);
//                                    helper.setRewardObserver(newValue -> {
//                                        // Reward is obtained.
//                                        shouldShowAds = false;
//                                        // Remove current ad in the Info Fragment so user won't be confused.
//                                        ((ViewGroup) root).removeView(root.findViewById(R.id.banner));
//                                        activity.recreate();
//
//                                    });
//
//                                }
//
//                                @Override
//                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                                    super.onAdFailedToLoad(loadAdError);
//                                    // Ad failed to load.
//                                    Snackbar.make(root, R.string.ad_failed_to_load, Snackbar.LENGTH_SHORT).show();
//                                    resetCallback.doAction();
//                                }
//
//                            });
//                })
//                .setOnCancelListener(dialog -> resetCallback.doAction())
//                .show();
//
//
//    }
//
//
//
//
//}

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
