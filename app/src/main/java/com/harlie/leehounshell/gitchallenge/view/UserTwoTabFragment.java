package com.harlie.leehounshell.gitchallenge.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

// FIXME: i want to do data binding here but can't get it working..
// FIXME: so instead i pass the GitUser_Model via the Green Robot Event Bus

public class UserTwoTabFragment extends UserTabFragment {
    private final static String TAG = "LEE: <" + UserTwoTabFragment.class.getSimpleName() + ">";

    private static BrowseUsersRepositorysActivity.GitUserTwoResultsEvent mLastEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogHelper.v(TAG, "onCreateView");
        mView = inflater.inflate(R.layout.user2_tab_fragment, container, false);
        findTheViews();
        return mView;
    }

    private void findTheViews() {
        LogHelper.v(TAG, "findTheViews");
        if (mView != null) {
            mUserName = mView.findViewById(R.id.tab2_user_name);
            mProfileUrl = mView.findViewById(R.id.tab2_user_profile_url);
            mAvatar = mView.findViewById(R.id.tab2_user_avatar);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BrowseUsersRepositorysActivity.GitUserTwoResultsEvent event) {
        LogHelper.v(TAG, "===> onMessageEvent: userNumber=" + mUserNumber + ", event=" + event);
        mLastEvent = event;
        handleLastEvent();
    }

    public void handleLastEvent() {
        if (mLastEvent != null) {
            LogHelper.v(TAG, "handleLastEvent: mLastEvent=" + mLastEvent);
            setGitUser(mLastEvent.getUserModel());
            findTheViews();
            if (mUserName != null) {
                mUserName.setText(getGitUser().getUserName());
            }
            if (mProfileUrl != null) {
                mProfileUrl.setText(getGitUser().getUserProfileUrl());
            }
            if (mAvatar != null) {
                setAvatarClickListener();
                loadAvatarImage(getGitUser().getUserAvatarUrl());
            }
        }
        else {
            LogHelper.w(TAG, "handleLastEvent: empty");
        }
    }

    @Override
    public void onDestroy() {
        LogHelper.v(TAG, "onDestroy");
        mLastEvent = null;
        super.onDestroy();
    }
}

