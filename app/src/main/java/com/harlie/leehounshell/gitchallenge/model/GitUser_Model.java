package com.harlie.leehounshell.gitchallenge.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class GitUser_Model implements Parcelable {
    private final static String TAG = "LEE: <" + GitUser_Model.class.getSimpleName() + ">";

    private String mUserName;
    private String mUserProfileUrl;
    private String mUserAvatarUrl;
    private List<Repository_Model> mUserRepositoryList;

    public GitUser_Model() {
        this.mUserRepositoryList = new ArrayList<>();
    }

    public GitUser_Model(String userName, String userProfileUrl, String userAvatarUrl, List<Repository_Model> userRepositoryList) {
        this.mUserName = userName;
        this.mUserProfileUrl = userProfileUrl;
        this.mUserAvatarUrl = userAvatarUrl;
        this.mUserRepositoryList = userRepositoryList;
        this.mUserRepositoryList = new ArrayList<>();
    }

    public void enterGitUser(View view, MainActivity mainActivity) {
        mUserName = ((AppCompatEditText) view).getText().toString().trim();
        LogHelper.v(TAG, "enterGitUser: mUserName=" + mUserName);
        if (mUserName.length() == 0) {
            mainActivity.showSoftKeyboard();
        }
        else {
            mainActivity.hideSoftKeyboard();
        }
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getUserProfileUrl() {
        return mUserProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.mUserProfileUrl = userProfileUrl;
    }

    public String getUserAvatarUrl() {
        return mUserAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.mUserAvatarUrl = userAvatarUrl;
    }

    public List<Repository_Model> getUserRepositoryList() {
        return mUserRepositoryList;
    }

    public void setUserRepositoryList(List<Repository_Model> userRepositoryList) {
        this.mUserRepositoryList = userRepositoryList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUserName);
        dest.writeString(this.mUserProfileUrl);
        dest.writeString(this.mUserAvatarUrl);
        dest.writeTypedList(this.mUserRepositoryList);
    }

    protected GitUser_Model(Parcel in) {
        this.mUserName = in.readString();
        this.mUserProfileUrl = in.readString();
        this.mUserAvatarUrl = in.readString();
        this.mUserRepositoryList = in.createTypedArrayList(Repository_Model.CREATOR);
    }

    public static final Parcelable.Creator<GitUser_Model> CREATOR = new Parcelable.Creator<GitUser_Model>() {
        @Override
        public GitUser_Model createFromParcel(Parcel source) {
            return new GitUser_Model(source);
        }

        @Override
        public GitUser_Model[] newArray(int size) {
            return new GitUser_Model[size];
        }
    };

    @Override
    public String toString() {
        return "GitUser_Model{" +
                "mUserName='" + mUserName + '\'' +
                ", mUserProfileUrl='" + mUserProfileUrl + '\'' +
                ", mUserAvatarUrl='" + mUserAvatarUrl + '\'' +
                ", mUserRepositoryList=" + mUserRepositoryList +
                '}';
    }
}
