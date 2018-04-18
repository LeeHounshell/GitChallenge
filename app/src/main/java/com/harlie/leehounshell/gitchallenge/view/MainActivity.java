package com.harlie.leehounshell.gitchallenge.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.databinding.ActivityMainBinding;
import com.harlie.leehounshell.gitchallenge.model.GitHubUser_Model;
import com.harlie.leehounshell.gitchallenge.util.CustomToast;
import com.harlie.leehounshell.gitchallenge.util.CustomTooltip;
import com.harlie.leehounshell.gitchallenge.util.GitHubUserNameInputDialog;
import com.harlie.leehounshell.gitchallenge.util.GitHubUsersSearchResults;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.util.NetworkHelper;
import com.harlie.leehounshell.gitchallenge.view_model.GitHubUserPair_ViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {
    private final static String TAG = "LEE: <" + MainActivity.class.getSimpleName() + ">";

    private ActivityMainBinding mBinding;
    private GitHubUserPair_ViewModel mGitHubUserPair_ViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mGitHubUserPair_ViewModel = ViewModelProviders.of(this).get(GitHubUserPair_ViewModel.class);
        GitHubUser_Model gitHubUserOne = mGitHubUserPair_ViewModel.getGitHubUserOne();
        GitHubUser_Model gitHubUserTwo = mGitHubUserPair_ViewModel.getGitHubUserTwo();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setGitHubUserOne(gitHubUserOne);
        mBinding.setGitHubUserTwo(gitHubUserTwo);
        mBinding.setMainActivity(this);
        mBinding.executePendingBindings();
    }

    @Override
    protected void onResume() {
        LogHelper.v(TAG, "onResume");
        super.onResume();
        CustomTooltip.tooltip(this, R.id.start_button);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GitHubUser_Model.GitHubUserNameRequestChangeEvent event) {
        LogHelper.v(TAG, "REQUEST NAME CHANGE! onMessageEvent: event=" + event);
        if (event.getCallbackId() == mGitHubUserPair_ViewModel.getGitHubUserOne().hashCode()) {
            LogHelper.v(TAG, "request enter new UserOne name");
            popupInputDialog(mGitHubUserPair_ViewModel.getGitHubUserOne());
        }
        else if (event.getCallbackId() == mGitHubUserPair_ViewModel.getGitHubUserTwo().hashCode()) {
            LogHelper.v(TAG, "request enter new UserTwo name");
            popupInputDialog(mGitHubUserPair_ViewModel.getGitHubUserTwo());
        }
        else {
            LogHelper.w(TAG, "*** orphan event=" + event);
        }
    }

    public void popupInputDialog(GitHubUser_Model gitHubUser_model) {
        LogHelper.v(TAG, "popupInputDialog");
        GitHubUserNameInputDialog gitHubUserNameInputDialog =
                new GitHubUserNameInputDialog(this,
                        this.getLayoutInflater(),
                        getString(R.string.github_user_name),
                        getString(R.string.enter_github_user_name),
                        gitHubUser_model.hashCode());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GitHubUserNameInputDialog.TextInputDialogEvent event) {
        LogHelper.v(TAG, "GET USER NAME! onMessageEvent: event=" + event);
        if (event.getCallbackId() == mGitHubUserPair_ViewModel.getGitHubUserOne().hashCode()) {
            LogHelper.v(TAG, "set UserOne name: name=" + event.getTextMessage());
            mGitHubUserPair_ViewModel.getGitHubUserOne().setUserName(event.getTextMessage());
            // FIXME: this should notify using the data binding..
//            mGitHubUserPair_ViewModel.getGitHubUserOne().notifyPropertyChanged(R.id.git_user_one_edit_text);
            // TODO: remove the findViewById code when the above data binding issue is resolved
            AppCompatTextView editedNameTextView = findViewById(R.id.git_user_one_edit_text);
            editedNameTextView.setText(event.getTextMessage());
        }
        else if (event.getCallbackId() == mGitHubUserPair_ViewModel.getGitHubUserTwo().hashCode()) {
            LogHelper.v(TAG, "set UserTwo name: name=" + event.getTextMessage());
            mGitHubUserPair_ViewModel.getGitHubUserTwo().setUserName(event.getTextMessage());
            // FIXME: this should notify using the data binding..
//            mGitHubUserPair_ViewModel.getGitHubUserOne().notifyPropertyChanged(R.id.git_user_two_edit_text);
            // TODO: remove the findViewById code when the data binding issue is resolved
            AppCompatTextView editedNameTextView = findViewById(R.id.git_user_two_edit_text);
            editedNameTextView.setText(event.getTextMessage());
        }
        else {
            LogHelper.w(TAG, "*** orphan event=" + event);
        }
    }

    public void searchGitHubUsers(GitHubUser_Model userOne, GitHubUser_Model userTwo) {
        if (userOne != null && userOne.getUserName() != null && userOne.getUserName().length() > 0
                && userTwo != null && userTwo.getUserName() != null && userTwo.getUserName().length() > 0) {
            LogHelper.v(TAG, "searchGitHubUsers: userOne=" + userOne.getUserName() + ", userTwo=" + userTwo.getUserName());
            getProgressCircle().setVisibility(View.VISIBLE);
            if (getProgressCircle() != null) {
                getProgressCircle().setVisibility(View.VISIBLE);
            }
            mGitHubUserPair_ViewModel.searchForUsers(userOne, userTwo);
        }
        else {
            LogHelper.v(TAG, "searchGitHubUsers: INVALID user name(s)");
            String invalidUserName = getString(R.string.empty_user_name);
            CustomToast.post(invalidUserName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GitHubUsersSearchResults.GitHubUsersSearchResultsEvent event) {
        LogHelper.v(TAG, "SEARCH RESULTS! onMessageEvent: event=" + event);
        if (getProgressCircle() != null) {
            getProgressCircle().setVisibility(View.GONE);
        }
        if (event.searchOneSuccess() && event.searchTwoSuccess()) {
            LogHelper.v(TAG, "successful search");
            goToBrowseUsersRepositorysActivity(
                    event.getUserOne(),
                    event.getUserTwo());
        }
        else {
            LogHelper.v(TAG, "unsuccessful search");
            if (NetworkHelper.isNotOnline()) {
                LogHelper.v(TAG, "offline");
                String not_online = getString(R.string.not_online);
                CustomToast.post(not_online);
            }
            else {
                LogHelper.v(TAG, "online");
                if (!event.searchOneSuccess()) {
                    LogHelper.v(TAG, "bad user one");
                    String invalidUserName = getString(R.string.cant_get_user_name_one);
                    CustomToast.post(invalidUserName);
                }
                if (!event.searchTwoSuccess()) {
                    LogHelper.v(TAG, "bad user two");
                    String invalidUserName = getString(R.string.cant_get_user_name_two);
                    CustomToast.post(invalidUserName);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        LogHelper.v(TAG, "onDestroy");
        mBinding = null;
        mGitHubUserPair_ViewModel = null;
        super.onDestroy();
    }
}
