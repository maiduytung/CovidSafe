package com.android.covidsafe.ui.main.home.report.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;

import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentReportDetailBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Report;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ReportDetailFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentReportDetailBinding> binding;

    private ReportDetailViewModel reportDetailViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentReportDetailBinding dataBinding = FragmentReportDetailBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reportDetailViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ReportDetailViewModel.class);

        subscribeUi();

        if (getArguments() != null) {
            reportDetailViewModel.setReportId(getArguments().getString(Constants.REPORT_KEY));
            getArguments().clear();
        }
    }

    private void subscribeUi() {

        reportDetailViewModel.getReportResource().observe(getViewLifecycleOwner(), reportResource -> {
            if (reportResource.data != null) {
                Report report = reportResource.data;
                binding.get().setReport(report);
            }
        });

    }
}
