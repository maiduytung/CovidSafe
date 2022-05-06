package com.android.covidsafe.di.main;

import com.android.covidsafe.ui.main.account.AccountFragment;
import com.android.covidsafe.ui.main.account.profile.ProfileFragment;
import com.android.covidsafe.ui.main.account.setting.SettingFragment;
import com.android.covidsafe.ui.main.home.HomeFragment;
import com.android.covidsafe.ui.main.home.healthdeclaration.HealthDeclarationFragment;
import com.android.covidsafe.ui.main.home.healthdeclaration.add.AddHealthDeclarationFragment;
import com.android.covidsafe.ui.main.home.healthdeclaration.detail.HealthDeclarationDetailFragment;
import com.android.covidsafe.ui.main.home.report.ReportFragment;
import com.android.covidsafe.ui.main.home.report.add.AddReportFragment;
import com.android.covidsafe.ui.main.home.report.detail.ReportDetailFragment;
import com.android.covidsafe.ui.main.home.vaccination.VaccinationFragment;
import com.android.covidsafe.ui.main.home.vaccination.detail.VaccinationDetailFragment;
import com.android.covidsafe.ui.main.home.vaccineregistration.VaccineRegistrationFragment;
import com.android.covidsafe.ui.main.home.vaccineregistration.add.AddVaccineRegistrationFragment;
import com.android.covidsafe.ui.main.home.vaccineregistration.detail.VaccineRegistrationDetailFragment;

import java.util.Set;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract AccountFragment contributeAccountFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();


    @ContributesAndroidInjector
    abstract HealthDeclarationFragment contributeHealthDeclarationFragment();

    @ContributesAndroidInjector
    abstract HealthDeclarationDetailFragment contributeHealthDeclarationDetailFragment();

    @ContributesAndroidInjector
    abstract AddHealthDeclarationFragment contributeAddHealthDeclarationFragment();

    @ContributesAndroidInjector
    abstract VaccineRegistrationFragment contributeVaccineRegistrationFragment();

    @ContributesAndroidInjector
    abstract AddVaccineRegistrationFragment contributeAddVaccineRegistrationFragment();

    @ContributesAndroidInjector
    abstract VaccineRegistrationDetailFragment contributeVaccineRegistrationDetailFragment();

    @ContributesAndroidInjector
    abstract ReportFragment contributeReportFragment();

    @ContributesAndroidInjector
    abstract AddReportFragment contributeAddReportFragment();

    @ContributesAndroidInjector
    abstract ReportDetailFragment contributeReportDetailFragment();

    @ContributesAndroidInjector
    abstract VaccinationFragment contributeVaccinationFragment();

    @ContributesAndroidInjector
    abstract VaccinationDetailFragment contributeVaccinationDetailFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributeSettingFragment();
}
