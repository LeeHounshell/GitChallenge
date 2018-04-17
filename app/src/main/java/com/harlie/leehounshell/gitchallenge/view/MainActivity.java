package com.harlie.leehounshell.gitchallenge.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.databinding.ActivityMainBinding;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.view_model.GitUserPair_ViewModel;

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
        GitUser_Model mGitUserOne = mGitUserPair_ViewModel.getGitUserOne();
        GitUser_Model mGitUserTwo = mGitUserPair_ViewModel.getGitUserTwo();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setGitUserOne(mGitUserOne);
        mBinding.setGitUserTwo(mGitUserTwo);
        mBinding.setBaseActivity(this);
        mBinding.executePendingBindings();
    }

    @Override
    protected void onResume() {
        LogHelper.v(TAG, "onResume");
        super.onResume();
        tooltip();
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
                        AppCompatImageButton startButton = findViewById(R.id.start_button);
                        Tooltip.TooltipView tooltipView = Tooltip.make(MainActivity.this,
                                new Tooltip.Builder(1)
                                        .anchor(startButton, Tooltip.Gravity.TOP)
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
                                        .withArrow(true)
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
