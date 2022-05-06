package com.android.covidsafe.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.covidsafe.R;
import com.android.covidsafe.databinding.ActivityMainBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView navView;
    private BottomAppBar bottomAppBar;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarMainTopAppBar);

        navView = findViewById(R.id.bottom_navigation);
        navView.setBackground(null);
//        navView.getMenu().getItem(1).setEnabled(false);
        bottomAppBar = findViewById(R.id.bottom_app_bar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.home_fragment, R.id.account_fragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        navController.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(
                            @NonNull NavController controller,
                            @NonNull NavDestination destination,
                            @Nullable Bundle arguments
                    ) {
                        boolean showBottomNav = false;
                        if (arguments != null) {
                            showBottomNav = arguments.getBoolean("ShowBottomNav", false);
                        }
                        if (showBottomNav) {
                            showBottomNavigation();
                        } else {
                            hideBottomNavigation();
                        }
                    }
                }
        );
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void hideBottomNavigation() {
        bottomAppBar.setVisibility(View.GONE);
//        binding.floatingActionButton.setVisibility(View.GONE);
    }

    private void showBottomNavigation() {
        bottomAppBar.setVisibility(View.VISIBLE);
//        binding.floatingActionButton.setVisibility(View.VISIBLE);
    }
}