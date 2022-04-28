package com.android.covidsafe.ui.main.home.healthdeclaration.detail;

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
import com.android.covidsafe.databinding.FragmentHealthDeclarationDetailBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.HealthDeclaration;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class HealthDeclarationDetailFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentHealthDeclarationDetailBinding> binding;

    private HealthDeclarationDetailViewModel healthDeclarationDetailViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentHealthDeclarationDetailBinding dataBinding = FragmentHealthDeclarationDetailBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        healthDeclarationDetailViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(HealthDeclarationDetailViewModel.class);
        binding.get().setHealthDeclarationDetailViewModel(healthDeclarationDetailViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        if (getArguments() != null) {
            healthDeclarationDetailViewModel.setHealthDeclarationId(getArguments().getString(Constants.HEALTH_DECLARATION_KEY));
            getArguments().clear();
        } else {
            Toast.makeText(getContext(), R.string.error_load_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void subscribeUi() {

        healthDeclarationDetailViewModel.getHealthDeclarationResource().observe(getViewLifecycleOwner(), healthDeclarationResource -> {
            if (healthDeclarationResource.data != null) {
                HealthDeclaration healthDeclaration = healthDeclarationResource.data;
                binding.get().setHealthDeclaration(healthDeclaration);

                healthDeclarationDetailViewModel.setProvince(healthDeclaration.province != null ? healthDeclaration.province : "0");
                healthDeclarationDetailViewModel.setDistrict(healthDeclaration.district != null ? healthDeclaration.district : "0");
                healthDeclarationDetailViewModel.setWard(healthDeclaration.ward != null ? healthDeclaration.ward : "0");
                healthDeclarationDetailViewModel.setNationality(healthDeclaration.nationality != null ? healthDeclaration.nationality : "0");

            }
        });
    }
}
