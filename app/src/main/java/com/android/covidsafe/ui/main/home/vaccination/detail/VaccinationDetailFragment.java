package com.android.covidsafe.ui.main.home.vaccination.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;

import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentVaccinationDetailBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Vaccination;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class VaccinationDetailFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentVaccinationDetailBinding> binding;

    private VaccinationDetailViewModel vaccinationDetailViewModel;
    private String vaccineRegistrationId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentVaccinationDetailBinding dataBinding = FragmentVaccinationDetailBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vaccinationDetailViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(VaccinationDetailViewModel.class);

        subscribeUi();

        if (getArguments() != null) {
            vaccinationDetailViewModel.setVaccinationId(getArguments().getString(Constants.VACCINATION_KEY));
            getArguments().clear();
        }
    }

    private void subscribeUi() {

        vaccinationDetailViewModel.getVaccinationResource().observe(getViewLifecycleOwner(), vaccinationResource -> {
            if (vaccinationResource.data != null) {
                Vaccination vaccination = vaccinationResource.data;
                vaccineRegistrationId = vaccination.vaccineRegistrationId;
                binding.get().setVaccination(vaccination);
            }
        });
    }
}
