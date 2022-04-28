package com.android.covidsafe.ui.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingComponent;

import com.android.covidsafe.databinding.ItemHealthDeclarationBinding;
import com.android.covidsafe.vo.HealthDeclaration;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A RecyclerView adapter for {@link HealthDeclaration} class.
 */
public class HealthDeclarationListAdapter extends DataBoundListAdapter<HealthDeclaration, ItemHealthDeclarationBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final HealthDeclarationClickCallback healthDeclarationClickCallback;

    public HealthDeclarationListAdapter(DataBindingComponent dataBindingComponent, HealthDeclarationClickCallback healthDeclarationClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.healthDeclarationClickCallback = healthDeclarationClickCallback;
    }

    @Override
    protected ItemHealthDeclarationBinding createBinding(ViewGroup parent) {
        ItemHealthDeclarationBinding binding = ItemHealthDeclarationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            HealthDeclaration healthDeclaration = binding.getHealthDeclaration();
            if (healthDeclaration != null && healthDeclarationClickCallback != null) {
                healthDeclarationClickCallback.onClick(healthDeclaration);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemHealthDeclarationBinding binding, HealthDeclaration item) {
        Date expiredDate = new Date(item.createdDate.getTime() + TimeUnit.DAYS.toMillis(14));
        if (expiredDate.before(new Date())) binding.setShowExpired(true);
        binding.setHealthDeclaration(item);
    }

    @Override
    protected boolean areItemsTheSame(HealthDeclaration oldItem, HealthDeclaration newItem) {
        return Objects.equals(oldItem.id, newItem.id) &&
                Objects.equals(oldItem.userId, newItem.userId);
    }

    @Override
    protected boolean areContentsTheSame(HealthDeclaration oldItem, HealthDeclaration newItem) {
        return Objects.equals(oldItem.fullName, newItem.fullName) &&
                oldItem.yearOfBirth == newItem.yearOfBirth;
    }

    public interface HealthDeclarationClickCallback {
        void onClick(HealthDeclaration healthDeclaration);
    }
}
