package com.harlie.leehounshell.gitchallenge.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.model.Repository_Model;
import com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;

import static com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService.STATUS_ERROR;
import static com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService.STATUS_GIT_USER_RESULTS;

public class GitUsersSearchResults implements MyResultReceiver.Receiver {
    private final static String TAG = "LEE: <" + GitUsersSearchResults.class.getSimpleName() + ">";

    public static class GitUsersSearchResultsEvent {
        private final static String TAG = "LEE: <" + GitUsersSearchResultsEvent.class.getSimpleName() + ">";

        @SuppressWarnings("CanBeFinal")
        private GitUser_Model userOne;
        private GitUser_Model userTwo;

        GitUsersSearchResultsEvent(GitUser_Model userOne, GitUser_Model userTwo) {
            LogHelper.v(TAG, "GitUsersSearchResultsEvent");
            this.userOne = userOne;
            this.userTwo = userTwo;
        }

        public GitUser_Model getUserOne() {
            LogHelper.v(TAG, "getUserOne");
            return userOne;
        }

        public GitUser_Model getUserTwo() {
            LogHelper.v(TAG, "getUserTwo");
            return userTwo;
        }

        public boolean searchOneSuccess() {
            return userOne != null && userOne.getUserProfileUrl().length() > 0;
        }

        public boolean searchTwoSuccess() {
            return userTwo != null && userTwo.getUserProfileUrl().length() > 0;
        }

        public String toString() {
            return "GitUserSearchResultsEvent{" +
                    "userOne='" + getUserOne() + '\'' +
                    "userTwo='" + getUserTwo() + '\'' +
                    '}';
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        LogHelper.v(TAG, "onReceiveResult: resultCode=" + resultCode);
        switch (resultCode) {
            case STATUS_GIT_USER_RESULTS: {
                LogHelper.v(TAG, "onReceiveResult: STATUS_GIT_USER_RESULTS");
                GitUser_Model userOne = resultData.getParcelable(GitUsersSearchIntentService.GIT_USER_ONE);
                GitUser_Model userTwo = resultData.getParcelable(GitUsersSearchIntentService.GIT_USER_TWO);
                String resultsOne = resultData.getString(GitUsersSearchIntentService.GIT_USER_SEARCH_RESULTS_ONE);
                String resultsTwo = resultData.getString(GitUsersSearchIntentService.GIT_USER_SEARCH_RESULTS_TWO);

                parseGitHubData(userOne, resultsOne);
                parseGitHubData(userTwo, resultsTwo);

                LogHelper.v(TAG, "===> GIT USER SEARCH: userOne=" + userOne + ", userTwo=" + userTwo);
                post(userOne, userTwo);
                break;
            }
            case STATUS_ERROR: {
                LogHelper.v(TAG, "onReceiveResult: STATUS_ERROR");
                String problem = "Error: " + resultData.getString(Intent.EXTRA_TEXT);
                @SuppressLint("ShowToast") Toast toast = Toast.makeText(GitChallengeApplication.getAppContext(), problem, Toast.LENGTH_SHORT);
                new CustomToast(toast).invoke();
                break;
            }
        }
    }

    // parse the results into the GitUser_Model
    public void parseGitHubData(GitUser_Model user, String results) {
        LogHelper.v(TAG, "parseGitHubData");
        try {
            JSONArray jsonArray = new JSONArray(results);
            JSONObject jsonObj = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = jsonArray.getJSONObject(i);
                if (i == 0) {
                    JSONObject owner = (JSONObject) jsonObj.get("owner");
                    String userName = String.valueOf(owner.get("login"));
                    String userProfile = String.valueOf(owner.get("html_url"));
                    String avatarUrl = String.valueOf(owner.get("avatar_url"));
                    LogHelper.v(TAG, "userName: " + userName);
                    LogHelper.v(TAG, "userProfile: " + userProfile);
                    LogHelper.v(TAG, "avatarUrl: " + avatarUrl);
                    user.setUserName(userName);
                    user.setUserProfileUrl(userProfile);
                    user.setUserAvatarUrl(avatarUrl);
                }
                String repoName = String.valueOf(jsonObj.get("name"));
                String repoUrl = String.valueOf(jsonObj.get("html_url"));
                String repoStars = String.valueOf(jsonObj.get("stargazers_count"));
                LogHelper.v(TAG, "repo: " + repoName);
                LogHelper.v(TAG, "url: " + repoUrl);
                LogHelper.v(TAG, "stars: " + repoStars);
                Repository_Model repositoryModel = new Repository_Model();
                repositoryModel.setRepoName(repoName);
                repositoryModel.setRepoUrl(repoUrl);
                repositoryModel.setRepoStars(Integer.valueOf(repoStars));
                user.getUserRepositoryList().add(repositoryModel);
            }
            sortByNumberOfStars(user);
//            for (Repository_Model repositoryModel : user.getUserRepositoryList()) {
//                LogHelper.v(TAG, "SORTED ================================================================================");
//                LogHelper.v(TAG, "REPOSITORY: " + repositoryModel);
//            }
        } catch (JSONException e) {
            LogHelper.e(TAG, "JSON parse exception: e=" + e);
        }
    }

    private void sortByNumberOfStars(GitUser_Model user) {
        LogHelper.v(TAG, "sortByNumberOfStars");
        // sort the repository list by number of stars
        Collections.sort(user.getUserRepositoryList(), new Comparator<Repository_Model>() {
            @Override
            public int compare(Repository_Model o1, Repository_Model o2) {
                // -1 - less than, 1 - greater than, 0 - equal
                return o1.getRepoStars() > o2.getRepoStars() ? -1 : (o1.getRepoStars() < o2.getRepoStars()) ? 1 : 0;
            }
        });
    }

    private static void post(GitUser_Model userOne, GitUser_Model userTwo) {
        LogHelper.v(TAG, "post");
        GitUsersSearchResultsEvent usersSearchResultsEvent = new GitUsersSearchResultsEvent(userOne, userTwo);
        EventBus.getDefault().post(usersSearchResultsEvent);
    }
}
