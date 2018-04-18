package com.harlie.leehounshell.gitchallenge.model;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.harlie.leehounshell.gitchallenge.util.LogHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class GitHubUser_Model extends BaseObservable implements Parcelable {
    private final static String TAG = "LEE: <" + GitHubUser_Model.class.getSimpleName() + ">";

    private String mUserName;
    private String mUserProfileUrl;
    private String mUserAvatarUrl;
    private List<Repository_Model> mUserRepositoryList;

    public GitHubUser_Model() {
        this.mUserRepositoryList = new ArrayList<>();
    }

    public GitHubUser_Model(String userName, String userProfileUrl, String userAvatarUrl, List<Repository_Model> userRepositoryList) {
        this.mUserName = userName;
        this.mUserProfileUrl = userProfileUrl;
        this.mUserAvatarUrl = userAvatarUrl;
        this.mUserRepositoryList = userRepositoryList;
        this.mUserRepositoryList = new ArrayList<>();
    }

    public void enterGitHubUser(View view) {
        mUserName = ((AppCompatTextView) view).getText().toString().trim();
        LogHelper.v(TAG, "enterGitHubUser: mUserName=" + mUserName);
        GitHubUserNameRequestChangeEvent event = new GitHubUserNameRequestChangeEvent(mUserName, hashCode());
        event.post();
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

    protected GitHubUser_Model(Parcel in) {
        this.mUserName = in.readString();
        this.mUserProfileUrl = in.readString();
        this.mUserAvatarUrl = in.readString();
        this.mUserRepositoryList = in.createTypedArrayList(Repository_Model.CREATOR);
    }

    public static final Parcelable.Creator<GitHubUser_Model> CREATOR = new Parcelable.Creator<GitHubUser_Model>() {
        @Override
        public GitHubUser_Model createFromParcel(Parcel source) {
            return new GitHubUser_Model(source);
        }

        @Override
        public GitHubUser_Model[] newArray(int size) {
            return new GitHubUser_Model[size];
        }
    };

    @Override
    public String toString() {
        return "GitHubUser_Model{" +
                "mUserName='" + mUserName + '\'' +
                ", mUserProfileUrl='" + mUserProfileUrl + '\'' +
                ", mUserAvatarUrl='" + mUserAvatarUrl + '\'' +
                ", mUserRepositoryList=" + mUserRepositoryList +
                '}';
    }

    public static class GitHubUserNameRequestChangeEvent {
        private String textMessage;
        private int callbackId;

        GitHubUserNameRequestChangeEvent(String textMessage, int callbackId) {
            this.textMessage = textMessage;
            this.callbackId = callbackId;
        }

        public String getTextMessage() {
            return textMessage;
        }

        public int getCallbackId() {
            return callbackId;
        }

        public void post() {
            LogHelper.v(TAG, "post: textMessage=" + textMessage);
            GitHubUserNameRequestChangeEvent gitHubUserNameRequestChangeEvent = new GitHubUserNameRequestChangeEvent(textMessage, callbackId);
            EventBus.getDefault().post(gitHubUserNameRequestChangeEvent);
        }

        @Override
        public String toString() {
            return "GitHubUserNameRequestChangeEvent{" +
                    "textMessage='" + textMessage + '\'' +
                    "callbackId='" + callbackId + '\'' +
                    '}';
        }
    }
}
