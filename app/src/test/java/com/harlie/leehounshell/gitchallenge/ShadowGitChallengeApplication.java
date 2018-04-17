package com.harlie.leehounshell.gitchallenge;


import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowApplication;

@Implements(GitChallengeApplication.class)
public class ShadowGitChallengeApplication extends ShadowApplication
{
    private static ShadowGitChallengeApplication sInstance;

    public ShadowGitChallengeApplication() {
        sInstance = this;
    }

    @SuppressWarnings("unused")
    public static synchronized ShadowGitChallengeApplication getInstance() {
        return ShadowGitChallengeApplication.sInstance;
    }

}
