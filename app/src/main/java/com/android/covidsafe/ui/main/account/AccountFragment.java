package com.android.covidsafe.ui.main.account;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentAccountBinding;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.CryptographyManager;
import com.android.covidsafe.utilities.CryptographyManagerImpl;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AccountFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    SecureDatabase secureDatabase;

    private CryptographyManager cryptographyManager = new CryptographyManagerImpl();

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @VisibleForTesting
    AutoClearedValue<FragmentAccountBinding> binding;

    private AccountViewModel accountViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAccountBinding dataBinding = FragmentAccountBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(AccountViewModel.class);

        binding.get().setAccountViewModel(accountViewModel);
        binding.get().setLifecycleOwner(this);

        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        binding.get().tvAccountProfile.setOnClickListener(this::navEditPersonal);
        binding.get().tvAccountSetting.setOnClickListener(this::navSetting);
        binding.get().tvAccountLogout.setOnClickListener(this::navAuthActivity);
    }

    private void navEditPersonal(View view) {
        Navigation.findNavController(view).navigate(R.id.action_account_fragment_to_profile_fragment);
    }

    private void navSetting(View view) {
        Navigation.findNavController(view).navigate(R.id.action_account_fragment_to_setting_fragment);
    }

    private void navAuthActivity(View view) {
        AsyncTask.execute(() -> secureDatabase.clearAllTables());
        cryptographyManager.persistCiphertextWrapperToSharedPrefs(null, getContext(), "biometric_prefs", 0, "ciphertext_wrapper");
        Toast.makeText(getContext(), getString(R.string.toast_logout_success), Toast.LENGTH_SHORT).show();
        Navigation.findNavController(view).navigate(R.id.action_account_fragment_to_auth_activity);
    }
}