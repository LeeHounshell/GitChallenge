package com.harlie.leehounshell.gitchallenge.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.databinding.ActivityBrowseUsersRepositorysBinding;
import com.harlie.leehounshell.gitchallenge.model.GitHubUser_Model;
import com.harlie.leehounshell.gitchallenge.model.Repository_Model;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.util.PagerAdapter;
import com.harlie.leehounshell.gitchallenge.util.TheWinner;
import com.harlie.leehounshell.gitchallenge.view_model.GitHubUserPair_ViewModel;

import org.greenrobot.eventbus.EventBus;

public class BrowseUsersRepositorysActivity extends BaseActivity {
    private final static String TAG = "LEE: <" + BrowseUsersRepositorysActivity.class.getSimpleName() + ">";

    final static String KEY_USER_ONE = "user_one";
    final static String KEY_USER_TWO = "user_two";

    private ActivityBrowseUsersRepositorysBinding mBinding;
    private GitHubUserPair_ViewModel mGitHubUserPair_ViewModel;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TheWinner mWinner;
    private UserOneTabFragment mTabFragment1;
    private UserTwoTabFragment mTabFragment2;

    public static class GitHubUserResultsEvent {
        private final static String TAG = "LEE: <" + GitHubUserResultsEvent.class.getSimpleName() + ">";

        @SuppressWarnings("CanBeFinal")
        private GitHubUser_Model mUserModel;
        private int mUserNumber;

        GitHubUserResultsEvent(GitHubUser_Model userModel, int userNumber) {
            LogHelper.v(TAG, "GitHubUsersResultsEvent");
            this.mUserModel = userModel;
            this.mUserNumber = userNumber;
        }

        public GitHubUser_Model getUserModel() {
            return mUserModel;
        }

        public int getUserNumber() {
            return mUserNumber;
        }

        @Override
        public String toString() {
            return "GitHubUserResultsEvent{" +
                    "mUserModel=" + mUserModel +
                    '}';
        }
    }

    public static class GitHubUserOneResultsEvent extends GitHubUserResultsEvent {
        public GitHubUserOneResultsEvent(GitHubUser_Model userModel, int userNumber) {
            super(userModel, userNumber);
        }
    }

    public static class GitHubUserTwoResultsEvent extends GitHubUserResultsEvent {
        public GitHubUserTwoResultsEvent(GitHubUser_Model userModel, int userNumber) {
            super(userModel, userNumber);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mGitHubUserPair_ViewModel = ViewModelProviders.of(this).get(GitHubUserPair_ViewModel.class);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_browse_users_repositorys);

        if (getIntent().getExtras() != null) {
            GitHubUser_Model userOne = getIntent().getParcelableExtra(KEY_USER_ONE);
            mGitHubUserPair_ViewModel.setGitHubUserOne(userOne);
            mBinding.setGitHubUserOne(mGitHubUserPair_ViewModel.getGitHubUserOne());

            GitHubUser_Model userTwo = getIntent().getParcelableExtra(KEY_USER_TWO);
            mGitHubUserPair_ViewModel.setGitHubUserTwo(userTwo);
            mBinding.setGitHubUserTwo(mGitHubUserPair_ViewModel.getGitHubUserTwo());

            LogHelper.v(TAG, "userOne=" + userOne);
            LogHelper.v(TAG, "userTwo=" + userOne);

            mViewPager = findViewById(R.id.pager);
            setupViewPager(mViewPager);
            setupTabLayout();
            setTheWinner();
        }
        else {
            LogHelper.e(TAG, "something went wrong with the passed browse parameters");
            goToMainActivity();
        }
    }

    private void setupViewPager(ViewPager mViewPager) {
        LogHelper.v(TAG, "setupViewPager");
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 2);
        mTabFragment1 = new UserOneTabFragment();
        mTabFragment2 = new UserTwoTabFragment();
        mTabFragment1.setUserNumber(1);
        mTabFragment2.setUserNumber(2);
        adapter.addFragment(mTabFragment1, "USER ONE");
        adapter.addFragment(mTabFragment2, "USER TWO");
        mViewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        LogHelper.v(TAG, "setupTabLayout");
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.user_one)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.user_two)));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogHelper.v(TAG, "onTabSelected: position=" + tab.getPosition());
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogHelper.v(TAG, "onTabUnselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LogHelper.v(TAG, "onTabReselected");
            }
        });
    }

    @Override
    protected void onResume() {
        LogHelper.v(TAG, "onResume");
        super.onResume();
        // post in onResume so the UserTabFragment has time to setup
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogHelper.v(TAG, "*** ---------> post userModels to the UserTabFragments <--------- ***");
                post(mGitHubUserPair_ViewModel.getGitHubUserOne(), 1); // the userNumber indicates which UserTabFragment owns this event
                post(mGitHubUserPair_ViewModel.getGitHubUserTwo(), 2);
            }
        }, 1000);
    }

    private void setTheWinner() {
        mWinner = new TheWinner();
        mBinding.setWinner(mWinner);
        int numberStarsTotalOne = countTotalNumberStars(mGitHubUserPair_ViewModel.getGitHubUserOne());
        int numberStarsTotalTwo = countTotalNumberStars(mGitHubUserPair_ViewModel.getGitHubUserTwo());
        if (numberStarsTotalOne > numberStarsTotalTwo) {
            mWinner.setWinner(mGitHubUserPair_ViewModel.getGitHubUserOne(), numberStarsTotalOne);
        }
        else {
            mWinner.setWinner(mGitHubUserPair_ViewModel.getGitHubUserTwo(), numberStarsTotalTwo);
        }
    }

    private int countTotalNumberStars(GitHubUser_Model user) {
        int count = 0;
        for (Repository_Model repositoryModel : user.getUserRepositoryList()) {
            count += repositoryModel.getRepoStars();
        }
        return count;
    }

    private static void post(GitHubUser_Model userModel, int userNumber) {
        LogHelper.v(TAG, "post");
        switch (userNumber) {
            case 1: {
                GitHubUserOneResultsEvent userResultsEvent = new GitHubUserOneResultsEvent(userModel, userNumber);
                EventBus.getDefault().post(userResultsEvent);
                break;
            }
            case 2: {
                GitHubUserTwoResultsEvent userResultsEvent = new GitHubUserTwoResultsEvent(userModel, userNumber);
                EventBus.getDefault().post(userResultsEvent);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        LogHelper.v(TAG, "onBackPressed");
        goToMainActivity();
    }

    @Override
    protected void onDestroy() {
        LogHelper.v(TAG, "onDestroy");
        mBinding = null;
        mGitHubUserPair_ViewModel = null;
        mTabLayout = null;
        mViewPager = null;
        mWinner = null;
        mTabFragment1 = null;
        mTabFragment2 = null;
        super.onDestroy();
    }
}
