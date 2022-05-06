package com.android.covidsafe.utilities;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.covidsafe.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import kotlin.jvm.functions.Function1;

public final class BiometricPromptUtils {
    private static final String TAG = "BiometricPromptUtils";
    @NotNull
    public static final BiometricPromptUtils INSTANCE;

    @NotNull
    public final BiometricPrompt createBiometricPrompt(@NotNull AppCompatActivity activity, @NotNull final Function1 processSuccess) {
        Executor executor = ContextCompat.getMainExecutor((Context) activity);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            public void onAuthenticationError(int errCode, @NotNull CharSequence errString) {
                super.onAuthenticationError(errCode, errString);
                Log.d("BiometricPromptUtils", (new StringBuilder()).append("errCode is ").append(errCode).append(" and errString is: ").append(errString).toString());
            }

            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d("BiometricPromptUtils", "User biometric rejected.");
            }

            public void onAuthenticationSucceeded(@NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d("BiometricPromptUtils", "Authentication was successful");
                processSuccess.invoke(result);
            }
        };
        return new BiometricPrompt((FragmentActivity) activity, executor, (BiometricPrompt.AuthenticationCallback) callback);
    }

    @NotNull
    public final BiometricPrompt.PromptInfo createPromptInfo(@NotNull AppCompatActivity activity) {
        BiometricPrompt.PromptInfo.Builder builder = new BiometricPrompt.PromptInfo.Builder();
        builder.setTitle((CharSequence) activity.getString(R.string.prompt_info_title));
        builder.setSubtitle((CharSequence) activity.getString(R.string.prompt_info_subtitle));
        builder.setDescription((CharSequence) activity.getString(R.string.prompt_info_description));
        builder.setConfirmationRequired(false);
        builder.setNegativeButtonText((CharSequence) activity.getString(R.string.prompt_info_use_app_password));
        return builder.build();
    }

    private BiometricPromptUtils() {
    }

    static {
        INSTANCE = new BiometricPromptUtils();
    }
}
