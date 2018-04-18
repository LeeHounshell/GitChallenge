package com.harlie.leehounshell.gitchallenge.util;

import android.content.Context;
import android.databinding.BaseObservable;

import com.harlie.leehounshell.gitchallenge.BR;
import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;
import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;

public class TheWinner extends BaseObservable {
    private final static String TAG = "LEE: <" + TheWinner.class.getSimpleName() + ">";

    private String mWinner = "";

    public String getWinner() {
        LogHelper.v(TAG, "getWinner");
        return mWinner;
    }

    public void setWinner(GitUser_Model user, int totalStars) {
        LogHelper.v(TAG, "setWinner: user=" + user);
        Context context = GitChallengeApplication.getAppContext();
        this.mWinner = user.getUserName()
                + " "
                + context.getString(R.string.the_winner)
                + totalStars
                + " "
                + context.getString(R.string.stars);
        notifyPropertyChanged(BR.winner);
    }
}
