package com.android.covidsafe.ui.main.account.setting;

import static com.android.covidsafe.utilities.Constants.CIPHERTEXT_WRAPPER;
import static com.android.covidsafe.utilities.Constants.SHARED_PREFS_FILENAME;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentSettingBinding;
import com.android.covidsafe.utilities.AccessTokenAuthenticator;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.BiometricPromptUtils;
import com.android.covidsafe.utilities.CiphertextWrapper;
import com.android.covidsafe.utilities.CryptographyManager;
import com.android.covidsafe.utilities.CryptographyManagerImpl;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SettingFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentSettingBinding> binding;

    private CryptographyManager cryptographyManager = new CryptographyManagerImpl();

    private CiphertextWrapper ciphertextWrapper;

    private SettingViewModel settingViewModel;

    @Inject
    AccessTokenAuthenticator accessTokenAuthenticator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSettingBinding dataBinding = FragmentSettingBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(SettingViewModel.class);

        ciphertextWrapper = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
                getContext(),
                SHARED_PREFS_FILENAME,
                Context.MODE_PRIVATE,
                CIPHERTEXT_WRAPPER
        );

        int canAuthenticate = BiometricManager.from(getContext()).canAuthenticate();
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            binding.get().smSettingFingerprint.setEnabled(true);
        } else {
            binding.get().smSettingFingerprint.setEnabled(false);
        }
        if (ciphertextWrapper != null) {
            binding.get().smSettingFingerprint.setChecked(true);
        }

        initClickListener();
    }

    private void initClickListener() {
        binding.get().smSettingFingerprint.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                showBiometricPromptForEncryption();
            } else {
                cryptographyManager.persistCiphertextWrapperToSharedPrefs(null, getContext(), "biometric_prefs", 0, "ciphertext_wrapper");
            }
        });
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
            if (cipher != null && accessTokenAuthenticator.getRefreshToken() != null) {
                CiphertextWrapper encryptedServerTokenWrapper = cryptographyManager.encryptData(accessTokenAuthenticator.getRefreshToken(), cipher);
                cryptographyManager.persistCiphertextWrapperToSharedPrefs(encryptedServerTokenWrapper, getContext(), "biometric_prefs", 0, "ciphertext_wrapper");
            }
        }
    }

}