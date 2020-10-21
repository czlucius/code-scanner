package com.czlucius.scan.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int[] topLevelDestIds = {R.id.scannerFragment, R.id.historyFragment};


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

                navController.navigate(R.id.scannerFragment);
                return true;
            } else if (itemId == R.id.history) {
                navController.navigate(R.id.historyFragment);
                return true;
            }
            return false;
        });

        binding.bottomNav.setOnNavigationItemReselectedListener(item -> {
        });
    }


}