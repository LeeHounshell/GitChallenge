package com.harlie.leehounshell.gitchallenge.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.harlie.leehounshell.gitchallenge.util.LogHelper;
import com.harlie.leehounshell.gitchallenge.view.BaseActivity;

import java.util.List;

public class GitUser_Model implements Parcelable {
    private final static String TAG = "LEE: <" + GitUser_Model.class.getSimpleName() + ">";

    private String userName;
    private String userEmail;
    private String userProfileUrl;
    private String userAvatarUrl;
    private List<Repository_Model> userRepositoryList;

    public GitUser_Model() {
    }

    public GitUser_Model(String userName, String userEmail, String userProfileUrl, String userAvatarUrl, List<Repository_Model> userRepositoryList) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userProfileUrl = userProfileUrl;
        this.userAvatarUrl = userAvatarUrl;
        this.userRepositoryList = userRepositoryList;
    }

    public void enterGitUser(View view, BaseActivity baseActivity) {
        userName = ((AppCompatEditText) view).getText().toString().trim();
        LogHelper.v(TAG, "enterGitUser: userName=" + userName);
        baseActivity.hideSoftKeyboard();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public List<Repository_Model> getUserRepositoryList() {
        return userRepositoryList;
    }

    public void setUserRepositoryList(List<Repository_Model> userRepositoryList) {
        this.userRepositoryList = userRepositoryList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.userEmail);
        dest.writeString(this.userProfileUrl);
        dest.writeString(this.userAvatarUrl);
        dest.writeTypedList(this.userRepositoryList);
    }

    protected GitUser_Model(Parcel in) {
        this.userName = in.readString();
        this.userEmail = in.readString();
        this.userProfileUrl = in.readString();
        this.userAvatarUrl = in.readString();
        this.userRepositoryList = in.createTypedArrayList(Repository_Model.CREATOR);
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
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userProfileUrl='" + userProfileUrl + '\'' +
                ", userAvatarUrl='" + userAvatarUrl + '\'' +
                ", userRepositoryList=" + userRepositoryList +
                '}';
    }
}
