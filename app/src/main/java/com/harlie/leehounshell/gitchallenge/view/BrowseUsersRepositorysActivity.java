package com.harlie.leehounshell.gitchallenge.view;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.view_model.GitUserPair_ViewModel;

public class BrowseUsersRepositorysActivity extends BaseActivity {
    private final static String TAG = "LEE: <" + BrowseUsersRepositorysActivity.class.getSimpleName() + ">";

    final static String KEY_USER_ONE            = "user_one";
    final static String KEY_USER_TWO            = "user_two";

    private GitUserPair_ViewModel mGitUserPair_ViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mGitUserPair_ViewModel = ViewModelProviders.of(this).get(GitUserPair_ViewModel.class);

        setContentView(R.layout.activity_browse_users_repositorys);

        if (getIntent().getExtras() != null) {
            GitUser_Model userOne = getIntent().getParcelableExtra(KEY_USER_ONE);
            mGitUserPair_ViewModel.setGitUserOne(userOne);
            GitUser_Model userTwo = getIntent().getParcelableExtra(KEY_USER_TWO);
            mGitUserPair_ViewModel.setGitUserTwo(userTwo);

            LogHelper.v(TAG, "userOne=" + userOne);
            LogHelper.v(TAG, "userTwo=" + userOne);
        }
        else {
            goToMainActivity();
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
        mGitUserPair_ViewModel = null;
        super.onDestroy();
    }
}
