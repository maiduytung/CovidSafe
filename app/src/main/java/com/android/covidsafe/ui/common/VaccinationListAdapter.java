package com.android.covidsafe.ui.common;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingComponent;

import com.android.covidsafe.R;
import com.android.covidsafe.databinding.ItemVaccinationBinding;
import com.android.covidsafe.vo.Vaccination;

import java.util.Objects;

/**
 * A RecyclerView adapter for {@link Vaccination} class.
 */
public class VaccinationListAdapter extends DataBoundListAdapter<Vaccination, ItemVaccinationBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final VaccinationClickCallback vaccinationClickCallback;

    public VaccinationListAdapter(DataBindingComponent dataBindingComponent, VaccinationClickCallback vaccinationClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.vaccinationClickCallback = vaccinationClickCallback;
    }

    @Override
    protected ItemVaccinationBinding createBinding(ViewGroup parent) {
        ItemVaccinationBinding binding = ItemVaccinationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Vaccination vaccination = binding.getVaccination();
            if (vaccination != null && vaccinationClickCallback != null) {
                vaccinationClickCallback.onClick(vaccination);
            }
        });
        return binding;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void bind(ItemVaccinationBinding binding, Vaccination item) {
        binding.setVaccination(item);
    }

    @Override
    protected boolean areItemsTheSame(Vaccination oldItem, Vaccination newItem) {
        return Objects.equals(oldItem.id, newItem.id) &&
                Objects.equals(oldItem.vaccinationDate, newItem.vaccinationDate);
    }

    @Override
    protected boolean areContentsTheSame(Vaccination oldItem, Vaccination newItem) {
        return Objects.equals(oldItem.vaccinationDate, newItem.vaccinationDate) &&
                oldItem.address == newItem.address;
    }

    public interface VaccinationClickCallback {
        void onClick(Vaccination vaccination);
    }
}
