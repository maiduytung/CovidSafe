package com.android.covidsafe.ui.main.home.vaccineregistration.add;

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
import com.android.covidsafe.databinding.FragmentAddVaccineRegistrationBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Ethnic;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Priority;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Status;
import com.android.covidsafe.vo.Subnational;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AddVaccineRegistrationFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentAddVaccineRegistrationBinding> binding;

    private AddVaccineRegistrationViewModel addVaccineRegistrationViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddVaccineRegistrationBinding dataBinding = FragmentAddVaccineRegistrationBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addVaccineRegistrationViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(AddVaccineRegistrationViewModel.class);
        binding.get().setAddVaccineRegistrationViewModel(addVaccineRegistrationViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initInputListener();
        initClickListener();

    }

    private void subscribeUi() {
        addVaccineRegistrationViewModel.getVaccineRegistrationResource().observe(getViewLifecycleOwner(), vaccineRegistrationResource -> {
            if (vaccineRegistrationResource.status == Status.SUCCESS && vaccineRegistrationResource.data != null) {
                Toast.makeText(getContext(), R.string.success_add_vaccine_registration, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigate(R.id.action_add_vaccine_registration_fragment_to_vaccine_registration_fragment);
            } else if (vaccineRegistrationResource.status == Status.ERROR) {
                Toast.makeText(getContext(), R.string.error_data_update, Toast.LENGTH_SHORT).show();
            }
        });

        addVaccineRegistrationViewModel.getProfileResource().observe(getViewLifecycleOwner(), profileResource -> {
            if (profileResource.data != null) {
                Profile profile = profileResource.data;
                addVaccineRegistrationViewModel.setVaccineRegistration(profile);

                setPriority("0");
                setProvince(profile.province);
                setDistrict(profile.district);
                setWard(profile.ward);
                setNationality(profile.nationality);
                setEthnic(profile.ethnic);

                initBirthdayDatePicker(profileResource.data.dateOfBirth);
                initPreferredVaccinationDatePicker(null);
            }
        });
    }

    private void initBirthdayDatePicker(Date date) {

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker)
                .setSelection(date == null ? MaterialDatePicker.todayInUtcMilliseconds() : date.getTime())
                .build();

        binding.get().itAddVaccineRegistrationDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    addVaccineRegistrationViewModel.setDateOfBirth(datePicker.getHeaderText());
                });
            }
        });
    }

    private void initPreferredVaccinationDatePicker(Date date) {

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker)
                .setSelection(date == null ? MaterialDatePicker.todayInUtcMilliseconds() : date.getTime())
                .build();

        binding.get().itAddVaccineRegistrationPreferredVaccinationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    addVaccineRegistrationViewModel.setPreferredVaccinationDate(datePicker.getHeaderText());
                });
            }
        });
    }

    private void initInputListener() {
        binding.get().itAddVaccineRegistrationFullName.addTextChangedListener(new TextFieldValidation(binding.get().itAddVaccineRegistrationFullName));
        binding.get().itAddVaccineRegistrationIdentification.addTextChangedListener(new TextFieldValidation(binding.get().itAddVaccineRegistrationIdentification));
        binding.get().itAddVaccineRegistrationOccupation.addTextChangedListener(new TextFieldValidation(binding.get().itAddVaccineRegistrationOccupation));
    }

    private void initClickListener() {
        binding.get().atAddVaccineRegistrationPriorityGroup.setOnItemClickListener((adapterView, view, i, l) -> {
            Priority priority = (Priority) adapterView.getAdapter().getItem(i);
            setPriority(priority.id);
        });

        binding.get().atAddVaccineRegistrationProvince.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setProvince(subnational.id);
        });

        binding.get().atAddVaccineRegistrationDistrict.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setDistrict(subnational.id);
        });

        binding.get().atAddVaccineRegistrationWard.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setWard(subnational.id);
        });

        binding.get().atAddVaccineRegistrationNationality.setOnItemClickListener((adapterView, view, i, l) -> {
            Nationality nationality = (Nationality) adapterView.getAdapter().getItem(i);
            setNationality(nationality.id);
        });

        binding.get().atAddVaccineRegistrationEthnic.setOnItemClickListener((adapterView, view, i, l) -> {
            Ethnic ethnic = (Ethnic) adapterView.getAdapter().getItem(i);
            setNationality(ethnic.id);
        });

        binding.get().btnAddVaccineRegistrationSend.setOnClickListener(view -> {
            if (isValidate()) {
                addVaccineRegistrationViewModel.doSend();
            }
        });
    }

    private void setPriority(String id) {
        addVaccineRegistrationViewModel.setPriority(id != null ? id : "0");
    }

    private void setProvince(String id) {
        binding.get().atAddVaccineRegistrationDistrict.setAdapter(null);
        binding.get().atAddVaccineRegistrationWard.setAdapter(null);
        binding.get().atAddVaccineRegistrationDistrict.setText(null, false);
        binding.get().atAddVaccineRegistrationWard.setText(null, false);

        addVaccineRegistrationViewModel.setProvince(id != null ? id : "0");
    }

    private void setDistrict(String id) {
        if (id != null) {
            binding.get().atAddVaccineRegistrationWard.setAdapter(null);
            binding.get().atAddVaccineRegistrationWard.setText(null, false);

            addVaccineRegistrationViewModel.setDistrict(id);
        }
    }

    private void setWard(String id) {
        if (id != null) {
            addVaccineRegistrationViewModel.setWard(id);
        }
    }

    private void setNationality(String id) {
        addVaccineRegistrationViewModel.setNationality(id != null ? id : "0");
    }

    private void setEthnic(String id) {
        addVaccineRegistrationViewModel.setEthnic(id != null ? id : "0");
    }

    private Boolean isValidate() {
        return validateFullName() && validateDateOfBirth() && validateIdentification() && validateOccupation()
                && validatePriorityGroup() && validateProvince() && validateDistrict() && validateWard() && validateAgree();
    }

    private Boolean validateFullName() {
        if (binding.get().itAddVaccineRegistrationFullName.getText().toString().trim().isEmpty()) {
            binding.get().ilAddVaccineRegistrationFullName.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddVaccineRegistrationFullName.requestFocus();
            return false;
        } else {
            binding.get().ilAddVaccineRegistrationFullName.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateDateOfBirth() {
        if (binding.get().itAddVaccineRegistrationDateOfBirth.getText().toString().trim().isEmpty()) {
            binding.get().ilAddVaccineRegistrationDateOfBirth.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddVaccineRegistrationDateOfBirth.requestFocus();
            return false;
        } else {
            binding.get().ilAddVaccineRegistrationDateOfBirth.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateIdentification() {
        if (binding.get().itAddVaccineRegistrationIdentification.getText().toString().trim().isEmpty()) {
            binding.get().ilAddVaccineRegistrationIdentification.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddVaccineRegistrationIdentification.requestFocus();
            return false;
        } else {
            binding.get().ilAddVaccineRegistrationIdentification.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateOccupation() {
        if (binding.get().itAddVaccineRegistrationOccupation.getText().toString().trim().isEmpty()) {
            binding.get().ilAddVaccineRegistrationOccupation.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddVaccineRegistrationOccupation.requestFocus();
            return false;
        } else {
            binding.get().ilAddVaccineRegistrationOccupation.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validatePriorityGroup() {
        if (binding.get().atAddVaccineRegistrationPriorityGroup.getText().toString().trim().isEmpty()) {
            binding.get().ilAddVaccineRegistrationPriorityGroup.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddVaccineRegistrationPriorityGroup.requestFocus();
            return false;
        } else {
            binding.get().ilAddVaccineRegistrationPriorityGroup.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateProvince() {
        if (binding.get().atAddVaccineRegistrationProvince.getText().toString().trim().isEmpty()) {
            binding.get().ilAddVaccineRegistrationProvince.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddVaccineRegistrationProvince.requestFocus();
            return false;
        } else {
            binding.get().ilAddVaccineRegistrationProvince.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateDistrict() {
        if (binding.get().atAddVaccineRegistrationDistrict.getText().toString().trim().isEmpty()) {
            binding.get().ilAddVaccineRegistrationDistrict.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddVaccineRegistrationDistrict.requestFocus();
            return false;
        } else {
            binding.get().ilAddVaccineRegistrationDistrict.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateWard() {
        if (binding.get().atAddVaccineRegistrationWard.getText().toString().trim().isEmpty()) {
            binding.get().ilAddVaccineRegistrationWard.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddVaccineRegistrationWard.requestFocus();
            return false;
        } else {
            binding.get().ilAddVaccineRegistrationWard.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateAgree() {
        if (!binding.get().cbAddVaccineRegistrationAgree.isChecked()) {
            Toast.makeText(getContext(), R.string.error_agree, Toast.LENGTH_SHORT).show();
            return false;
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
                case R.id.it_add_vaccine_registration_full_name:
                    validateFullName();
                    break;
                case R.id.it_add_vaccine_registration_identification:
                    validateIdentification();
                    break;
                case R.id.it_add_vaccine_registration_occupation:
                    validateOccupation();
                    break;
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }
}
