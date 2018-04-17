package com.harlie.leehounshell.gitchallenge.view_model;

import android.arch.lifecycle.ViewModel;
import android.os.Handler;

import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService;
import com.harlie.leehounshell.gitchallenge.util.GitUsersSearchResults;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.util.MyResultReceiver;

public class GitUserPair_ViewModel extends ViewModel {
    private final static String TAG = "LEE: <" + GitUserPair_ViewModel.class.getSimpleName() + ">";

    private GitUser_Model mGitUserOne;
    private String mUserOneResults;
    private GitUser_Model mGitUserTwo;
    private String mUserTwoResults;

    public GitUserPair_ViewModel() {
        mGitUserOne = new GitUser_Model();
        mGitUserTwo = new GitUser_Model();
    }

    public void searchForUsers(GitUser_Model userOne, GitUser_Model userTwo) {
        LogHelper.v(TAG, "searchForUsers: userOne=" + userOne + ", userTwo=" + userTwo);
        mGitUserOne = userOne;
        mGitUserTwo = userTwo;
        MyResultReceiver receiver = new MyResultReceiver(new Handler());
        receiver.setReceiver(new GitUsersSearchResults());
        GitUsersSearchIntentService.startActionFindGitUsers(GitChallengeApplication.getAppContext(), userOne, userTwo, receiver);
    }

    public GitUser_Model getGitUserOne() {
        return mGitUserOne;
    }

    public void setGitUserOne(GitUser_Model gitUserOne) {
        this.mGitUserOne = gitUserOne;
    }

    public GitUser_Model getGitUserTwo() {
        return mGitUserTwo;
    }

    public void setGitUserTwo(GitUser_Model gitUserTwo) {
        this.mGitUserTwo = gitUserTwo;
    }
}

