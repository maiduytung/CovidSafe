package com.android.covidsafe.ui.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.android.covidsafe.R;
import com.android.covidsafe.databinding.DialogVerificationBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Status;
import com.android.covidsafe.vo.request.SendVerificationRequest;

import javax.inject.Inject;

import dagger.android.support.DaggerDialogFragment;

public class VerificationDialog extends DaggerDialogFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    AutoClearedValue<DialogVerificationBinding> binding;

    private VerificationViewModel verificationViewModel;

    private CountDownTimer countDownTimer;
    private boolean isExpired = false;

    private String phoneNumber;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                .TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        DialogVerificationBinding dataBinding = DialogVerificationBinding.inflate(inflater, container, false);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        verificationViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(VerificationViewModel.class);
        binding.get().setOtpViewModel(verificationViewModel);
        binding.get().setLifecycleOwner(this);

        initPhoneNumber();
        initCountdownTime();
        initOtpDialogInputListener();

        String androidId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        SendVerificationRequest sendVerificationRequest = new SendVerificationRequest("0" + phoneNumber, androidId);
        verificationViewModel.setOTPRequest(sendVerificationRequest);

        observeGetOTP();
        observeVerificationOTP();
    }

    private void initPhoneNumber() {
        phoneNumber = getArguments().getString(Constants.REGISTER_KEY);
        binding.get().setPhoneNumber("0" + phoneNumber);
    }

    private void initCountdownTime() {
        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.get().setCountdown(millisUntilFinished / 1000);
            }

            public void onFinish() {
                isExpired = true;
                binding.get().setIsExpired(isExpired);
                Toast.makeText(getContext(), getString(R.string.toast_activationcodeisexpired), Toast.LENGTH_SHORT);
            }
        }.start();
    }

    private void initOtpDialogInputListener() {
        binding.get().buttonCancel.setOnClickListener(this::doCancel);
        binding.get().buttonOk.setOnClickListener(this::doOk);
        binding.get().textviewResendCode.setOnClickListener(this::doResendCode);
    }

    private void doCancel(View view) {
        countDownTimer.cancel();
        getDialog().dismiss();
    }

    private void doOk(View view) {
        verificationViewModel.setOTPVerificationRequest();
    }

    private void doResendCode(View view) {
        countDownTimer.start();
    }

    private void observeGetOTP() {
        verificationViewModel.getOTPResults().observe(getViewLifecycleOwner(), sendOTPResource -> {
            if (sendOTPResource.data != null) {
                Toast.makeText(getContext(), getString(R.string.toast_otpsentsuccessfully) + " 0" + phoneNumber, Toast.LENGTH_SHORT);
            }
        });
    }

    private void observeVerificationOTP() {
        verificationViewModel.getVerificationResults().observe(getViewLifecycleOwner(), sendOTPResource -> {
            if (sendOTPResource.status == Status.ERROR) {
                Toast.makeText(getContext(), getString(R.string.toast_otpinvalidorexpired), Toast.LENGTH_SHORT);
            } else if (sendOTPResource.status == Status.SUCCESS) {
                countDownTimer.cancel();
                NavHostFragment.findNavController(this)
                        .getPreviousBackStackEntry()
                        .getSavedStateHandle()
                        .set(Constants.OTP_KEY, sendOTPResource.data.verificationKey);
                getDialog().dismiss();
            }
        });
    }
}
