package com.android.covidsafe.ui.main.home.vaccination;

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
import com.android.covidsafe.databinding.FragmentVaccinationBinding;
import com.android.covidsafe.ui.common.VaccinationListAdapter;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Status;
import com.android.covidsafe.vo.Vaccination;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class VaccinationFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentVaccinationBinding> binding;

    AutoClearedValue<VaccinationListAdapter> adapter;

    private VaccinationViewModel vaccinationViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentVaccinationBinding dataBinding = FragmentVaccinationBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vaccinationViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(VaccinationViewModel.class);
        binding.get().setVaccinationViewModel(vaccinationViewModel);
        binding.get().setLifecycleOwner(this);

        initRecyclerView();

        VaccinationListAdapter rvAdapter = new VaccinationListAdapter(dataBindingComponent, vaccination -> navigateToVaccinationDetail(view, vaccination));
        binding.get().rvVaccinationList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        vaccinationViewModel.getProfileResource().observe(getViewLifecycleOwner(), profileResource -> {
            if (profileResource.status == Status.SUCCESS && profileResource.data != null && profileResource.data.identification != null) {
                vaccinationViewModel.setQuery(profileResource.data.identification);
            }
        });

        binding.get().setCallback(() -> vaccinationViewModel.refresh());
    }

    private void initRecyclerView() {

        binding.get().rvVaccinationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == adapter.get().getItemCount() - 1) {
                    vaccinationViewModel.loadNextPage();
                }
            }
        });
        vaccinationViewModel.getResults().observe(getViewLifecycleOwner(), result -> {
            binding.get().setVaccinationResource(result);
            binding.get().setResultCount((result == null || result.data == null)
                    ? 0 : result.data.size());
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });

        vaccinationViewModel.getLoadMoreStatus().observe(getViewLifecycleOwner(), loadingMore -> {
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

    private void navigateToVaccinationDetail(View view, Vaccination vaccination) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.VACCINATION_KEY, vaccination.id);
        Navigation.findNavController(view).navigate(R.id.action_vaccination_fragment_to_vaccination_detail_fragment, bundle);
    }

}
