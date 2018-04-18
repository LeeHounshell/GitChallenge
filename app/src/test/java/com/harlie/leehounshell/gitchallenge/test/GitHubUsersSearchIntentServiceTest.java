package com.harlie.leehounshell.gitchallenge.test;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonParseException;
import com.harlie.leehounshell.gitchallenge.model.GitHubUser_Model;
import com.harlie.leehounshell.gitchallenge.model.Repository_Model;
import com.harlie.leehounshell.gitchallenge.service.GitHubUsersSearchIntentService;
import com.harlie.leehounshell.gitchallenge.util.FileUtil;
import com.harlie.leehounshell.gitchallenge.util.GitHubUsersSearchResults;
import com.harlie.leehounshell.gitchallenge.view.MainActivity;

import junit.framework.Assert;

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

import static com.harlie.leehounshell.gitchallenge.service.GitHubUsersSearchIntentService.ACTION_FIND_GITHUB_USERS;
import static com.harlie.leehounshell.gitchallenge.service.GitHubUsersSearchIntentService.GITHUB_USER_ONE;
import static com.harlie.leehounshell.gitchallenge.service.GitHubUsersSearchIntentService.GITHUB_USER_TWO;
import static com.harlie.leehounshell.gitchallenge.service.GitHubUsersSearchIntentService.RECEIVER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@Config(sdk = 25) //clean up Robolectric message: “WARNING: unknown service autofill”
@RunWith(RobolectricTestRunner.class)
public class GitHubUsersSearchIntentServiceTest {
    private final static String TAG = "LEE: <" + GitHubUsersSearchIntentServiceTest.class.getSimpleName() + ">";

    private ServiceController<GitHubUsersSearchIntentService> controller;
    private GitHubUser_Model gitUserModelOne = null;
    private GitHubUser_Model gitUserModelTwo = null;
    private ResultReceiver resultReceiver = null;

    @Before
    public void setUp() {
        controller = Robolectric.buildService(GitHubUsersSearchIntentService.class);
        GitHubUsersSearchIntentService service = controller.bind().create().get();

        gitUserModelOne = new GitHubUser_Model();
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

        gitUserModelTwo = new GitHubUser_Model();
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
        Intent intent = new Intent(RuntimeEnvironment.application, GitHubUsersSearchIntentService.class);
        intent.setAction(ACTION_FIND_GITHUB_USERS);
        intent.putExtra(GITHUB_USER_ONE, gitUserModelOne);
        intent.putExtra(GITHUB_USER_TWO, gitUserModelTwo);
        intent.putExtra(RECEIVER, new ResultReceiver(handler));

        controller.withIntent(intent).startCommand(0, 0);
        // assert here
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("repos_test_data.json");
            assertThat(in, notNullValue());
            String jsonInfo = FileUtil.convertStreamToString(in);
            System.out.println(TAG + "jsonInfo=" + jsonInfo);
            assertThat(jsonInfo, notNullValue());
            GitHubUsersSearchResults results = new GitHubUsersSearchResults();
            GitHubUser_Model model = new GitHubUser_Model();

            // PARSE THE JSON!
            results.parseGitHubData(model, jsonInfo);

            System.out.println("================================================================================");
            System.out.println("userName: " + model.getUserName());
            System.out.println("userProfile: " + model.getUserProfileUrl());
            System.out.println("avatarUrl: " + model.getUserAvatarUrl());
            assertThat(model.getUserName(), notNullValue());
            assertThat(model.getUserProfileUrl(), notNullValue());
            assertThat(model.getUserAvatarUrl(), notNullValue());

            for (Repository_Model repositoryModel : model.getUserRepositoryList()) {
                assertThat(repositoryModel.getRepoName(), notNullValue());
                assertThat(repositoryModel.getRepoUrl(), notNullValue());
                assertThat(repositoryModel.getRepoStars(), notNullValue());
                System.out.println("--------------------------------------------------------------------------------");
                System.out.println("repo: " + repositoryModel.getRepoName());
                System.out.println("url: " + repositoryModel.getRepoUrl());
                System.out.println("stars: " + repositoryModel.getRepoStars());
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
        MyTestIntentServiceHub intentService = new MyTestIntentServiceHub();
        Intent intent = new Intent(RuntimeEnvironment.application, GitHubUsersSearchIntentService.class);
        intent.setAction(ACTION_FIND_GITHUB_USERS);
        intent.putExtra(GITHUB_USER_ONE, gitUserModelOne);
        intent.putExtra(GITHUB_USER_TWO, gitUserModelTwo);
        intent.putExtra(RECEIVER, new ResultReceiver(handler));
        controller.withIntent(intent).startCommand(0, 0);
        // now we can call onHandleIntent in the Service
        intentService.onHandleIntent(intent);
    }

    @After
    public void tearDown() {
        controller.destroy();
    }

    public static class MyTestIntentServiceHub extends GitHubUsersSearchIntentService {

        @Override
        protected void onHandleIntent(Intent intent) {
            super.onHandleIntent(intent);
        }
    }

}

