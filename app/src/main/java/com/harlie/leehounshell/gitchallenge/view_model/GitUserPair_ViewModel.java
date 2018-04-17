package com.harlie.leehounshell.gitchallenge.view_model;

import android.arch.lifecycle.ViewModel;

import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;

public class GitUserPair_ViewModel extends ViewModel {
    private final static String TAG = "LEE: <" + GitUserPair_ViewModel.class.getSimpleName() + ">";

    private GitUser_Model mGitUserOne;
    private GitUser_Model mGitUserTwo;

    public GitUserPair_ViewModel() {
        mGitUserOne = new GitUser_Model();
        mGitUserTwo = new GitUser_Model();
    }

    public GitUser_Model getGitUserOne() {
        return mGitUserOne;
    }

    public void setGitUserOne(GitUser_Model mGitUserOne) {
        this.mGitUserOne = mGitUserOne;
    }

    public GitUser_Model getGitUserTwo() {
        return mGitUserTwo;
    }

    public void setGitUserTwo(GitUser_Model mGitUserTwo) {
        this.mGitUserTwo = mGitUserTwo;
    }
}

