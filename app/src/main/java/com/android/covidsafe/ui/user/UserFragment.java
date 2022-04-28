//package com.android.covidsafe.ui.user;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.VisibleForTesting;
//import androidx.databinding.DataBindingComponent;
//import androidx.databinding.DataBindingUtil;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.android.covidsafe.R;
//import com.android.covidsafe.binding.FragmentDataBindingComponent;
//import com.android.covidsafe.databinding.UserFragmentBinding;
//import com.android.covidsafe.ui.common.NavigationController;
//import com.android.covidsafe.ui.common.RepoListAdapter;
//import com.android.covidsafe.util.AutoClearedValue;
//
//import javax.inject.Inject;
//
//import dagger.android.support.DaggerFragment;
//
//public class UserFragment extends DaggerFragment {
//    private static final String LOGIN_KEY = "login";
//    @Inject
//    ViewModelProvider.Factory viewModelFactory;
//    @Inject
//    NavigationController navigationController;
//
//    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
//    private UserViewModel userViewModel;
//    @VisibleForTesting
//    AutoClearedValue<UserFragmentBinding> binding;
//    private AutoClearedValue<RepoListAdapter> adapter;
//
//    public static UserFragment create(String login) {
//        UserFragment userFragment = new UserFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(LOGIN_KEY, login);
//        userFragment.setArguments(bundle);
//        return userFragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//            @Nullable Bundle savedInstanceState) {
//        UserFragmentBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.user_fragment,
//                container, false, dataBindingComponent);
//        dataBinding.setRetryCallback(() -> userViewModel.retry());
//        binding = new AutoClearedValue<>(this, dataBinding);
//        return dataBinding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
//        userViewModel.setLogin(getArguments().getString(LOGIN_KEY));
//        userViewModel.getUser().observe(this, userResource -> {
//            binding.get().setUser(userResource == null ? null : userResource.data);
//            binding.get().setUserResource(userResource);
//            // this is only necessary because espresso cannot read data binding callbacks.
//            binding.get().executePendingBindings();
//        });
//        RepoListAdapter rvAdapter = new RepoListAdapter(dataBindingComponent, false,
//                repo -> navigationController.navigateToRepo(repo.owner.login, repo.name));
//        binding.get().repoList.setAdapter(rvAdapter);
//        this.adapter = new AutoClearedValue<>(this, rvAdapter);
//        initRepoList();
//    }
//
//    private void initRepoList() {
//        userViewModel.getRepositories().observe(this, repos -> {
//            // no null checks for adapter.get() since LiveData guarantees that we'll not receive
//            // the event if fragment is now show.
//            if (repos == null) {
//                adapter.get().replace(null);
//            } else {
//                adapter.get().replace(repos.data);
//            }
//        });
//    }
//}
