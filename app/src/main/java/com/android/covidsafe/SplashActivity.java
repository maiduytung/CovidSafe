package com.android.covidsafe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.covidsafe.databinding.ActivitySplashBinding;
import com.android.covidsafe.repository.AuthRepository;
import com.android.covidsafe.ui.auth.AuthActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SplashActivity extends DaggerAppCompatActivity {

    private ActivitySplashBinding binding;

    @Inject
    AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (authRepository.isLogin()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                }

                finish();
            }
        }, 3000);
    }
}