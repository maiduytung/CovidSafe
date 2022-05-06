package com.android.covidsafe.ui.auth.login;

import static com.android.covidsafe.utilities.Constants.CIPHERTEXT_WRAPPER;
import static com.android.covidsafe.utilities.Constants.SHARED_PREFS_FILENAME;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentLoginBinding;
import com.android.covidsafe.generated.callback.OnClickListener;
import com.android.covidsafe.repository.SecureSharedPref;
import com.android.covidsafe.ui.auth.AuthActivity;
import com.android.covidsafe.utilities.AccessTokenAuthenticator;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.BiometricPromptUtils;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.utilities.CryptographyManager;
import com.android.covidsafe.utilities.CiphertextWrapper;
import com.android.covidsafe.utilities.CryptographyManagerImpl;
import com.android.covidsafe.utilities.FieldValidators;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Status;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import kotlin.LateinitKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;

public class LoginFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    AccessTokenAuthenticator accessTokenAuthenticator;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentLoginBinding> binding;

    private LoginViewModel loginViewModel;

    private CryptographyManager cryptographyManager = new CryptographyManagerImpl();

    private CiphertextWrapper ciphertextWrapper;

    private String refreshToken;

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

        ciphertextWrapper = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
                getContext(),
                SHARED_PREFS_FILENAME,
                Context.MODE_PRIVATE,
                CIPHERTEXT_WRAPPER
        );

        subscribeUi();
        initInputListener();
        initClickListener();

        int canAuthenticate = BiometricManager.from(getContext()).canAuthenticate();
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            binding.get().tvLoginFingerprint.setVisibility(View.VISIBLE);
        } else {
            binding.get().tvLoginFingerprint.setVisibility(View.INVISIBLE);
        }

    }

    public void subscribeUi() {
        loginViewModel.getTokenResource().observe(getViewLifecycleOwner(), tokenResource -> {
            if (tokenResource.status == Status.SUCCESS) {
                refreshToken = tokenResource.data.refreshToken;

                accessTokenAuthenticator.setAccessToken(tokenResource.data.accessToken);
                accessTokenAuthenticator.setRefreshToken(tokenResource.data.refreshToken);

                Toast.makeText(getContext(), getString(R.string.toast_login_success), Toast.LENGTH_SHORT).show();

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
        binding.get().tvLoginFingerprint.setOnClickListener(view -> {
            if (ciphertextWrapper != null) {
                showBiometricPromptForDecryption();
            } else {
                Toast.makeText(getContext(), getString(R.string.toast_login_fingerprint_not_setup), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void navToMainActivity() {
        NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_mainActivity);
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

    private final void showBiometricPromptForDecryption() {
        if (ciphertextWrapper != null) {
            String secretKeyName = "SECRET_KEY_NAME";
            Cipher cipher = this.cryptographyManager.getInitializedCipherForDecryption(secretKeyName, ciphertextWrapper.getInitializationVector());
            BiometricPrompt biometricPrompt = BiometricPromptUtils.INSTANCE.createBiometricPrompt((AppCompatActivity) requireActivity(), new Function1() {
                public Object invoke(Object obj) {
                    this.invoke((BiometricPrompt.AuthenticationResult) obj);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull BiometricPrompt.AuthenticationResult result) {
                    decryptServerTokenFromStorage(result);
                }
            });
            BiometricPrompt.PromptInfo promptInfo = BiometricPromptUtils.INSTANCE.createPromptInfo((AppCompatActivity) requireActivity());
            biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
        }

    }

    private final void decryptServerTokenFromStorage(BiometricPrompt.AuthenticationResult authResult) {
        if (ciphertextWrapper != null) {
            BiometricPrompt.CryptoObject cryptoObject = authResult.getCryptoObject();
            if (cryptoObject != null) {
                Cipher cipher = cryptoObject.getCipher();
                if (cipher != null) {
                    byte[] ciphertext = ciphertextWrapper.getCiphertext();
                    String plaintext = cryptographyManager.decryptData(ciphertext, cipher);

                    //add Token to App
                    accessTokenAuthenticator.setRefreshToken(plaintext);
                    accessTokenAuthenticator.setAccessToken("");

                    navToMainActivity();

                }
            }
        }
    }
}
