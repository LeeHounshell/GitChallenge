package com.harlie.leehounshell.gitchallenge;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class GitChallengeApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        GitChallengeApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return GitChallengeApplication.context;
    }
}
