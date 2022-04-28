package com.android.covidsafe.ui.auth.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentLoginBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.FieldValidators;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Status;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class LoginFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentLoginBinding> binding;

    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentLoginBinding dataBinding = FragmentLoginBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel.class);

        binding.get().setLoginViewModel(loginViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initInputListener();
        initClickListener();

    }

    public void subscribeUi() {
        loginViewModel.getTokenResource().observe(getViewLifecycleOwner(), tokenResource -> {
            if (tokenResource.status == Status.SUCCESS) {
                Toast.makeText(getContext(), getString(R.string.toast_login_success), Toast.LENGTH_SHORT).show();

                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_mainActivity);
            } else if (tokenResource.status == Status.ERROR) {
                Toast.makeText(getContext(), getString(R.string.toast_login_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initInputListener() {
        binding.get().itLoginUsername.addTextChangedListener(new TextFieldValidation(binding.get().itLoginUsername));
        binding.get().itLoginPassword.addTextChangedListener(new TextFieldValidation(binding.get().itLoginPassword));
    }

    private Boolean isValidate() {
        return validateUserName() && validatePassword();
    }

    private Boolean validateUserName() {
        if (binding.get().itLoginUsername.getText().toString().trim().isEmpty()) {
            binding.get().ilLoginUsername.setError(getResources().getText(R.string.error_username_required_field));
            binding.get().itLoginUsername.requestFocus();
            return false;
        } else {
            binding.get().ilLoginUsername.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validatePassword() {
        if (binding.get().itLoginPassword.getText().toString().trim().isEmpty()) {
            binding.get().ilLoginPassword.setError(getResources().getText(R.string.error_password_required_field));
            binding.get().itLoginPassword.requestFocus();
            return false;
        } else if (binding.get().itLoginPassword.getText().toString().length() < 6) {
            binding.get().ilLoginPassword.setError(getResources().getText(R.string.error_password_cant_be_less_than_6));
            binding.get().itLoginPassword.requestFocus();
            return false;
        } else if (!FieldValidators.isStringContainNumber(binding.get().itLoginPassword.getText().toString())) {
            binding.get().ilLoginPassword.setError(getResources().getText(R.string.error_password_1_digit));
            binding.get().itLoginPassword.requestFocus();
            return false;
        } else if (!FieldValidators.isStringLowerAndUpperCase(binding.get().itLoginPassword.getText().toString())) {
            binding.get().ilLoginPassword.setError(getResources().getText(R.string.error_password_upper_and_lower_case));
            binding.get().itLoginPassword.requestFocus();
            return false;
        } else if (!FieldValidators.isStringContainSpecialCharacter(binding.get().itLoginPassword.getText().toString())) {
            binding.get().ilLoginPassword.setError(getResources().getText(R.string.error_password_1_special_character));
            binding.get().itLoginPassword.requestFocus();
            return false;
        } else {
            binding.get().ilLoginPassword.setErrorEnabled(false);
        }
        return true;
    }

    private class TextFieldValidation implements TextWatcher {
        private final View view;

        private TextFieldValidation(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @SuppressLint("NonConstantResourceId")
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.it_login_username:
                    validateUserName();
                    break;
                case R.id.it_login_password:
                    validatePassword();
                    break;
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }

    private void initClickListener() {

        binding.get().btnLoginLogin.setOnClickListener(this::doLogin);
        binding.get().tvLoginRegister.setOnClickListener(this::navToRegister);
    }

    private void doLogin(View view) {
        if (isValidate()) {
            @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            loginViewModel.setLoginUser(androidId);
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_login_non_null), Toast.LENGTH_SHORT).show();
        }
    }

    private void navToRegister(View view) {
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
    }

}
