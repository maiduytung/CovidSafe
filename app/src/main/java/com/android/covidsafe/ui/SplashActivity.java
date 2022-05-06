package com.android.covidsafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.covidsafe.databinding.ActivitySplashBinding;
import com.android.covidsafe.repository.SecureSharedPref;
import com.android.covidsafe.ui.auth.AuthActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SplashActivity extends DaggerAppCompatActivity {

    private ActivitySplashBinding binding;

    @Inject
    SecureSharedPref secureSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                finish();
            }
        }, 3000);
    }
}