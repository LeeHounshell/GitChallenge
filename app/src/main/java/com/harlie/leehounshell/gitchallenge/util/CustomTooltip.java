package com.harlie.leehounshell.gitchallenge.util;

import android.support.v7.widget.AppCompatImageButton;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.view.BaseActivity;
import com.harlie.leehounshell.gitchallenge.view.MainActivity;

import it.sephiroth.android.library.tooltip.Tooltip;

public class CustomTooltip {
    private final static String TAG = "LEE: <" + CustomToast.class.getSimpleName() + ">";

    private static final int TOOLTIP_DISPLAY_TIME = 5000;
    private static volatile boolean sSeenTooltip_GitHubUsers = false;

    public static void tooltip(final BaseActivity activity, final int viewAnchorId) {
        LogHelper.v(TAG, "tooltip");
        if (activity.baseActivity() != null && ! sSeenTooltip_GitHubUsers) {
            activity.baseActivity().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (activity.baseActivity() != null) {
                        LogHelper.v(TAG, "tooltip: make");
                        sSeenTooltip_GitHubUsers = true;
                        String tip = activity.getResources().getString(R.string.tooltip_enter_github_users);
                        AppCompatImageButton anchor = activity.findViewById(viewAnchorId);
                        Tooltip.TooltipView tooltipView = Tooltip.make(activity,
                                new Tooltip.Builder(1)
                                        .anchor(anchor, Tooltip.Gravity.CENTER)
                                        .closePolicy(new Tooltip.ClosePolicy()
                                                .insidePolicy(true, false)
                                                .outsidePolicy(true, false), TOOLTIP_DISPLAY_TIME)
                                        .activateDelay(0)
                                        .showDelay(0)
                                        .text(tip)
                                        .textSize(activity.getResources().getInteger(R.integer.tooltip_text_size))
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
}
