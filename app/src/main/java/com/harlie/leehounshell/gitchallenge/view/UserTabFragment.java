package com.harlie.leehounshell.gitchallenge.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;
import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

public class UserTabFragment extends Fragment {
    private final static String TAG = "LEE: <" + UserTabFragment.class.getSimpleName() + ">";

    protected View mView;
    protected GitUser_Model mGitUser;
    protected AppCompatTextView mUserName;
    protected AppCompatTextView mProfileUrl;
    protected RoundedImageView mAvatar;
    protected int mUserNumber;

    @Override
    public void onStart() {
        LogHelper.v(TAG, "onStart");
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        LogHelper.v(TAG, "onStop");
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void loadAvatarImage(String userAvatarUrl) {
        LogHelper.v(TAG, "loadAvatarImage");
        if (mAvatar != null && userAvatarUrl != null && userAvatarUrl.length() > 0) {
            Context context = GitChallengeApplication.getAppContext();

//            Picasso.with(context)
//                    .load(userAvatarUrl)
//                    .placeholder(R.drawable.generic_photo)
//                    .fit()
//                    .into(mAvatar);

            Glide.with(context)
                    .load(userAvatarUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.generic_photo)
                            .fitCenter()
                    )
                    .into(mAvatar);
        }
    }

    public GitUser_Model getGitUser() {
        return mGitUser;
    }

    public void setGitUser(GitUser_Model gitUser) {
        this.mGitUser = gitUser;
    }

    public int getUserNumber() {
        return mUserNumber;
    }

    public void setUserNumber(int userNumber) {
        LogHelper.v(TAG, "setUserNumber: " + userNumber);
        this.mUserNumber = userNumber;
    }

    @Override
    public void onDestroy() {
        LogHelper.v(TAG, "onDestroy");
        mView = null;
        mGitUser = null;
        mUserName = null;
        mProfileUrl = null;
        mAvatar = null;
        super.onDestroy();
    }
}

