package com.harlie.leehounshell.gitchallenge.view_model;

import android.arch.lifecycle.ViewModel;
import android.os.Handler;

import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;
import com.harlie.leehounshell.gitchallenge.model.GitHubUser_Model;
import com.harlie.leehounshell.gitchallenge.service.GitHubUsersSearchIntentService;
import com.harlie.leehounshell.gitchallenge.util.GitHubUsersSearchResults;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.util.MyResultReceiver;

public class GitUserPair_ViewModel extends ViewModel {
    private final static String TAG = "LEE: <" + GitUserPair_ViewModel.class.getSimpleName() + ">";

    private GitHubUser_Model mGitHubUserOne;
    private String mUserOneResults;
    private GitHubUser_Model mGitHubUserTwo;
    private String mUserTwoResults;

    public GitUserPair_ViewModel() {
        mGitHubUserOne = new GitHubUser_Model();
        mGitHubUserTwo = new GitHubUser_Model();
    }

    public void searchForUsers(GitHubUser_Model userOne, GitHubUser_Model userTwo) {
        LogHelper.v(TAG, "searchForUsers: userOne=" + userOne + ", userTwo=" + userTwo);
        mGitHubUserOne = userOne;
        mGitHubUserTwo = userTwo;
        MyResultReceiver receiver = new MyResultReceiver(new Handler());
        receiver.setReceiver(new GitHubUsersSearchResults());
        GitHubUsersSearchIntentService.startActionFindGitHubUsers(GitChallengeApplication.getAppContext(), userOne, userTwo, receiver);
    }

    public GitHubUser_Model getGitHubUserOne() {
        return mGitHubUserOne;
    }

    public void setGitUserOne(GitHubUser_Model gitHubUserOne) {
        this.mGitHubUserOne = gitHubUserOne;
    }

    public GitHubUser_Model getGitUserTwo() {
        return mGitHubUserTwo;
    }

    public void setGitUserTwo(GitHubUser_Model gitHubUserTwo) {
        this.mGitHubUserTwo = gitHubUserTwo;
    }
}

