package com.harlie.leehounshell.gitchallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Repository_Model implements Parcelable {
    private final static String TAG = "LEE: <" + Repository_Model.class.getSimpleName() + ">";

    private String repoName;
    private String repoUrl;
    private Integer repoStars;

    public Repository_Model() {
    }

    public Repository_Model(String repoName, String repoUrl, Integer repoStars) {
        this.repoName = repoName;
        this.repoUrl = repoUrl;
        this.repoStars = repoStars;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public Integer getRepoStars() {
        return repoStars;
    }

    public void setRepoStars(Integer repoStars) {
        this.repoStars = repoStars;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.repoName);
        dest.writeString(this.repoUrl);
        dest.writeValue(this.repoStars);
    }

    protected Repository_Model(Parcel in) {
        this.repoName = in.readString();
        this.repoUrl = in.readString();
        this.repoStars = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Repository_Model> CREATOR = new Parcelable.Creator<Repository_Model>() {
        @Override
        public Repository_Model createFromParcel(Parcel source) {
            return new Repository_Model(source);
        }

        @Override
        public Repository_Model[] newArray(int size) {
            return new Repository_Model[size];
        }
    };

    @Override
    public String toString() {
        return "Repository_Model{" +
                "repoName='" + repoName + '\'' +
                ", repoUrl='" + repoUrl + '\'' +
                ", repoStars=" + repoStars +
                '}';
    }
}
