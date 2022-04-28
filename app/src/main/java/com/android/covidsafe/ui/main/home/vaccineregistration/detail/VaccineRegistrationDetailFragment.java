package com.android.covidsafe.ui.main.home.vaccineregistration.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentVaccineRegistrationDetailBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.VaccineRegistration;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class VaccineRegistrationDetailFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentVaccineRegistrationDetailBinding> binding;

    private VaccineRegistrationDetailViewModel vaccineRegistrationDetailViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentVaccineRegistrationDetailBinding dataBinding = FragmentVaccineRegistrationDetailBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vaccineRegistrationDetailViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(VaccineRegistrationDetailViewModel.class);
        binding.get().setVaccineRegistrationDetailViewModel(vaccineRegistrationDetailViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();

        if (getArguments() != null) {
            vaccineRegistrationDetailViewModel.setVaccineRegistrationId(getArguments().getString(Constants.VACCINE_REGISTRATION_KEY));
            getArguments().clear();
        } else {
            Toast.makeText(getContext(), R.string.error_load_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void subscribeUi() {
        vaccineRegistrationDetailViewModel.getVaccineRegistrationResource().observe(getViewLifecycleOwner(), vaccineRegistrationResource -> {
            if (vaccineRegistrationResource.data != null) {
                VaccineRegistration vaccineRegistration = vaccineRegistrationResource.data;
                binding.get().setVaccineRegistration(vaccineRegistration);

                vaccineRegistrationDetailViewModel.setPriority(vaccineRegistration.priority != null ? vaccineRegistration.priority : "0");
                vaccineRegistrationDetailViewModel.setProvince(vaccineRegistration.province != null ? vaccineRegistration.province : "0");
                vaccineRegistrationDetailViewModel.setDistrict(vaccineRegistration.district != null ? vaccineRegistration.district : "0");
                vaccineRegistrationDetailViewModel.setWard(vaccineRegistration.ward != null ? vaccineRegistration.ward : "0");
                vaccineRegistrationDetailViewModel.setNationality(vaccineRegistration.nationality != null ? vaccineRegistration.nationality : "0");
                vaccineRegistrationDetailViewModel.setEthnic(vaccineRegistration.ethnic != null ? vaccineRegistration.ethnic : "0");
            }
        });
    }
}
