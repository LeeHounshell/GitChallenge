package com.harlie.leehounshell.gitchallenge.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;
import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.util.CustomToast;
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

    public void setAvatarClickListener() {
        if (mAvatar != null && mProfileUrl != null && mProfileUrl.getText().toString().length() > 0) {
            mAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogHelper.v(TAG, "-CLICK-");
                    showUserProfile(mProfileUrl.getText().toString());
                }
            });
        }
    }

    private void showUserProfile(String profileUrl) {
        LogHelper.v(TAG, "showUserProfile");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(profileUrl));
        try {
            LogHelper.v(TAG, "startActivityForResult: Linked-In");
            startActivityForResult(intent, 5678);
        } catch (final android.content.ActivityNotFoundException e) {
            LogHelper.w(TAG, "no internet browser found: e=" + e);
            String no_internet_browser = getResources().getString(R.string.no_internet_browser);
            @SuppressLint("ShowToast") Toast toast = Toast.makeText(getContext(), no_internet_browser, Toast.LENGTH_SHORT);
            new CustomToast(toast).invoke();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogHelper.v(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == 5678) {
            LogHelper.v(TAG, "GitHub profile successfully viewed");
        }
        super.onActivityResult(requestCode, resultCode, data);
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

