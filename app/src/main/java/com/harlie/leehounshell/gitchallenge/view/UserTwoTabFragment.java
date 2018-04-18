package com.harlie.leehounshell.gitchallenge.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.list.RepositoryListAdapter;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

// FIXME: i want to do data binding here but can't get it working..
// FIXME: so instead i pass the GitHubUser_Model via the Green Robot Event Bus

public class UserTwoTabFragment extends UserTabFragment {
    private final static String TAG = "LEE: <" + UserTwoTabFragment.class.getSimpleName() + ">";

    private BrowseUsersRepositorysActivity.GitHubUserTwoResultsEvent mLastEvent;

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
            mUserNameLabel = mView.findViewById(R.id.tab2_user_name_label);
            mUserName = mView.findViewById(R.id.tab2_user_name);
            mProfileUrlLabel = mView.findViewById(R.id.tab2_user_profile_url_label);
            mProfileUrl = mView.findViewById(R.id.tab2_user_profile_url);
            mAvatar = mView.findViewById(R.id.tab2_user_avatar);
            initializeRecyclerView();
        }
    }

    private void initializeRecyclerView() {
        LogHelper.v(TAG, "initializeRecyclerView");
        if (mRecyclerView == null && mGitHubUser != null) {
            mRecyclerView = mView.findViewById(R.id.tab2_repo_recycler_view);
            if (mRecyclerView != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setNestedScrollingEnabled(false);
                RepositoryListAdapter repoListAdapter = new RepositoryListAdapter(this, mGitHubUser.getUserRepositoryList());
                mRecyclerView.setAdapter(repoListAdapter);
            } else {
                LogHelper.e(TAG, "*** null RecyclerView ***");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BrowseUsersRepositorysActivity.GitHubUserTwoResultsEvent event) {
        LogHelper.v(TAG, "===> onMessageEvent: userNumber=" + mUserNumber + ", event=" + event);
        mLastEvent = event;
        handleLastEvent();
    }

    public void handleLastEvent() {
        if (mLastEvent != null) {
            LogHelper.v(TAG, "handleLastEvent: mLastEvent=" + mLastEvent);
            setGitHubUser(mLastEvent.getUserModel());
            findTheViews();
            if (mUserName != null && mUserNameLabel != null) {
                mUserNameLabel.setText(getString(R.string.user_name));
                mUserName.setText(getGitHubUser().getUserName());
            }
            if (mProfileUrl != null && mProfileUrlLabel != null) {
                mProfileUrlLabel.setText(getString(R.string.profile));
                mProfileUrl.setText(getGitHubUser().getUserProfileUrl());
            }
            if (mAvatar != null) {
                setAvatarClickListener();
                loadAvatarImage(getGitHubUser().getUserAvatarUrl());
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

