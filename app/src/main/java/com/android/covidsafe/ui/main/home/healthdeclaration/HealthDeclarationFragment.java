package com.android.covidsafe.ui.main.home.healthdeclaration;

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
import com.android.covidsafe.databinding.FragmentHealthDeclarationBinding;
import com.android.covidsafe.ui.common.HealthDeclarationListAdapter;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.HealthDeclaration;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class HealthDeclarationFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentHealthDeclarationBinding> binding;

    AutoClearedValue<HealthDeclarationListAdapter> adapter;

    private HealthDeclarationViewModel healthDeclarationViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentHealthDeclarationBinding dataBinding = FragmentHealthDeclarationBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        healthDeclarationViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(HealthDeclarationViewModel.class);

        initRecyclerView();
        initClickListener();

        HealthDeclarationListAdapter rvAdapter = new HealthDeclarationListAdapter(dataBindingComponent, healthDeclaration -> navigateToHealthDeclarationDetail(view, healthDeclaration));
        binding.get().rvHealthDeclarationList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        binding.get().setQuery(getString(R.string.health_declaration_title));
        healthDeclarationViewModel.setQuery("Health Declaration");

        binding.get().setCallback(() -> healthDeclarationViewModel.refresh());
    }

    private void initRecyclerView() {

        binding.get().rvHealthDeclarationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == adapter.get().getItemCount() - 1) {
                    healthDeclarationViewModel.loadNextPage();
                }
            }
        });
        healthDeclarationViewModel.getResults().observe(getViewLifecycleOwner(), result -> {
            binding.get().setHealthDeclarationResource(result);
            binding.get().setResultCount((result == null || result.data == null)
                    ? 0 : result.data.size());
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });

        healthDeclarationViewModel.getLoadMoreStatus().observe(getViewLifecycleOwner(), loadingMore -> {
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

    private void initClickListener() {
        binding.get().btnHealthDeclaration.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_health_declaration_fragment_to_add_health_declaration_fragment));
    }

    private void navigateToHealthDeclarationDetail(View view, HealthDeclaration healthDeclaration) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.HEALTH_DECLARATION_KEY, healthDeclaration.id);
        Navigation.findNavController(view).navigate(R.id.action_health_declaration_fragment_to_health_declaration_detail_fragment, bundle);
    }
}
