package com.android.covidsafe.db;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.android.covidsafe.repository.SecureSharedPref;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.vo.Certification;
import com.android.covidsafe.vo.HealthDeclaration;
import com.android.covidsafe.vo.HealthDeclarationResult;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Report;
import com.android.covidsafe.vo.ReportResult;
import com.android.covidsafe.vo.User;
import com.android.covidsafe.vo.Vaccination;
import com.android.covidsafe.vo.VaccinationResult;
import com.android.covidsafe.vo.VaccineRegistration;
import com.android.covidsafe.vo.VaccineRegistrationResult;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.UUID;

/**
 * Main database description.
 */
@Database(entities = {User.class, Profile.class, Certification.class, HealthDeclarationResult.class, HealthDeclaration.class,
        VaccineRegistrationResult.class, VaccineRegistration.class, ReportResult.class, Report.class, VaccinationResult.class, Vaccination.class,}, version = 3)
@TypeConverters({Converters.class})
public abstract class SecureDatabase extends RoomDatabase {

    @VisibleForTesting
    public static final String SECURE_DATABASE_NAME = "secure_covidsafe";

    private static volatile SecureDatabase covidsafeDBSecure = null;

    public static SecureDatabase getInstance(Context context, SecureSharedPref secureSharedPref) {
        if (covidsafeDBSecure == null) {
            synchronized (SecureDatabase.class) {
                if (covidsafeDBSecure == null) {
                    covidsafeDBSecure = buildDatabase(context, secureSharedPref);
                }
            }
        }
        return covidsafeDBSecure;
    }

    private static SecureDatabase buildDatabase(Context context, SecureSharedPref secureSharedPref) {
        if (secureSharedPref.getString(Constants.SECURE_DATABASE_KEY) == null) {
            secureSharedPref.putString(Constants.SECURE_DATABASE_KEY, UUID.randomUUID().toString());
        }
        final byte[] passphrase = SQLiteDatabase.getBytes(secureSharedPref.getString(Constants.SECURE_DATABASE_KEY).toCharArray());
        final SupportFactory factory = new SupportFactory(passphrase);
        return Room.databaseBuilder(context, SecureDatabase.class, SECURE_DATABASE_NAME)
                .openHelperFactory(factory)
                .build();
    }

    abstract public UserDao userDao();

    abstract public ProfileDao profileDao();

    abstract public CertificationDao certificationDao();

    abstract public HealthDeclarationDao healthDeclarationDao();

    abstract public VaccineRegistrationDao vaccineRegistrationDao();

    abstract public ReportDao reportDao();

    abstract public VaccinationDao vaccinationDao();
}
