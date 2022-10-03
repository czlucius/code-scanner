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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ActivityMainBinding;
import com.czlucius.scan.preferences.Settings;

/**
 * NOTE:
 *
 * Tag EXPM is used for experimental features.
 * Use Ctrl+Shift+F to perform project-wide search in Android Studio.
 */

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_INTRO = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.czlucius.scan.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        if (navHostFragment == null) {
            throw new RuntimeException("No NavHost found");
        }
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomNav, navController);


        binding.bottomNav.setOnNavigationItemReselectedListener(item -> {
        });

        Settings s = Settings.getInstance(this);
        if (s.getShouldShowOnboarding()) {
            Intent i = new Intent(this, CSOnboarding.class);
            startActivityForResult(i, REQUEST_CODE_INTRO
            );
        }

        if (savedInstanceState == null) {
            handleIntent(getIntent(), navController); // First launch
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == Activity.RESULT_OK) {
                recreate(); // Previous changes no longer applicable
            } else {
                finish();
            }
        }
    }

    private void handleIntent(Intent intent, NavController navController)  {
        if (intent == null || intent.getType() == null) return;
        if (intent.getType().startsWith("image")) {
            navigate(navController, R.id.scannerFragment);
        } else {
            navigate(navController, R.id.createFragment);
        }
    }


    private void navigate(NavController navController, int id) {

        navController.navigate(id);
    }


}