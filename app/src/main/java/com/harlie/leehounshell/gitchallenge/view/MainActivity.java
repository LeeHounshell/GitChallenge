package com.harlie.leehounshell.gitchallenge.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.databinding.ActivityMainBinding;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.util.CustomToast;
import com.harlie.leehounshell.gitchallenge.util.GitUsersSearchResults;
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
        GitUser_Model gitUserOne = mGitUserPair_ViewModel.getGitUserOne();
        GitUser_Model gitUserTwo = mGitUserPair_ViewModel.getGitUserTwo();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setGitUserOne(gitUserOne);
        mBinding.setGitUserTwo(gitUserTwo);
        mBinding.setMainActivity(this);
        mBinding.executePendingBindings();
    }

    @Override
    protected void onResume() {
        LogHelper.v(TAG, "onResume");
        super.onResume();
        tooltip();
    }

    public void searchGitUsers(GitUser_Model userOne, GitUser_Model userTwo) {
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
    public void onMessageEvent(GitUsersSearchResults.GitUsersSearchResultsEvent event) {
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
                        String tip = getResources().getString(R.string.tooltip_enter_git_users);
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
