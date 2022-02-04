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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarView;

/**
 * NOTE:
 *
 * Tag EXPM is used for experimental features.
 * Use Ctrl+Shift+F to perform project-wide search in Android Studio.
 */

public class MainActivity extends AppCompatActivity {


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


        binding.bottomNav.setOnNavigationItemReselectedListener(item -> {});

        if (savedInstanceState == null) {
            handleIntent(getIntent(), navController); // First launch
        }


        // TODO For testing only
        Intent in = new Intent(this, CSOnboarding.class);
        startActivity(in);
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