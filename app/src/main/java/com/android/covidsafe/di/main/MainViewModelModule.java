package com.android.covidsafe.di.main;

import androidx.lifecycle.ViewModel;

import com.android.covidsafe.di.ViewModelKey;
import com.android.covidsafe.ui.main.account.AccountViewModel;
import com.android.covidsafe.ui.main.account.profile.ProfileViewModel;
import com.android.covidsafe.ui.main.home.HomeViewModel;
import com.android.covidsafe.ui.main.home.healthdeclaration.HealthDeclarationViewModel;
import com.android.covidsafe.ui.main.home.healthdeclaration.add.AddHealthDeclarationViewModel;
import com.android.covidsafe.ui.main.home.healthdeclaration.detail.HealthDeclarationDetailViewModel;
import com.android.covidsafe.ui.main.home.report.ReportViewModel;
import com.android.covidsafe.ui.main.home.report.add.AddReportViewModel;
import com.android.covidsafe.ui.main.home.report.detail.ReportDetailViewModel;
import com.android.covidsafe.ui.main.home.vaccination.VaccinationViewModel;
import com.android.covidsafe.ui.main.home.vaccination.detail.VaccinationDetailViewModel;
import com.android.covidsafe.ui.main.home.vaccineregistration.VaccineRegistrationViewModel;
import com.android.covidsafe.ui.main.home.vaccineregistration.add.AddVaccineRegistrationViewModel;
import com.android.covidsafe.ui.main.home.vaccineregistration.detail.VaccineRegistrationDetailViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel.class)
    abstract ViewModel bindAccountViewModel(AccountViewModel accountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel bindProfileViewModel(ProfileViewModel profileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HealthDeclarationViewModel.class)
    abstract ViewModel bindHealthDeclarationViewModel(HealthDeclarationViewModel healthDeclarationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddHealthDeclarationViewModel.class)
    abstract ViewModel bindAddHealthDeclarationViewModel(AddHealthDeclarationViewModel addHealthDeclarationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HealthDeclarationDetailViewModel.class)
    abstract ViewModel bindHealthDeclarationDetailViewModel(HealthDeclarationDetailViewModel healthDeclarationDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VaccineRegistrationViewModel.class)
    abstract ViewModel bindVaccineRegistrationViewModel(VaccineRegistrationViewModel vaccineRegistrationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddVaccineRegistrationViewModel.class)
    abstract ViewModel bindAddVaccineRegistrationViewModel(AddVaccineRegistrationViewModel addVaccineRegistrationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VaccineRegistrationDetailViewModel.class)
    abstract ViewModel VaccineRegistrationDetailViewModel(VaccineRegistrationDetailViewModel vaccineRegistrationDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel.class)
    abstract ViewModel bindReportViewModel(ReportViewModel reportViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddReportViewModel.class)
    abstract ViewModel bindAddReportViewModel(AddReportViewModel addreportViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReportDetailViewModel.class)
    abstract ViewModel bindReportDetailViewModel(ReportDetailViewModel reportDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VaccinationViewModel.class)
    abstract ViewModel bindVaccinationViewModel(VaccinationViewModel vaccinationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(VaccinationDetailViewModel.class)
    abstract ViewModel bindVaccinationDetailViewModel(VaccinationDetailViewModel vaccinationDetailViewModel);
}
