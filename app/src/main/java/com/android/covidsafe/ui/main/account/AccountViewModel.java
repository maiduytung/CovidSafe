package com.android.covidsafe.ui.main.account;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.UserRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.User;

import javax.inject.Inject;

public class AccountViewModel extends ViewModel {

    private final MutableLiveData<String> id = new MutableLiveData<>();

    private final LiveData<Resource<User>> results;

    @Inject
    public AccountViewModel(UserRepository userRepository) {
        results = Transformations.switchMap(id, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.loadUser();
            }
        });
    }

    @VisibleForTesting
    public LiveData<Resource<User>> getResults() {
        return results;
    }

    public void setQuery() {
        id.setValue("me");
    }
}