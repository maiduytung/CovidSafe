package com.android.covidsafe.ui.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentHomeBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Status;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class HomeFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @VisibleForTesting
    AutoClearedValue<FragmentHomeBinding> binding;

    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding dataBinding = FragmentHomeBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel.class);
        binding.get().setHomeViewModel(homeViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initClickListener();
    }

    private void subscribeUi() {
        homeViewModel.getProfileResource().observe(getViewLifecycleOwner(), profileResource -> {
            if (profileResource.status == Status.SUCCESS && profileResource.data != null && profileResource.data.identification != null) {
                homeViewModel.setIdentification(profileResource.data.identification);
            }
        });
        homeViewModel.getCertificationResource().observe(getViewLifecycleOwner(), certificationResource -> {
            if (certificationResource.status == Status.SUCCESS && certificationResource.data != null) {
                binding.get().setCertification(certificationResource.data);
            }
        });
    }

    public void initClickListener() {
        binding.get().cvHomeCertification.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_home_fragment_to_vaccination_fragment));
        binding.get().cvHomeHealthDeclaration.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_home_fragment_to_health_declaration_fragment));
        binding.get().cvHomeVaccineRegistration.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_home_fragment_to_vaccine_registration_fragment));
        binding.get().cvHomeReport.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_home_fragment_to_report_fragment));
    }
}