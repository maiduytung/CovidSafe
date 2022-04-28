package com.android.covidsafe.ui.main.home.healthdeclaration.add;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentAddHealthDeclarationBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.FieldValidators;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Status;
import com.android.covidsafe.vo.Subnational;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AddHealthDeclarationFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentAddHealthDeclarationBinding> binding;

    private AddHealthDeclarationViewModel addHealthDeclarationViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAddHealthDeclarationBinding dataBinding = FragmentAddHealthDeclarationBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addHealthDeclarationViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(AddHealthDeclarationViewModel.class);
        binding.get().setAddHealthDeclarationViewModel(addHealthDeclarationViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initInputListener();
        initClickListener();
    }

    private void subscribeUi() {
        addHealthDeclarationViewModel.getHealthDeclarationResource().observe(getViewLifecycleOwner(), healthDeclarationResource -> {
            if (healthDeclarationResource.status == Status.SUCCESS && healthDeclarationResource.data != null) {
                Toast.makeText(getContext(), R.string.success_data_update_complete, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigate(R.id.action_add_health_declaration_fragment_to_health_declaration_fragment);
            } else if (healthDeclarationResource.status == Status.ERROR) {
                Toast.makeText(getContext(), R.string.error_data_update, Toast.LENGTH_SHORT).show();
            }
        });

        addHealthDeclarationViewModel.getProfileResource().observe(getViewLifecycleOwner(), profileResource -> {
            if (profileResource.data != null) {
                Profile profile = profileResource.data;
                addHealthDeclarationViewModel.setHealthDeclaration(profile);

                setProvince(profile.province);
                setDistrict(profile.district);
                setWard(profile.ward);
                setNationality(profile.nationality);
            }
        });
    }

    private void initInputListener() {
        binding.get().itAddHealthDeclarationFullName.addTextChangedListener(new TextFieldValidation(binding.get().itAddHealthDeclarationFullName));
        binding.get().itAddHealthDeclarationYearOfBirth.addTextChangedListener(new TextFieldValidation(binding.get().itAddHealthDeclarationYearOfBirth));
        binding.get().itAddHealthDeclarationIdentification.addTextChangedListener(new TextFieldValidation(binding.get().itAddHealthDeclarationIdentification));
        binding.get().atAddHealthDeclarationNationality.addTextChangedListener(new TextFieldValidation(binding.get().atAddHealthDeclarationNationality));
        binding.get().atAddHealthDeclarationProvince.addTextChangedListener(new TextFieldValidation(binding.get().atAddHealthDeclarationProvince));
        binding.get().atAddHealthDeclarationDistrict.addTextChangedListener(new TextFieldValidation(binding.get().atAddHealthDeclarationDistrict));
        binding.get().atAddHealthDeclarationWard.addTextChangedListener(new TextFieldValidation(binding.get().atAddHealthDeclarationWard));
        binding.get().itAddHealthDeclarationAddress.addTextChangedListener(new TextFieldValidation(binding.get().itAddHealthDeclarationAddress));
        binding.get().itAddHealthDeclarationPhoneNumber.addTextChangedListener(new TextFieldValidation(binding.get().itAddHealthDeclarationPhoneNumber));
        binding.get().itAddHealthDeclarationEmail.addTextChangedListener(new TextFieldValidation(binding.get().itAddHealthDeclarationEmail));
        binding.get().itAddHealthDeclarationVisitDetails.addTextChangedListener(new TextFieldValidation(binding.get().itAddHealthDeclarationVisitDetails));
        binding.get().itAddHealthDeclarationSymptomsDetails.addTextChangedListener(new TextFieldValidation(binding.get().itAddHealthDeclarationSymptomsDetails));
    }

    private void initClickListener() {
        binding.get().atAddHealthDeclarationNationality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Nationality nationality = (Nationality) adapterView.getAdapter().getItem(i);
                setNationality(nationality.id);
            }
        });

        binding.get().atAddHealthDeclarationProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
                setProvince(subnational.id);
            }
        });

        binding.get().atAddHealthDeclarationDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
                setDistrict(subnational.id);
            }
        });

        binding.get().atAddHealthDeclarationWard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
                setWard(subnational.id);
            }
        });

        binding.get().btnAddHealthDeclarationSend.setOnClickListener(this::doSend);
    }

    private void setNationality(String id) {
        addHealthDeclarationViewModel.setNationality(id != null ? id : "0");
    }

    private void setProvince(String id) {
        binding.get().atAddHealthDeclarationDistrict.setAdapter(null);
        binding.get().atAddHealthDeclarationWard.setAdapter(null);
        binding.get().atAddHealthDeclarationDistrict.setText(null, false);
        binding.get().atAddHealthDeclarationWard.setText(null, false);

        addHealthDeclarationViewModel.setProvince(id != null ? id : "0");
    }

    private void setDistrict(String id) {
        if (id != null) {
            binding.get().atAddHealthDeclarationWard.setAdapter(null);
            binding.get().atAddHealthDeclarationWard.setText(null, false);

            addHealthDeclarationViewModel.setDistrict(id);
        }
    }

    private void setWard(String id) {
        if (id != null) {
            addHealthDeclarationViewModel.setWard(id);
        }
    }

    private void doSend(View view) {
        if (isValidate()) {
            addHealthDeclarationViewModel.doSend();
        }
    }

    private Boolean isValidate() {
        return validateFullName() && validateYearOfBirth() && validateIdentification() && validateNationality()
                && validateProvince() && validateDistrict() && validateWard() && validateAddress() && validatePhoneNumber()
                && validateEmail();
    }

    private Boolean validateFullName() {
        if (binding.get().itAddHealthDeclarationFullName.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationFullName.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddHealthDeclarationFullName.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationFullName.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateYearOfBirth() {
        if (binding.get().itAddHealthDeclarationYearOfBirth.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationYearOfBirth.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddHealthDeclarationYearOfBirth.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationYearOfBirth.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateIdentification() {
        if (binding.get().itAddHealthDeclarationIdentification.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationIdentification.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddHealthDeclarationIdentification.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationIdentification.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateNationality() {
        if (binding.get().atAddHealthDeclarationNationality.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationNationality.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddHealthDeclarationNationality.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationNationality.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateProvince() {
        if (binding.get().atAddHealthDeclarationProvince.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationProvince.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddHealthDeclarationProvince.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationProvince.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateDistrict() {
        if (binding.get().atAddHealthDeclarationDistrict.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationDistrict.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddHealthDeclarationDistrict.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationDistrict.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateWard() {
        if (binding.get().atAddHealthDeclarationWard.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationWard.setError(getResources().getText(R.string.error_required_field));
            binding.get().atAddHealthDeclarationWard.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationWard.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateAddress() {
        if (binding.get().itAddHealthDeclarationAddress.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationAddress.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddHealthDeclarationAddress.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationAddress.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validatePhoneNumber() {
        if (binding.get().itAddHealthDeclarationPhoneNumber.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationPhoneNumber.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddHealthDeclarationPhoneNumber.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationPhoneNumber.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateEmail() {
        if (!binding.get().itAddHealthDeclarationEmail.getText().toString().trim().isEmpty()
                && !FieldValidators.isValidEmail(binding.get().itAddHealthDeclarationEmail.getText().toString())) {
            binding.get().ilAddHealthDeclarationEmail.setError(getResources().getText(R.string.error_invalid_email));
            binding.get().itAddHealthDeclarationEmail.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationEmail.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateVisitDetails() {
        if (binding.get().itAddHealthDeclarationVisitDetails.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationVisitDetails.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddHealthDeclarationVisitDetails.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationVisitDetails.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateSymptomsDetails() {
        if (binding.get().itAddHealthDeclarationSymptomsDetails.getText().toString().trim().isEmpty()) {
            binding.get().ilAddHealthDeclarationSymptomsDetails.setError(getResources().getText(R.string.error_required_field));
            binding.get().itAddHealthDeclarationSymptomsDetails.requestFocus();
            return false;
        } else {
            binding.get().ilAddHealthDeclarationSymptomsDetails.setErrorEnabled(false);
        }
        return true;
    }

    private class TextFieldValidation implements TextWatcher {
        private View view;

        private TextFieldValidation(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @SuppressLint("NonConstantResourceId")
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.it_add_health_declaration_full_name:
                    validateFullName();
                    break;
                case R.id.it_add_health_declaration_year_of_birth:
                    validateYearOfBirth();
                    break;
                case R.id.it_add_health_declaration_identification:
                    validateIdentification();
                    break;
//                case R.id.at_add_health_declarationnationality:
//                    validateNationality();
//                    break;
//                case R.id.at_add_health_declarationprovince:
//                    validateProvince();
//                    break;
//                case R.id.at_add_health_declarationdistrict:
//                    validateDistrict();
//                    break;
//                case R.id.at_add_health_declarationward:
//                    validateWard();
//                    break;
                case R.id.it_add_health_declaration_address:
                    validateAddress();
                    break;
                case R.id.it_add_health_declaration_phone_number:
                    validatePhoneNumber();
                    break;
                case R.id.it_add_health_declaration_email:
                    validateEmail();
                    break;
                case R.id.it_add_health_declaration_visit_details:
                    validateVisitDetails();
                    break;
                case R.id.it_add_health_declaration_symptoms_details:
                    validateSymptomsDetails();
                    break;
            }
        }

        @SuppressLint("NonConstantResourceId")
        public void afterTextChanged(Editable editable) {
        }
    }

}
