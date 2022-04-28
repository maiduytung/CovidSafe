//package com.android.covidsafe.ui.user;
//
//import androidx.annotation.VisibleForTesting;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Transformations;
//import androidx.lifecycle.ViewModel;
//
//import com.android.covidsafe.repository.RepoRepository;
//import com.android.covidsafe.repository.UserRepository;
//import com.android.covidsafe.util.AbsentLiveData;
//import com.android.covidsafe.vo.Repo;
//import com.android.covidsafe.vo.Resource;
//import com.android.covidsafe.vo.User;
//
//import java.util.List;
//import java.util.Objects;
//
//import javax.inject.Inject;
//
//public class UserViewModel extends ViewModel {
//    @VisibleForTesting
//    final MutableLiveData<String> login = new MutableLiveData<>();
//    private final LiveData<Resource<List<Repo>>> repositories;
//    private final LiveData<Resource<User>> user;
//    @SuppressWarnings("unchecked")
//    @Inject
//    public UserViewModel(UserRepository userRepository, RepoRepository repoRepository) {
//        user = Transformations.switchMap(login, login -> {
//            if (login == null) {
//                return AbsentLiveData.create();
//            } else {
//                return userRepository.loadUser(login);
//            }
//        });
//        repositories = Transformations.switchMap(login, login -> {
//            if (login == null) {
//                return AbsentLiveData.create();
//            } else {
//                return repoRepository.loadRepos(login);
//            }
//        });
//    }
//
//    @VisibleForTesting
//    public void setLogin(String login) {
//        if (Objects.equals(this.login.getValue(), login)) {
//            return;
//        }
//        this.login.setValue(login);
//    }
//
//    @VisibleForTesting
//    public LiveData<Resource<User>> getUser() {
//        return user;
//    }
//
//    @VisibleForTesting
//    public LiveData<Resource<List<Repo>>> getRepositories() {
//        return repositories;
//    }
//
//    @VisibleForTesting
//    public void retry() {
//        if (this.login.getValue() != null) {
//            this.login.setValue(this.login.getValue());
//        }
//    }
//}
