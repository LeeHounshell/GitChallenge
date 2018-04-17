package com.harlie.leehounshell.gitchallenge.service;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
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
public class GitUsersSearchIntentService extends IntentService {
    private final static String TAG = "LEE: <" + GitUsersSearchIntentService.class.getSimpleName() + ">";

    // from: https://developer.github.com/v3/
    private static final String GIT_API_ENDPOINT = "https://api.github.com";
    private static final String GIT_USER_SEARCH_URL = "/users/";
    private static final String GIT_USER_REPO_SEARCH_URL = "/repos";

    // IntentService can perform, e.g. ACTION_FIND_GIT_USERS
    public static final String ACTION_FIND_GIT_USERS = "com.harlie.leehounshell.gitchallenge.action.ACTION_FIND_GIT_USERS";

    public static final String GIT_USER_ONE = "com.harlie.leehounshell.gitchallenge.extra.GIT_USER_ONE_SEARCH";
    public static final String GIT_USER_TWO = "com.harlie.leehounshell.gitchallenge.extra.GIT_USER_TWO_SEARCH";
    public static final String GIT_USER_SEARCH_RESULTS_ONE = "com.harlie.leehounshell.gitchallenge.extra.GIT_USER_SEARCH_RESULTS_ONE";
    public static final String GIT_USER_SEARCH_RESULTS_TWO = "com.harlie.leehounshell.gitchallenge.extra.GIT_USER_SEARCH_RESULTS_TWO";
    public static final String RECEIVER = "receiver";

    public final static int STATUS_GIT_USER_RESULTS = 1;
    public final static int STATUS_ERROR = 2;

    private OkHttpClient okHttpClient;

    public GitUsersSearchIntentService() {
        super("GitUsersSearchIntentService");
        if (okHttpClient == null) {
            // avoid creating several instances, should be singleton
            okHttpClient = new OkHttpClient();
        }
    }

    /**
     * Starts this service to perform action FindGitUser with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFindGitUsers(Context context, GitUser_Model gitUserOne, GitUser_Model gitUserTwo, ResultReceiver searchResultReceiver) {
        LogHelper.v(TAG, "startActionFindGitUsers");
        Intent intent = new Intent(context, GitUsersSearchIntentService.class);
        intent.setAction(ACTION_FIND_GIT_USERS);
        intent.putExtra(GIT_USER_ONE, gitUserOne);
        intent.putExtra(GIT_USER_TWO, gitUserTwo);
        intent.putExtra(RECEIVER, searchResultReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogHelper.v(TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FIND_GIT_USERS.equals(action)) {
                GitUser_Model gitUserOne = intent.getParcelableExtra(GIT_USER_ONE);
                GitUser_Model gitUserTwo = intent.getParcelableExtra(GIT_USER_TWO);
                final ResultReceiver searchResultReceiver = intent.getParcelableExtra(RECEIVER);
                handleActionFindGitUsers(gitUserOne, gitUserTwo, searchResultReceiver);
            }
        }
    }

    /**
     * Handle action ACTION_FIND_GIT_USERS in the provided background thread with the provided parameters.
     */
    private void handleActionFindGitUsers(GitUser_Model gitUserOne, GitUser_Model gitUserTwo, ResultReceiver receiver) {
        LogHelper.v(TAG, "handleActionFindGitUsers");
        Bundle bundle = new Bundle();
        String resultsOne = requestGitRepositoriesForUser(gitUserOne.getUserName());
        String resultsTwo = requestGitRepositoriesForUser(gitUserTwo.getUserName());

        bundle.putParcelable(GitUsersSearchIntentService.GIT_USER_ONE, gitUserOne);
        bundle.putParcelable(GitUsersSearchIntentService.GIT_USER_TWO, gitUserTwo);
        bundle.putString(GitUsersSearchIntentService.GIT_USER_SEARCH_RESULTS_ONE, resultsOne);
        bundle.putString(GitUsersSearchIntentService.GIT_USER_SEARCH_RESULTS_TWO, resultsTwo);

        LogHelper.v(TAG, "---> handleActionFindGitUsers SEND THE RESULTS");
        LogHelper.v(TAG, "---> resultsOne=" + resultsOne);
        LogHelper.v(TAG, "---> resultsTwo=" + resultsTwo);
        receiver.send(STATUS_GIT_USER_RESULTS, bundle);
    }

    private String requestGitRepositoriesForUser(String gitUserName) {
        LogHelper.v(TAG, "requestGitRepositoriesForUser: gitUserName=" + gitUserName);
        String results = "";
        try {
            Request request = new Request.Builder()
                    .url(GIT_API_ENDPOINT + GIT_USER_SEARCH_URL + gitUserName + GIT_USER_REPO_SEARCH_URL)
                    .header("User-Agent", "OkHttp GitUsersSearchIntentService.java")
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
            System.err.println("WARNING: NullPointerException in handleActionFindGitUsers - JUNIT TEST?");
        }
        catch (IOException e) {
            LogHelper.e(TAG, "*** UNABLE TO LOAD JSON *** - e=" + e);
        }
        finally {
            return results;
        }
    }

    @Override
    public void onDestroy() {
        LogHelper.v(TAG, "onDestroy");
        super.onDestroy();
        okHttpClient = null;
    }
}

