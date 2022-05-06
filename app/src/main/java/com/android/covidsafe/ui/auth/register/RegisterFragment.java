package com.android.covidsafe.ui.auth.register;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentRegisterBinding;
import com.android.covidsafe.utilities.AccessTokenAuthenticator;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.BiometricPromptUtils;
import com.android.covidsafe.utilities.CiphertextWrapper;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.utilities.CryptographyManager;
import com.android.covidsafe.utilities.CryptographyManagerImpl;
import com.android.covidsafe.utilities.FieldValidators;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Status;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RegisterFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    AccessTokenAuthenticator accessTokenAuthenticator;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentRegisterBinding> binding;

    private CryptographyManager cryptographyManager = new CryptographyManagerImpl();

    private RegisterViewModel registerViewModel;

    private String refreshToken;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentRegisterBinding dataBinding = FragmentRegisterBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(RegisterViewModel.class);

        binding.get().setRegisterViewModel(registerViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initInputListener();
        initClickListener();
    }

    private void subscribeUi() {
        MutableLiveData<String> liveData = NavHostFragment.findNavController(this).getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData(Constants.OTP_KEY);
        liveData.observe(getViewLifecycleOwner(), s -> doRegister(s));

        registerViewModel.getTokenResource().observe(getViewLifecycleOwner(), tokenResource -> {
            if (tokenResource.status == Status.SUCCESS && tokenResource.data != null) {
                Toast.makeText(getContext(), "Đăng ký thành công. Bạn vui lòng cập nhật hồ sơ người dùng để sử dụng đầy đủ chức năng ứng dụng.", Toast.LENGTH_SHORT).show();

                refreshToken = tokenResource.data.refreshToken;

                accessTokenAuthenticator.setAccessToken(tokenResource.data.accessToken);
                accessTokenAuthenticator.setRefreshToken(tokenResource.data.refreshToken);

                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Mở khoá bằng vân tay")
                        .setMessage("Sử dụng dấu vân tay để mở khoá ứng dụng một cánh nhanh chóng và thuận lợi")
                        .setCancelable(false)
                        .setNegativeButton("Để sau", (dialogInterface, i) -> {
                            navToMainActivity();
                        })
                        .setPositiveButton("Bật", (dialogInterface, i) -> {
                            showBiometricPromptForEncryption();
//                            navToMainActivity();
                        })
                        .show();
            }
        });
    }

    private void doRegister(String verificationKey) {
        @SuppressLint("HardwareIds") String androidId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        registerViewModel.setRegisterUser(androidId, verificationKey);
    }

    private void initInputListener() {
        binding.get().itRegisterPassword.addTextChangedListener(new TextFieldValidation(binding.get().itRegisterPassword));
    }

    private void initClickListener() {
        binding.get().btnRegisterRegister.setOnClickListener(this::doVerify);
    }

    private void doVerify(View view) {
        if (isValidate()) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.REGISTER_KEY, registerViewModel.phoneNumber.getValue());
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_verificationDialog, bundle);
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_inputinvalidate), Toast.LENGTH_SHORT);
        }
    }

    private Boolean isValidate() {
        return validatePhoneNumber() && validatePassword();
    }

    private Boolean validatePhoneNumber() {
        if (binding.get().itRegisterPhoneNumber.getText().toString().trim().isEmpty()) {
            binding.get().ilRegisterPhoneNumber.setError(getResources().getText(R.string.error_phone_number_invalid));
            binding.get().itRegisterPhoneNumber.requestFocus();
            return false;
        } else {
            binding.get().ilRegisterPhoneNumber.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validatePassword() {
        if (binding.get().itRegisterPassword.getText().toString().trim().isEmpty()) {
            binding.get().ilRegisterPassword.setError(getResources().getText(R.string.error_password_required_field));
            binding.get().itRegisterPassword.requestFocus();
            return false;
        } else if (binding.get().itRegisterPassword.getText().toString().length() < 6) {
            binding.get().ilRegisterPassword.setError(getResources().getText(R.string.error_password_cant_be_less_than_6));
            binding.get().itRegisterPassword.requestFocus();
            return false;
        } else if (!FieldValidators.isStringContainNumber(binding.get().itRegisterPassword.getText().toString())) {
            binding.get().ilRegisterPassword.setError(getResources().getText(R.string.error_password_1_digit));
            binding.get().itRegisterPassword.requestFocus();
            return false;
        } else if (!FieldValidators.isStringLowerAndUpperCase(binding.get().itRegisterPassword.getText().toString())) {
            binding.get().ilRegisterPassword.setError(getResources().getText(R.string.error_password_upper_and_lower_case));
            binding.get().itRegisterPassword.requestFocus();
            return false;
        } else if (!FieldValidators.isStringContainSpecialCharacter(binding.get().itRegisterPassword.getText().toString())) {
            binding.get().ilRegisterPassword.setError(getResources().getText(R.string.error_password_1_special_character));
            binding.get().itRegisterPassword.requestFocus();
            return false;
        } else {
            binding.get().ilRegisterPassword.setErrorEnabled(false);
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
                case R.id.it_register_phone_number:
                    validatePhoneNumber();
                    break;
                case R.id.it_register_password:
                    validatePassword();
                    break;
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }

    private void showBiometricPromptForEncryption() {
        int canAuthenticate = BiometricManager.from(getContext()).canAuthenticate();
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            String secretKeyName = "SECRET_KEY_NAME";
            Cipher cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName);

            BiometricPrompt biometricPrompt = BiometricPromptUtils.INSTANCE.createBiometricPrompt((AppCompatActivity) requireActivity(), new Function1() {
                public Object invoke(Object obj) {
                    this.invoke((BiometricPrompt.AuthenticationResult) obj);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull BiometricPrompt.AuthenticationResult result) {
                    encryptAndStoreServerToken(result);
                }
            });

            BiometricPrompt.PromptInfo promptInfo = BiometricPromptUtils.INSTANCE.createPromptInfo((AppCompatActivity) requireActivity());
            biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
        }
    }

    private final void encryptAndStoreServerToken(BiometricPrompt.AuthenticationResult authResult) {
        BiometricPrompt.CryptoObject cryptoObject = authResult.getCryptoObject();
        if (cryptoObject != null) {
            Cipher cipher = cryptoObject.getCipher();
            if (cipher != null && refreshToken != null) {
                CiphertextWrapper encryptedServerTokenWrapper = cryptographyManager.encryptData(refreshToken, cipher);
                cryptographyManager.persistCiphertextWrapperToSharedPrefs(encryptedServerTokenWrapper, getContext(), "biometric_prefs", 0, "ciphertext_wrapper");
            }
        }
        navToMainActivity();
    }

    private void navToMainActivity() {
        NavHostFragment.findNavController(this).navigate(R.id.action_registerFragment_to_mainActivity);
    }
}
