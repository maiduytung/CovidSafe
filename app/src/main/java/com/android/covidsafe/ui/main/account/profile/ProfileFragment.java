package com.android.covidsafe.ui.main.account.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingComponent;
import androidx.lifecycle.ViewModelProvider;

import com.android.covidsafe.R;
import com.android.covidsafe.binding.FragmentDataBindingComponent;
import com.android.covidsafe.databinding.FragmentProfileBinding;
import com.android.covidsafe.utilities.AutoClearedValue;
import com.android.covidsafe.utilities.FieldValidators;
import com.android.covidsafe.viewmodels.ViewModelProviderFactory;
import com.android.covidsafe.vo.Ethnic;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Status;
import com.android.covidsafe.vo.Subnational;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ProfileFragment extends DaggerFragment {
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int ACTION_IMAGE_CAPTURE_REQUEST_CODE = 100;
    private static final int EXTERNAL_CONTENT_URI_REQUEST_CODE = 101;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentProfileBinding> binding;

    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentProfileBinding dataBinding = FragmentProfileBinding.inflate(inflater, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ProfileViewModel.class);
        binding.get().setProfileViewModel(profileViewModel);
        binding.get().setLifecycleOwner(this);

        subscribeUi();
        initClickListener();
        initInputListener();
    }

    private void subscribeUi() {
        profileViewModel.getProfileResource().observe(getViewLifecycleOwner(), profileResource -> {
            if (profileResource.status == Status.SUCCESS && profileResource.data != null) {
                Profile profile = profileResource.data;
                profileViewModel.setProfile(profile);

                setProvince(profile.province);
                setDistrict(profile.district);
                setWard(profile.ward);
                setNationality(profile.nationality);
                setEthnic(profile.ethnic);

                initDatePicker(profile.dateOfBirth);
            }
        });

        profileViewModel.getProfileUpdateResource().observe(getViewLifecycleOwner(), baseResource -> {
            if (baseResource.status == Status.SUCCESS && baseResource.data != null) {
                Toast.makeText(getContext(), R.string.profile_success, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDatePicker(Date date) {
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.date_picker)
                .setSelection(date == null ? MaterialDatePicker.todayInUtcMilliseconds() : date.getTime())
                .build();

        binding.get().itProfileDateOfBirth.setOnClickListener(view -> {
            datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> profileViewModel.setDateOfBirth(datePicker.getHeaderText()));
        });
    }

    private void initInputListener() {
        binding.get().itProfileFullName.addTextChangedListener(new TextFieldValidation(binding.get().itProfileFullName));
        binding.get().itProfileDateOfBirth.addTextChangedListener(new TextFieldValidation(binding.get().ilProfileDateOfBirth));
        binding.get().itProfileIdentification.addTextChangedListener(new TextFieldValidation(binding.get().itProfileIdentification));
        binding.get().atProfileProvince.addTextChangedListener(new TextFieldValidation(binding.get().atProfileProvince));
        binding.get().atProfileDistrict.addTextChangedListener(new TextFieldValidation(binding.get().atProfileDistrict));
        binding.get().atProfileWard.addTextChangedListener(new TextFieldValidation(binding.get().atProfileWard));
        binding.get().itProfilePhoneNumber.addTextChangedListener(new TextFieldValidation(binding.get().itProfilePhoneNumber));
        binding.get().itProfileEmail.addTextChangedListener(new TextFieldValidation(binding.get().itProfileEmail));
    }

    private void initClickListener() {
        binding.get().atProfileProvince.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setProvince(subnational.id);
        });

        binding.get().atProfileDistrict.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setDistrict(subnational.id);
        });

        binding.get().atProfileWard.setOnItemClickListener((adapterView, view, i, l) -> {
            Subnational subnational = (Subnational) adapterView.getAdapter().getItem(i);
            setWard(subnational.id);
        });

        binding.get().atProfileNationality.setOnItemClickListener((adapterView, view, i, l) -> {
            Nationality nationality = (Nationality) adapterView.getAdapter().getItem(i);
            setNationality(nationality.id);
        });

        binding.get().atProfileEthnic.setOnItemClickListener((adapterView, view, i, l) -> {
            Ethnic ethnic = (Ethnic) adapterView.getAdapter().getItem(i);
            setEthnic(ethnic.id);
        });

        binding.get().ivProfileCamera.setOnClickListener(this::showSelectImageDialog);

        binding.get().btnProfileSave.setOnClickListener(this::doUpdate);
    }

    private void setProvince(String id) {
        binding.get().atProfileDistrict.setAdapter(null);
        binding.get().atProfileWard.setAdapter(null);
        binding.get().atProfileDistrict.setText(null, false);
        binding.get().atProfileWard.setText(null, false);

        profileViewModel.setProvince(id != null ? id : "0");
    }

    private void setDistrict(String id) {
        if (id != null) {
            binding.get().atProfileWard.setAdapter(null);
            binding.get().atProfileWard.setText(null, false);

            profileViewModel.setDistrict(id);
        }
    }

    private void setWard(String id) {
        if (id != null) {
            profileViewModel.setWard(id);
        }
    }

    private void setNationality(String id) {
        profileViewModel.setNationality(id != null ? id : "0");
    }

    private void setEthnic(String id) {
        profileViewModel.setEthnic(id != null ? id : "0");
    }

    private void showSelectImageDialog(View view) {

        final CharSequence[] options = {"Máy ảnh", "Thư viện ảnh", "Trở lại"};
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(view.getContext());
        builder.setTitle("Chọn ảnh");
        builder.setItems(options, (dialogInterface, i) -> {
            switch (i) {
                case 0:
                    showCameraPreview();
                    break;
                case 1:
                    selectLibrary();
                    break;
                case 2:
                    dialogInterface.dismiss();
                    break;
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_IMAGE_CAPTURE_REQUEST_CODE && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
            String encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

            profileViewModel.setAvatar(encodedImage);
        }
        if (requestCode == EXTERNAL_CONTENT_URI_REQUEST_CODE && data != null) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(getView(), R.string.camera_permission_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
                startCamera();
            } else {
                // Permission request was denied.
                Snackbar.make(getView(), R.string.camera_permission_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void showCameraPreview() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Snackbar.make(getView(),
                    R.string.camera_permission_available,
                    Snackbar.LENGTH_SHORT).show();
            startCamera();
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        // END_INCLUDE(startCamera)
    }

    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Snackbar.make(getView(), R.string.camera_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CAMERA);
                }
            }).show();

        } else {
            Snackbar.make(getView(), R.string.camera_unavailable, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    private void selectLibrary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXTERNAL_CONTENT_URI_REQUEST_CODE);
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ACTION_IMAGE_CAPTURE_REQUEST_CODE);
    }

    private void doUpdate(View view) {
        if (isValidate()) {
            profileViewModel.updateProfile();
        }
    }

    private Boolean isValidate() {
        return validateFullName() && validateDateOfBirth() && validatePhoneNumber() && validateIdentification()
                && validateProvince() && validateDistrict() && validateWard() && validateEmail();
    }

    private Boolean validateFullName() {
        if (binding.get().itProfileFullName.getText().toString().trim().isEmpty()) {
            binding.get().ilProfileFullName.setError(getResources().getText(R.string.error_required_field));
            binding.get().itProfileFullName.requestFocus();
            return false;
        } else {
            binding.get().ilProfileFullName.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateDateOfBirth() {
        if (binding.get().itProfileDateOfBirth.getText().toString().trim().isEmpty()) {
            binding.get().ilProfileDateOfBirth.setError(getResources().getText(R.string.error_required_field));
            binding.get().itProfileDateOfBirth.requestFocus();
            return false;
        } else {
            binding.get().ilProfileDateOfBirth.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateIdentification() {
        if (binding.get().itProfileIdentification.getText().toString().trim().isEmpty()) {
            binding.get().ilProfileIdentification.setError(getResources().getText(R.string.error_required_field));
            binding.get().itProfileIdentification.requestFocus();
            return false;
        } else {
            binding.get().ilProfileIdentification.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateProvince() {
        if (binding.get().atProfileProvince.getText().toString().trim().isEmpty()) {
            binding.get().ilProfileProvince.setError(getResources().getText(R.string.error_required_field));
            binding.get().atProfileProvince.requestFocus();
            return false;
        } else {
            binding.get().ilProfileProvince.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateDistrict() {
        if (binding.get().atProfileDistrict.getText().toString().trim().isEmpty()) {
            binding.get().ilProfileDistrict.setError(getResources().getText(R.string.error_required_field));
            binding.get().atProfileDistrict.requestFocus();
            return false;
        } else {
            binding.get().ilProfileDistrict.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateWard() {
        if (binding.get().atProfileWard.getText().toString().trim().isEmpty()) {
            binding.get().ilProfileWard.setError(getResources().getText(R.string.error_required_field));
            binding.get().atProfileWard.requestFocus();
            return false;
        } else {
            binding.get().ilProfileWard.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validatePhoneNumber() {
        if (binding.get().itProfilePhoneNumber.getText().toString().trim().isEmpty()) {
            binding.get().ilProfilePhoneNumber.setError(getResources().getText(R.string.error_required_field));
            binding.get().itProfilePhoneNumber.requestFocus();
            return false;
        } else {
            binding.get().ilProfilePhoneNumber.setErrorEnabled(false);
        }
        return true;
    }

    private Boolean validateEmail() {
        if (!binding.get().itProfileEmail.getText().toString().trim().isEmpty() && !FieldValidators.isValidEmail(binding.get().itProfileEmail.getText().toString())) {
            binding.get().ilProfileEmail.setError(getResources().getText(R.string.error_invalid_email));
            binding.get().itProfileEmail.requestFocus();
            return false;
        } else {
            binding.get().ilProfileEmail.setErrorEnabled(false);
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
                case R.id.it_profile_full_name:
                    validateFullName();
                    break;
                case R.id.it_profile_phone_number:
                    validatePhoneNumber();
                    break;
                case R.id.it_profile_identification:
                    validateIdentification();
                    break;
                case R.id.it_profile_email:
                    validateEmail();
                    break;
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }
}
