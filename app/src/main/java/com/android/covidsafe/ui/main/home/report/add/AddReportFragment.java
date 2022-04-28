package com.android.covidsafe.ui.main.home.report.add;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentAddReportBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Status;
import com.android.covidsafe.vo.Subnational;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AddReportFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentAddReportBinding> binding;

    private AddReportViewModel addReportViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddReportBinding dataBinding = FragmentAddReportBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addReportViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(AddReportViewModel.class);
        binding.get().setAddReportViewModel(addReportViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initInputListener();
        initClickListener();
        setProvince("0");
        binding.get().itAddReportContent.requestFocus();
    }

    private void subscribeUi() {
        addReportViewModel.getReportResource().observe(getViewLifecycleOwner(), reportResource -> {
            if (reportResource.status == Status.SUCCESS && reportResource.data != null) {
                Toast.makeText(getContext(), R.string.success_data_update_complete, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigate(R.id.action_add_report_fragment_to_report_fragment);
            } else if (reportResource.status == Status.ERROR) {
                Toast.makeText(getContext(), R.string.error_data_update, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initInputListener() {
    }

    private void initClickListener() {

        binding.get().atAddReportProvince.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setProvince(subnational.id);
        });

        binding.get().atAddReportDistrict.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setDistrict(subnational.id);
        });

        binding.get().atAddReportWard.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setWard(subnational.id);
        });

        binding.get().btnAddReportSend.setOnClickListener(view -> {
            if (isValidate()) {
                addReportViewModel.doSend();
            }
        });
    }

    private void setProvince(String id) {
        binding.get().atAddReportDistrict.setAdapter(null);
        binding.get().atAddReportWard.setAdapter(null);
        binding.get().atAddReportDistrict.setText(null, false);
        binding.get().atAddReportWard.setText(null, false);

        addReportViewModel.setProvince(id != null ? id : "0");
    }

    private void setDistrict(String id) {
        if (id != null) {
            binding.get().atAddReportWard.setAdapter(null);
            binding.get().atAddReportWard.setText(null, false);

            addReportViewModel.setDistrict(id);
        }
    }

    private void setWard(String id) {
        if (id != null) {
            addReportViewModel.setWard(id);
        }
    }


    private Boolean isValidate() {
        return validateContent() && validateProvince() && validateDistrict() && validateWard();
    }

    private Boolean validateContent() {
        if (binding.get().itAddReportContent.getText().toString().trim().isEmpty()) {
            binding.get().ilAddReportContent.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddReportContent.requestFocus();
            return false;
        } else {
            binding.get().ilAddReportContent.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateProvince() {
        if (binding.get().atAddReportProvince.getText().toString().trim().isEmpty()) {
            binding.get().ilAddReportProvince.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddReportProvince.requestFocus();
            return false;
        } else {
            binding.get().ilAddReportProvince.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateDistrict() {
        if (binding.get().atAddReportDistrict.getText().toString().trim().isEmpty()) {
            binding.get().ilAddReportDistrict.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddReportDistrict.requestFocus();
            return false;
        } else {
            binding.get().ilAddReportDistrict.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateWard() {
        if (binding.get().atAddReportWard.getText().toString().trim().isEmpty()) {
            binding.get().ilAddReportWard.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddReportWard.requestFocus();
            return false;
        } else {
            binding.get().ilAddReportWard.setErrorEnabled(false);
        }
        return true;
    }

    private class TextFieldValidation implements TextWatcher {
        private final View view;

        private TextFieldValidation(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @SuppressLint("NonConstantResourceId")
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.it_add_report_content:
                    validateContent();
                    break;
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }
}
