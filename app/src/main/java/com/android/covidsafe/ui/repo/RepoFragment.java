//package com.android.covidsafe.ui.repo;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.databinding.DataBindingComponent;
//import androidx.databinding.DataBindingUtil;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.android.covidsafe.R;
//import com.android.covidsafe.binding.FragmentDataBindingComponent;
//import com.android.covidsafe.databinding.RepoFragmentBinding;
//import com.android.covidsafe.ui.common.NavigationController;
//import com.android.covidsafe.util.AutoClearedValue;
//import com.android.covidsafe.vo.Repo;
//import com.android.covidsafe.vo.Resource;
//
//import java.util.Collections;
//
//import javax.inject.Inject;
//
//import dagger.android.support.DaggerFragment;
//
///**
// * The UI Controller for displaying a Github Repo's information with its contributors.
// */
//public class RepoFragment extends DaggerFragment {
//
//    private static final String REPO_OWNER_KEY = "repo_owner";
//
//    private static final String REPO_NAME_KEY = "repo_name";
//
//    @Inject
//    ViewModelProvider.Factory viewModelFactory;
//
//    private RepoViewModel repoViewModel;
//
//    @Inject
//    NavigationController navigationController;
//
//    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
//    AutoClearedValue<RepoFragmentBinding> binding;
//    AutoClearedValue<ContributorAdapter> adapter;
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        repoViewModel = new ViewModelProvider(this, viewModelFactory).get(RepoViewModel.class);
//        Bundle args = getArguments();
//        if (args != null && args.containsKey(REPO_OWNER_KEY) &&
//                args.containsKey(REPO_NAME_KEY)) {
//            repoViewModel.setId(args.getString(REPO_OWNER_KEY),
//                    args.getString(REPO_NAME_KEY));
//        } else {
//            repoViewModel.setId(null, null);
//        }
//        LiveData<Resource<Repo>> repo = repoViewModel.getRepo();
//        repo.observe(this, resource -> {
//            binding.get().setRepo(resource == null ? null : resource.data);
//            binding.get().setRepoResource(resource);
//            binding.get().executePendingBindings();
//        });
//
//        ContributorAdapter adapter = new ContributorAdapter(dataBindingComponent,
//                contributor -> navigationController.navigateToUser(contributor.getLogin()));
//        this.adapter = new AutoClearedValue<>(this, adapter);
//        binding.get().contributorList.setAdapter(adapter);
//        initContributorList(repoViewModel);
//    }
//
//    private void initContributorList(RepoViewModel viewModel) {
//        viewModel.getContributors().observe(this, listResource -> {
//            // we don't need any null checks here for the adapter since LiveData guarantees that
//            // it won't call us if fragment is stopped or not started.
//            if (listResource != null && listResource.data != null) {
//                adapter.get().replace(listResource.data);
//            } else {
//                //noinspection ConstantConditions
//                adapter.get().replace(Collections.emptyList());
//            }
//        });
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//            @Nullable Bundle savedInstanceState) {
//        RepoFragmentBinding dataBinding = DataBindingUtil
//                .inflate(inflater, R.layout.repo_fragment, container, false);
//        dataBinding.setRetryCallback(() -> repoViewModel.retry());
//        binding = new AutoClearedValue<>(this, dataBinding);
//        return dataBinding.getRoot();
//    }
//
//    public static RepoFragment create(String owner, String name) {
//        RepoFragment repoFragment = new RepoFragment();
//        Bundle args = new Bundle();
//        args.putString(REPO_OWNER_KEY, owner);
//        args.putString(REPO_NAME_KEY, name);
//        repoFragment.setArguments(args);
//        return repoFragment;
//    }
//}
