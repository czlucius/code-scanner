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

package com.czlucius.scan.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.czlucius.scan.R;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;

public class CSOnboarding extends AppIntro2 {

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState, @Nullable @org.jetbrains.annotations.Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        addSlide(
                AppIntroFragment.createInstance(
                        "Welcome to Code Scanner!",
                        "This on-boarding will guide you through the application and it's features.\nIf you have recently updated and see this screen, you may skip this and your data will still be intact.",
                        R.drawable.ic_baseline_call_24
                )
        );

        addSlide(
                AppIntroFragment.createInstance(
                        "Test screen",
                        "Text only intro screen"
                )
        );

        addSlide(
                AppIntroFragment.createInstance(
                        "Welcome to Code Scanner!",
                        "Text only intro screen"
                )
        );
    }
}
