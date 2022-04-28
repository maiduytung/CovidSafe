package com.android.covidsafe.ui.main.home.vaccineregistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentVaccineRegistrationBinding;
import com.android.covidsafe.ui.common.VaccineRegistrationListAdapter;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.VaccineRegistration;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class VaccineRegistrationFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentVaccineRegistrationBinding> binding;

    AutoClearedValue<VaccineRegistrationListAdapter> adapter;

    private VaccineRegistrationViewModel vaccineRegistrationViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentVaccineRegistrationBinding dataBinding = FragmentVaccineRegistrationBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vaccineRegistrationViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(VaccineRegistrationViewModel.class);
        binding.get().setVaccineRegistrationViewmodel(vaccineRegistrationViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initRecyclerView();
        initClickListener();

        VaccineRegistrationListAdapter rvAdapter = new VaccineRegistrationListAdapter(dataBindingComponent, vaccineRegistration -> navigateToVaccineRegistrationDetail(view, vaccineRegistration));
        binding.get().rvVaccineRegistrationList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        binding.get().setQuery(getString(R.string.vaccine_registration_title));
        vaccineRegistrationViewModel.setQuery("Vaccine Registration");

        binding.get().setCallback(() -> vaccineRegistrationViewModel.refresh());
    }

    private void initRecyclerView() {

        binding.get().rvVaccineRegistrationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == adapter.get().getItemCount() - 1) {
                    vaccineRegistrationViewModel.loadNextPage();
                }
            }
        });
        vaccineRegistrationViewModel.getResults().observe(getViewLifecycleOwner(), result -> {
            binding.get().setVaccineRegistrationResource(result);
            binding.get().setResultCount((result == null || result.data == null)
                    ? 0 : result.data.size());
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });

        vaccineRegistrationViewModel.getLoadMoreStatus().observe(getViewLifecycleOwner(), loadingMore -> {
            if (loadingMore == null) {
                binding.get().setLoadingMore(false);
            } else {
                binding.get().setLoadingMore(loadingMore.isRunning());
                String error = loadingMore.getErrorMessageIfNotHandled();
                if (error != null) {
                    Snackbar.make(binding.get().loadMoreBar, error, Snackbar.LENGTH_LONG).show();
                }
            }
            binding.get().executePendingBindings();
        });
    }

    private void subscribeUi() {
    }

    private void initClickListener() {
        binding.get().btnVaccineRegistrationAdd.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_vaccine_registration_fragment_to_add_vaccine_registration_fragment));
    }

    private void navigateToVaccineRegistrationDetail(View view, VaccineRegistration vaccineRegistration) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.VACCINE_REGISTRATION_KEY, vaccineRegistration.id);
        Navigation.findNavController(view).navigate(R.id.action_vaccine_registration_fragment_to_vaccine_registration_detail_fragment, bundle);
    }

}
