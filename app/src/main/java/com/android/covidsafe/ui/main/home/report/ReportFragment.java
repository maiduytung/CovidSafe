package com.android.covidsafe.ui.main.home.report;

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
import com.android.covidsafe.databinding.FragmentReportBinding;
import com.android.covidsafe.ui.common.ReportListAdapter;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Report;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ReportFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentReportBinding> binding;

    AutoClearedValue<ReportListAdapter> adapter;

    private ReportViewModel reportViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentReportBinding dataBinding = FragmentReportBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reportViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ReportViewModel.class);
        binding.get().setReportViewModel(reportViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initRecyclerView();
        initClickListener();

        ReportListAdapter rvAdapter = new ReportListAdapter(dataBindingComponent, report -> navigateToReportDetail(view, report));
        binding.get().rvReportList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        reportViewModel.setQuery("Report");

        binding.get().setCallback(() -> reportViewModel.refresh());
    }

    private void initRecyclerView() {

        binding.get().rvReportList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == adapter.get().getItemCount() - 1) {
                    reportViewModel.loadNextPage();
                }
            }
        });
        reportViewModel.getResults().observe(getViewLifecycleOwner(), result -> {
            binding.get().setReportResource(result);
            binding.get().setResultCount((result == null || result.data == null)
                    ? 0 : result.data.size());
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });

        reportViewModel.getLoadMoreStatus().observe(getViewLifecycleOwner(), loadingMore -> {
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
        binding.get().itReportContent.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_report_fragment_to_add_report_fragment));
    }

    private void navigateToReportDetail(View view, Report report) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.REPORT_KEY, report.id);
        Navigation.findNavController(view).navigate(R.id.action_report_fragment_to_report_detail_fragment, bundle);
    }

}
