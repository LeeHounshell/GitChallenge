package com.harlie.leehounshell.gitchallenge.test;


import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.model.Repository_Model;
import com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService;
import com.harlie.leehounshell.gitchallenge.util.FileUtil;
import com.harlie.leehounshell.gitchallenge.view.MainActivity;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService.ACTION_FIND_GIT_USERS;
import static com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService.GIT_USER_ONE;
import static com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService.GIT_USER_TWO;
import static com.harlie.leehounshell.gitchallenge.service.GitUsersSearchIntentService.RECEIVER;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@Config(sdk = 25) //clean up Robolectric message: “WARNING: unknown service autofill”
@RunWith(RobolectricTestRunner.class)
public class GitUsersSearchIntentServiceTest {
    private final static String TAG = "LEE: <" + GitUsersSearchIntentServiceTest.class.getSimpleName() + ">";

    private ServiceController<GitUsersSearchIntentService> controller;
    private GitUser_Model gitUserModelOne = null;
    private GitUser_Model gitUserModelTwo = null;
    private ResultReceiver resultReceiver = null;

    @Before
    public void setUp() {
        controller = Robolectric.buildService(GitUsersSearchIntentService.class);
        GitUsersSearchIntentService service = controller.bind().create().get();

        gitUserModelOne = new GitUser_Model();
        gitUserModelOne.setUserName("leehounshell");
        gitUserModelOne.setUserAvatarUrl("https://avatars3.githubusercontent.com/u/1031573?v=4");
        gitUserModelOne.setUserProfileUrl("https://github.com/LeeHounshell");
        Repository_Model repository1 = new Repository_Model("Android-Wear-Weather-WatchFace", "https://github.com/LeeHounshell/Android-Wear-Weather-WatchFace", 1);
        Repository_Model repository2 = new Repository_Model("GitChallenge", "https://github.com/LeeHounshell/GitChallenge", 0);
        Repository_Model repository3 = new Repository_Model("iOS-calculator", "https://github.com/LeeHounshell/iOS-calculator", 0);
        Repository_Model repository4 = new Repository_Model("Music-Search", "https://github.com/LeeHounshell/Music-Search", 1);
        Repository_Model repository5 = new Repository_Model("Capstone-Project", "https://github.com/LeeHounshell/Capstone-Project", 0);
        List<Repository_Model> repoListOne = new ArrayList<>();
        repoListOne.add(repository1);
        repoListOne.add(repository2);
        repoListOne.add(repository3);
        repoListOne.add(repository4);
        repoListOne.add(repository5);
        gitUserModelOne.setUserRepositoryList(repoListOne);

        gitUserModelTwo = new GitUser_Model();
        gitUserModelTwo.setUserName("udacity");
        gitUserModelTwo.setUserAvatarUrl("https://avatars2.githubusercontent.com/u/1916665?v=4");
        gitUserModelTwo.setUserProfileUrl("https://github.com/udacity");
        Repository_Model repository6 = new Repository_Model("2d-game-dev-project-0", "https://github.com/udacity/2d-game-dev-project-0", 0);
        Repository_Model repository7 = new Repository_Model("activeadmin_json_editor", "https://github.com/udacity/activeadmin_json_editor", 4);
        Repository_Model repository8 = new Repository_Model("AdvancedAndroid_ClassicalMusicQuiz", "https://github.com/udacity/AdvancedAndroid_ClassicalMusicQuiz", 59);
        Repository_Model repository9 = new Repository_Model("AdvancedAndroid_Emojify", "https://github.com/udacity/AdvancedAndroid_Emojify", 68);
        List<Repository_Model> repoListTwo = new ArrayList<>();
        repoListTwo.add(repository6);
        repoListTwo.add(repository7);
        repoListTwo.add(repository8);
        repoListTwo.add(repository9);
        gitUserModelTwo.setUserRepositoryList(repoListTwo);

        resultReceiver = new ResultReceiver(new Handler());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testWithIntent() {
        AppCompatActivity activity = Robolectric.setupActivity(MainActivity.class);
        Handler handler = new Handler();
        Intent intent = new Intent(RuntimeEnvironment.application, GitUsersSearchIntentService.class);
        intent.setAction(ACTION_FIND_GIT_USERS);
        intent.putExtra(GIT_USER_ONE, gitUserModelOne);
        intent.putExtra(GIT_USER_TWO, gitUserModelTwo);
        intent.putExtra(RECEIVER, new ResultReceiver(handler));

        controller.withIntent(intent).startCommand(0, 0);
        // assert here
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("repos_test_data.json");
            assertThat(in, notNullValue());
            String jsonInfo = FileUtil.convertStreamToString(in);
            System.out.println(TAG + "jsonInfo=" + jsonInfo);
            assertThat(jsonInfo, notNullValue());

            try {
                JSONArray jsonArray = new JSONArray(jsonInfo);
                JSONObject jsonObj = null;
                String userName = null;
                String userProfile = null;
                String avatarUrl = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObj = jsonArray.getJSONObject(i);
                    if (i == 0) {
                        System.out.println("================================================================================");
                        JSONObject owner = (JSONObject) jsonObj.get("owner");
                        userName = String.valueOf(owner.get("login"));
                        assertThat(userName, notNullValue());
                        userProfile = String.valueOf(owner.get("html_url"));
                        assertThat(userProfile, notNullValue());
                        avatarUrl = String.valueOf(owner.get("avatar_url"));
                        assertThat(avatarUrl, notNullValue());

                        System.out.println("userName: " + userName);
                        System.out.println("userProfile: " + userProfile);
                        System.out.println("avatarUrl: " + avatarUrl);
                    }
                    System.out.println("");
                    System.out.println("--------------------------------------------------------------------------------");
                    String repoName = String.valueOf(jsonObj.get("name"));
                    assertThat(repoName, notNullValue());
                    String repoUrl = String.valueOf(jsonObj.get("html_url"));
                    assertThat(repoUrl, notNullValue());
                    String repoStars = String.valueOf(jsonObj.get("stargazers_count"));
                    assertThat(repoStars, notNullValue());

                    System.out.println("repo: " + repoName);
                    System.out.println("url: " + repoUrl);
                    System.out.println("stars: " + repoStars);
                }
                if (userName == null || userProfile == null || avatarUrl == null) {
                    fail("Unable to parse: login, html_url or avatar_url");
                }
            } catch (JSONException e) {
                Assert.fail("failed to parse JSON containing search result");
                e.printStackTrace();
            }
        }
        catch (JsonParseException e) {
            System.err.println(TAG + "*** UNABLE TO PARSE JSON *** - e=" + e);
            Assert.fail("failed to parse JSON containing search result");
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testOnHandleIntent() {
        AppCompatActivity activity = Robolectric.setupActivity(MainActivity.class);
        Handler handler = new Handler();
        MyTestIntentService intentService = new MyTestIntentService();
        Intent intent = new Intent(RuntimeEnvironment.application, GitUsersSearchIntentService.class);
        intent.setAction(ACTION_FIND_GIT_USERS);
        intent.putExtra(GIT_USER_ONE, gitUserModelOne);
        intent.putExtra(GIT_USER_TWO, gitUserModelTwo);
        intent.putExtra(RECEIVER, new ResultReceiver(handler));
        controller.withIntent(intent).startCommand(0, 0);
        // now we can call onHandleIntent in the Service
        intentService.onHandleIntent(intent);
    }

    @After
    public void tearDown() {
        controller.destroy();
    }

    public static class MyTestIntentService extends GitUsersSearchIntentService {

        @Override
        protected void onHandleIntent(Intent intent) {
            super.onHandleIntent(intent);
        }
    }

}

