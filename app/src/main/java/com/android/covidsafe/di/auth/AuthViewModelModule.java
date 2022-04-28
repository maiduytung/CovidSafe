package com.android.covidsafe.di.auth;

import androidx.lifecycle.ViewModel;

import com.android.covidsafe.di.ViewModelKey;
import com.android.covidsafe.ui.dialog.VerificationViewModel;
import com.android.covidsafe.ui.auth.login.LoginViewModel;
import com.android.covidsafe.ui.auth.register.RegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindRegisterViewModel(RegisterViewModel registerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VerificationViewModel.class)
    abstract ViewModel bindVerificationViewModel(VerificationViewModel verificationViewModel);

}
