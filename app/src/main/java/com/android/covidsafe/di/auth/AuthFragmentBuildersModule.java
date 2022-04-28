package com.android.covidsafe.di.auth;

import com.android.covidsafe.ui.dialog.VerificationDialog;
import com.android.covidsafe.ui.auth.login.LoginFragment;
import com.android.covidsafe.ui.auth.register.RegisterFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();

    @ContributesAndroidInjector
    abstract RegisterFragment contributeRegisterFragment();

    @ContributesAndroidInjector
    abstract VerificationDialog contributeVerificationDialog();

}
