package com.harlie.leehounshell.gitchallenge.service;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.harlie.leehounshell.gitchallenge.model.GitHubUser_Model;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class GitHubUsersSearchIntentService extends IntentService {
    private final static String TAG = "LEE: <" + GitHubUsersSearchIntentService.class.getSimpleName() + ">";

    // from: https://developer.github.com/v3/
    private static final String GITHUB_API_ENDPOINT = "https://api.github.com";
    private static final String GITHUB_USER_SEARCH_URL = "/users/";
    private static final String GITHUB_USER_REPO_SEARCH_URL = "/repos";

    // IntentService can perform, e.g. ACTION_FIND_GITHUB_USERS
    public static final String ACTION_FIND_GITHUB_USERS = "com.harlie.leehounshell.gitchallenge.action.ACTION_FIND_GITHUB_USERS";

    public static final String GITHUB_USER_ONE = "com.harlie.leehounshell.gitchallenge.extra.GIT_USER_ONE_SEARCH";
    public static final String GITHUB_USER_TWO = "com.harlie.leehounshell.gitchallenge.extra.GIT_USER_TWO_SEARCH";
    public static final String GITHUB_USER_SEARCH_RESULTS_ONE = "com.harlie.leehounshell.gitchallenge.extra.GITHUB_USER_SEARCH_RESULTS_ONE";
    public static final String GITHUB_USER_SEARCH_RESULTS_TWO = "com.harlie.leehounshell.gitchallenge.extra.GITHUB_USER_SEARCH_RESULTS_TWO";
    public static final String RECEIVER = "receiver";

    public final static int STATUS_GITHUB_USER_RESULTS = 1;
    public final static int STATUS_ERROR = 2;

    private OkHttpClient okHttpClient;

    public GitHubUsersSearchIntentService() {
        super("GitHubUsersSearchIntentService");
        if (okHttpClient == null) {
            // avoid creating several instances, should be singleton
            okHttpClient = new OkHttpClient();
        }
    }

    /**
     * Starts this service to perform action FindGitHubUser with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFindGitHubUsers(Context context, GitHubUser_Model gitHubUserOne, GitHubUser_Model gitHubUserTwo, ResultReceiver searchResultReceiver) {
        LogHelper.v(TAG, "startActionFindGitHubUsers");
        Intent intent = new Intent(context, GitHubUsersSearchIntentService.class);
        intent.setAction(ACTION_FIND_GITHUB_USERS);
        intent.putExtra(GITHUB_USER_ONE, gitHubUserOne);
        intent.putExtra(GITHUB_USER_TWO, gitHubUserTwo);
        intent.putExtra(RECEIVER, searchResultReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogHelper.v(TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FIND_GITHUB_USERS.equals(action)) {
                GitHubUser_Model gitHubUserOne = intent.getParcelableExtra(GITHUB_USER_ONE);
                GitHubUser_Model gitHubUserTwo = intent.getParcelableExtra(GITHUB_USER_TWO);
                final ResultReceiver searchResultReceiver = intent.getParcelableExtra(RECEIVER);
                handleActionFindGitHubUsers(gitHubUserOne, gitHubUserTwo, searchResultReceiver);
            }
        }
    }

    /**
     * Handle action ACTION_FIND_GITHUB_USERS in the provided background thread with the provided parameters.
     */
    private void handleActionFindGitHubUsers(GitHubUser_Model gitHubUserOne, GitHubUser_Model gitHubUserTwo, ResultReceiver receiver) {
        LogHelper.v(TAG, "handleActionFindGitHubUsers");
        Bundle bundle = new Bundle();
        String resultsOne = requestGitHubRepositoriesForUser(gitHubUserOne.getUserName());
        String resultsTwo = requestGitHubRepositoriesForUser(gitHubUserTwo.getUserName());

        bundle.putParcelable(GitHubUsersSearchIntentService.GITHUB_USER_ONE, gitHubUserOne);
        bundle.putParcelable(GitHubUsersSearchIntentService.GITHUB_USER_TWO, gitHubUserTwo);
        bundle.putString(GitHubUsersSearchIntentService.GITHUB_USER_SEARCH_RESULTS_ONE, resultsOne);
        bundle.putString(GitHubUsersSearchIntentService.GITHUB_USER_SEARCH_RESULTS_TWO, resultsTwo);

        LogHelper.v(TAG, "---> handleActionFindGitHubUsers SEND THE RESULTS");
        LogHelper.v(TAG, "---> resultsOne=" + resultsOne);
        LogHelper.v(TAG, "---> resultsTwo=" + resultsTwo);
        receiver.send(STATUS_GITHUB_USER_RESULTS, bundle);
    }

    @SuppressWarnings("ReturnInsideFinallyBlock")
    private String requestGitHubRepositoriesForUser(String gitHubUserName) {
        LogHelper.v(TAG, "requestGitHubRepositoriesForUser: gitHubUserName=" + gitHubUserName);
        String results = "";
        try {
            Request request = new Request.Builder()
                    .url(GITHUB_API_ENDPOINT + GITHUB_USER_SEARCH_URL + gitHubUserName + GITHUB_USER_REPO_SEARCH_URL)
                    .header("User-Agent", "OkHttp GitHubUsersSearchIntentService.java")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .build();

            LogHelper.v(TAG, "---> request=" + request.toString());

            Response response = okHttpClient.newCall(request).execute(); // thread is already in the background, so synchronous call is ok here
            if (response != null && response.body() != null) {
                //noinspection ConstantConditions
                results = response.body().string();
                LogHelper.v(TAG, "---> results=" + results);
            }
            else {
                LogHelper.e(TAG, "*** network request results are null! ***");
            }
        }
        catch (NullPointerException e) {
            // JUNIT test
            System.err.println("WARNING: NullPointerException in handleActionFindGitHubUsers - JUNIT TEST?");
        }
        catch (IOException e) {
            LogHelper.e(TAG, "*** UNABLE TO LOAD JSON *** - e=" + e);
        }
        return results;
    }

    @Override
    public void onDestroy() {
        LogHelper.v(TAG, "onDestroy");
        super.onDestroy();
        okHttpClient = null;
    }
}

