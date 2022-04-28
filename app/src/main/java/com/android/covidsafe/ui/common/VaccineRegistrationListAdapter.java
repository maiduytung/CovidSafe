package com.android.covidsafe.ui.common;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingComponent;

import com.android.covidsafe.databinding.ItemVaccineRegistrationBinding;
import com.android.covidsafe.vo.VaccineRegistration;

import java.util.Objects;

/**
 * A RecyclerView adapter for {@link VaccineRegistration} class.
 */
public class VaccineRegistrationListAdapter extends DataBoundListAdapter<VaccineRegistration, ItemVaccineRegistrationBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final VaccineRegistrationClickCallback vaccineRegistrationClickCallback;

    public VaccineRegistrationListAdapter(DataBindingComponent dataBindingComponent, VaccineRegistrationClickCallback vaccineRegistrationClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.vaccineRegistrationClickCallback = vaccineRegistrationClickCallback;
    }

    @Override
    protected ItemVaccineRegistrationBinding createBinding(ViewGroup parent) {
        ItemVaccineRegistrationBinding binding = ItemVaccineRegistrationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            VaccineRegistration vaccineRegistration = binding.getVaccineRegistration();
            if (vaccineRegistration != null && vaccineRegistrationClickCallback != null) {
                vaccineRegistrationClickCallback.onClick(vaccineRegistration);
            }
        });
        return binding;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void bind(ItemVaccineRegistrationBinding binding, VaccineRegistration item) {
        binding.setVaccineRegistration(item);
    }

    @Override
    protected boolean areItemsTheSame(VaccineRegistration oldItem, VaccineRegistration newItem) {
        return Objects.equals(oldItem.id, newItem.id) &&
                Objects.equals(oldItem.userId, newItem.userId);
    }

    @Override
    protected boolean areContentsTheSame(VaccineRegistration oldItem, VaccineRegistration newItem) {
        return Objects.equals(oldItem.fullName, newItem.fullName) &&
                oldItem.dateOfBirth == newItem.dateOfBirth;
    }

    public interface VaccineRegistrationClickCallback {
        void onClick(VaccineRegistration vaccineRegistration);
    }
}
