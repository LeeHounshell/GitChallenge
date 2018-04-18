package com.harlie.leehounshell.gitchallenge.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.databinding.ActivityMainBinding;
import com.harlie.leehounshell.gitchallenge.model.GitHubUser_Model;
import com.harlie.leehounshell.gitchallenge.util.CustomToast;
import com.harlie.leehounshell.gitchallenge.util.GitHubUserNameInputDialog;
import com.harlie.leehounshell.gitchallenge.util.GitHubUsersSearchResults;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.view_model.GitUserPair_ViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import it.sephiroth.android.library.tooltip.Tooltip;

public class MainActivity extends BaseActivity {
    private final static String TAG = "LEE: <" + MainActivity.class.getSimpleName() + ">";

    private final static int TOOLTIP_DISPLAY_TIME = 5000;

    private ActivityMainBinding mBinding;
    private GitUserPair_ViewModel mGitUserPair_ViewModel;

    public static volatile boolean sSeenTooltip_GitUsers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mGitUserPair_ViewModel = ViewModelProviders.of(this).get(GitUserPair_ViewModel.class);
        GitHubUser_Model gitHubUserOne = mGitUserPair_ViewModel.getGitHubUserOne();
        GitHubUser_Model gitHubUserTwo = mGitUserPair_ViewModel.getGitUserTwo();

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
        tooltip();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GitHubUser_Model.GitHubUserNameRequestChangeEvent event) {
        LogHelper.v(TAG, "onMessageEvent: event=" + event);
        if (event.getCallbackId() == mGitUserPair_ViewModel.getGitHubUserOne().hashCode()) {
            LogHelper.v(TAG, "request enter new UserOne name");
            popupInputDialog(mGitUserPair_ViewModel.getGitHubUserOne());
        }
        else if (event.getCallbackId() == mGitUserPair_ViewModel.getGitUserTwo().hashCode()) {
            LogHelper.v(TAG, "request enter new UserTwo name");
            popupInputDialog(mGitUserPair_ViewModel.getGitUserTwo());
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
        LogHelper.v(TAG, "onMessageEvent: event=" + event);
        if (event.getCallbackId() == mGitUserPair_ViewModel.getGitHubUserOne().hashCode()) {
            LogHelper.v(TAG, "set UserOne name: name=" + event.getTextMessage());
            mGitUserPair_ViewModel.getGitHubUserOne().setUserName(event.getTextMessage());
            // FIXME: this should notify using the data binding..
//            mGitUserPair_ViewModel.getGitHubUserOne().notifyPropertyChanged(R.id.git_user_one_edit_text);
            // TODO: remove the findViewById code when the data binding issue is resolved
            AppCompatTextView editedNameTextView = findViewById(R.id.git_user_one_edit_text);
            editedNameTextView.setText(event.getTextMessage());
        }
        else if (event.getCallbackId() == mGitUserPair_ViewModel.getGitUserTwo().hashCode()) {
            LogHelper.v(TAG, "set UserTwo name: name=" + event.getTextMessage());
            mGitUserPair_ViewModel.getGitUserTwo().setUserName(event.getTextMessage());
            // FIXME: this should notify using the data binding..
//            mGitUserPair_ViewModel.getGitHubUserOne().notifyPropertyChanged(R.id.git_user_two_edit_text);
            // TODO: remove the findViewById code when the data binding issue is resolved
            AppCompatTextView editedNameTextView = findViewById(R.id.git_user_two_edit_text);
            editedNameTextView.setText(event.getTextMessage());
        }
        else {
            LogHelper.w(TAG, "*** orphan event=" + event);
        }
    }

    public void searchGitUsers(GitHubUser_Model userOne, GitHubUser_Model userTwo) {
        if (userOne != null && userOne.getUserName() != null && userOne.getUserName().length() > 0
                && userTwo != null && userTwo.getUserName() != null && userTwo.getUserName().length() > 0) {
            LogHelper.v(TAG, "searchGitUsers: userOne=" + userOne.getUserName() + ", userTwo=" + userTwo.getUserName());
            getProgressCircle().setVisibility(View.VISIBLE);
            if (getProgressCircle() != null) {
                getProgressCircle().setVisibility(View.VISIBLE);
            }
            mGitUserPair_ViewModel.searchForUsers(userOne, userTwo);
        }
        else {
            LogHelper.v(TAG, "searchGitUsers: INVALID user name(s)");
            String invalidUserName = getString(R.string.empty_user_name);
            CustomToast.post(invalidUserName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GitHubUsersSearchResults.GitHubUsersSearchResultsEvent event) {
        LogHelper.v(TAG, "onMessageEvent: event=" + event);
        if (getProgressCircle() != null) {
            getProgressCircle().setVisibility(View.GONE);
        }
        if (event.searchOneSuccess() && event.searchTwoSuccess()) {
            goToBrowseUsersRepositorysActivity(
                    event.getUserOne(),
                    event.getUserTwo());
        }
        else {
            if (! event.searchOneSuccess()) {
                String invalidUserName = getString(R.string.invalid_user_name_one);
                CustomToast.post(invalidUserName);
            }
            if (! event.searchTwoSuccess()) {
                String invalidUserName = getString(R.string.invalid_user_name_two);
                CustomToast.post(invalidUserName);
            }
        }
    }

    private void tooltip() {
        LogHelper.v(TAG, "tooltip");
        if (baseActivity() != null && ! sSeenTooltip_GitUsers) {
            baseActivity().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (baseActivity() != null) {
                        LogHelper.v(TAG, "tooltip: make");
                        sSeenTooltip_GitUsers = true;
                        String tip = getResources().getString(R.string.tooltip_enter_github_users);
                        AppCompatImageButton anchor = findViewById(R.id.start_button);
                        Tooltip.TooltipView tooltipView = Tooltip.make(MainActivity.this,
                                new Tooltip.Builder(1)
                                        .anchor(anchor, Tooltip.Gravity.CENTER)
                                        .closePolicy(new Tooltip.ClosePolicy()
                                                .insidePolicy(true, false)
                                                .outsidePolicy(true, false), TOOLTIP_DISPLAY_TIME)
                                        .activateDelay(0)
                                        .showDelay(0)
                                        .text(tip)
                                        .textSize(getResources().getInteger(R.integer.tooltip_text_size))
                                        .withStyleId(R.style.ToolTipLayoutCustomStyle)
                                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                                        .maxWidth(600)
                                        .withArrow(false)
                                        .withOverlay(true).build()
                        );
                        tooltipView.show();
                    }
                }
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        LogHelper.v(TAG, "onDestroy");
        mBinding = null;
        mGitUserPair_ViewModel = null;
        super.onDestroy();
    }
}
