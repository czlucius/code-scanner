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

package com.czlucius.scan.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
import com.czlucius.scan.preferences.Settings;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class CSOnboarding extends IntroActivity {


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addSlide(
                new SimpleSlide.Builder()
                        .layout(R.layout.mi_fragment_simple_slide_scrollable)
                        .title(R.string.onboarding_welcome)
                        .description(R.string.onboarding_welcome_summary)
                        .background(R.color.onboarding_background)
                        .backgroundDark(R.color.onboarding_background_dark)
                        .build()
        );


        addSlide(
                new SimpleSlide.Builder()
                        .layout(R.layout.mi_fragment_simple_slide_scrollable)
                        .title(R.string.scan)
                        .description("Scan supported codes whenever, wherever you go.")
                        .image(R.mipmap.onboarding_scan)
                        .background(R.color.onboarding_background)
                        .backgroundDark(R.color.onboarding_background_dark)
                        .build()
        );


        addSlide(
                new SimpleSlide.Builder()
                        .layout(R.layout.mi_fragment_simple_slide_scrollable)
                        .title(R.string.history)
                        .description("View history of previously scanned codes.")
                        .image(R.mipmap.onboarding_history)
                        .background(R.color.onboarding_background)
                        .backgroundDark(R.color.onboarding_background_dark)
                        .build()
        );

        addSlide(
                new SimpleSlide.Builder()
                        .layout(R.layout.mi_fragment_simple_slide_scrollable)
                        .title(R.string.create)
                        .description("Create QR codes and style it with different colors.")
                        .image(R.mipmap.onboarding_create)
                        .background(R.color.onboarding_background)
                        .backgroundDark(R.color.onboarding_background_dark)
                        .build()

        );


        addSlide(
                new SimpleSlide.Builder()
                        .layout(R.layout.mi_fragment_simple_slide_scrollable)
                        .title(R.string.batch_scan)
                        .description("Scan unlimited codes at once, and save it to history.")
                        .image(R.mipmap.onboarding_batch)
                        .background(R.color.onboarding_background)
                        .backgroundDark(R.color.onboarding_background_dark)
                        .build()
        );


        addSlide(
                new SimpleSlide.Builder()
                        .layout(R.layout.mi_fragment_simple_slide_scrollable)
                        .title(R.string.privacy_policy)
                        .description(R.string.privacy_policy_description)
                        .background(R.color.onboarding_background)
                        .backgroundDark(R.color.onboarding_background_dark)
                        .buttonCtaLabel(R.string.click_privacy_policy)
                        .buttonCtaClickListener(v -> {
                            Utils.launchWebPageExternally(
                                    this,
                                    Uri.parse(getString(R.string.privacy_policy_url))
                            );
                        })
                        .build()
        );

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == (getSlides().size() - 1)) {
                    // Last slide already. Change the Shared Preference.
                    Settings.getInstance(CSOnboarding.this)
                            .setShouldShowOnboarding(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }
}
