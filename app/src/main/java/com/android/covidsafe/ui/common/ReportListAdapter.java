package com.android.covidsafe.ui.common;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingComponent;

import com.android.covidsafe.R;
import com.android.covidsafe.databinding.ItemReportBinding;
import com.android.covidsafe.vo.Report;

import java.util.Objects;

/**
 * A RecyclerView adapter for {@link Report} class.
 */
public class ReportListAdapter extends DataBoundListAdapter<Report, ItemReportBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final ReportClickCallback reportClickCallback;

    public ReportListAdapter(DataBindingComponent dataBindingComponent, ReportClickCallback reportClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.reportClickCallback = reportClickCallback;
    }

    @Override
    protected ItemReportBinding createBinding(ViewGroup parent) {
        ItemReportBinding binding = ItemReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Report report = binding.getReport();
            if (report != null && reportClickCallback != null) {
                reportClickCallback.onClick(report);
            }
        });
        return binding;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void bind(ItemReportBinding binding, Report item) {
        binding.setReport(item);
    }

    @Override
    protected boolean areItemsTheSame(Report oldItem, Report newItem) {
        return Objects.equals(oldItem.id, newItem.id) &&
                Objects.equals(oldItem.userId, newItem.userId);
    }

    @Override
    protected boolean areContentsTheSame(Report oldItem, Report newItem) {
        return Objects.equals(oldItem.status, newItem.status) &&
                oldItem.address == newItem.address;
    }

    public interface ReportClickCallback {
        void onClick(Report report);
    }
}
