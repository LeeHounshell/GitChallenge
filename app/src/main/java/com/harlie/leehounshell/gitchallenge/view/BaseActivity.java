package com.harlie.leehounshell.gitchallenge.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.harlie.leehounshell.gitchallenge.GitChallengeApplication;
import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.model.GitUser_Model;
import com.harlie.leehounshell.gitchallenge.util.CustomToast;
import com.harlie.leehounshell.gitchallenge.util.LogHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity
{
    private final static String TAG = "LEE: <" + BaseActivity.class.getSimpleName() + ">";

    private volatile boolean mStopped;
    private Handler mHandler;
    private ProgressBar mProgressCircle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogHelper.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        hideSoftKeyboard();
    }

    @Override
    protected void onStart() {
        LogHelper.v(TAG, "onStart");
        super.onStart();
        if (this instanceof MainActivity) {
            mProgressCircle = findViewById(R.id.progress_circle_main);
        }
    }

    @Override
    protected void onResume() {
        LogHelper.v(TAG, "onResume");
        super.onResume();
        mStopped = false;
    }

    public void searchGitUsers(GitUser_Model userOne, GitUser_Model userTwo) {
        if (userOne != null && userOne.getUserName() != null && userOne.getUserName().length() > 0
         && userTwo != null && userTwo.getUserName() != null && userTwo.getUserName().length() > 0) {
            LogHelper.v(TAG, "searchGitUsers: userOne=" + userOne.getUserName() + ", userTwo=" + userTwo.getUserName());
            getProgressCircle().setVisibility(View.VISIBLE);
            // TODO: load github data for both users
        }
        else {
            LogHelper.v(TAG, "searchGitUsers: INVALID user name(s)");
            String invalidUserName = getString(R.string.invalid_user_name);
            CustomToast.post(invalidUserName);
        }
    }

    @Override
    public boolean onNavigateUp(){
        LogHelper.v(TAG, "onNavigateUp");
        finish();
        return true;
    }

    public BaseActivity baseActivity() {
        return (mStopped) ? null : this;
    }

    public Handler getHandler() {
        //LogHelper.v(TAG, "getHandler");
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToastEvent(CustomToast.CustomToastEvent event) {
        LogHelper.v(TAG, "onToastEvent");
        if (event != null) {
            LogHelper.v(TAG, "onToastEvent: message=" + event.getToastMessage());
            String message = event.getToastMessage();
            @SuppressLint("ShowToast") Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            new CustomToast(toast).invoke();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static int orientation() {
        //LogHelper.v(TAG, "orientation");
        Context context = GitChallengeApplication.getAppContext();
        int orientation = context.getResources().getConfiguration().orientation;
        int sDeviceOrientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            sDeviceOrientation = Configuration.ORIENTATION_PORTRAIT;
        }
        else {
            sDeviceOrientation = Configuration.ORIENTATION_LANDSCAPE;
        }
        return sDeviceOrientation;
    }

    int getWidth() {
        //LogHelper.v(TAG, "getWidth");
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    int getHeight() {
        //LogHelper.v(TAG, "getHeight");
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    ProgressBar getProgressCircle() {
        //LogHelper.v(TAG, "getProgressCircle");
        return mProgressCircle;
    }

    public void hideSoftKeyboard() {
        LogHelper.v(TAG, "hideSoftKeyboard");
        if (getWindow() != null) {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    void goToMainActivity() {
        LogHelper.v(TAG, "goToMainActivity");
        Intent browseIntent = new Intent(this, MainActivity.class);
        startTheActivity(browseIntent);
    }

    private void startTheActivity(Intent intent) {
        LogHelper.v(TAG, "startTheActivity");
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    @Override
    protected void onStop() {
        LogHelper.v(TAG, "onStop");
        super.onStop();
        mStopped = true;
    }

    @Override
    protected void onDestroy() {
        LogHelper.v(TAG, "onDestroy");
        mHandler = null;
        mProgressCircle = null;
        super.onDestroy();
    }

}
