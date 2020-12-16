package com.czlucius.scan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ActivityMainBinding;

import org.json.JSONArray;

/**
 * NOTE:
 *
 * Tag EXPM is used for experimental features.
 * Use Ctrl+Shift+F to perform project-wide search in Android Studio.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private static final int[] topLevelDestIds = {R.id.scannerFragment, R.id.historyFragment, R.id.createFragment};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        if (navHostFragment == null) {
            throw new RuntimeException("No NavHost found");
        }
        NavController navController = navHostFragment.getNavController();

        AppBarConfiguration configuration = new AppBarConfiguration.Builder(topLevelDestIds).build();
        NavigationUI.setupActionBarWithNavController(this, navController, configuration);

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.scanner) {
                return navigate(navController, R.id.scannerFragment);
            } else if (itemId == R.id.history) {
                return navigate(navController, R.id.historyFragment);
            } else if (itemId == R.id.create) {
                return navigate(navController, R.id.createFragment);
            }

            return false;
        });

        binding.bottomNav.setOnNavigationItemReselectedListener(item -> {});


        handleIntent(getIntent(), navController);
    }

    private void handleIntent(Intent intent, NavController navController)  {
        if (intent == null || intent.getType() == null) return;
        if (intent.getType().equals("image/*")) {
            navigate(navController, R.id.scannerFragment);
        } else {
            navigate(navController, R.id.createFragment);
        }
    }


    private boolean navigate(NavController navController, int id) {
        navController.navigate(id);
        return true;
    }


}