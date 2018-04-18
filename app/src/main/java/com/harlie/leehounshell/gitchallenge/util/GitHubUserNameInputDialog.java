package com.harlie.leehounshell.gitchallenge.util;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.harlie.leehounshell.gitchallenge.R;
import com.harlie.leehounshell.gitchallenge.view.BaseActivity;

import org.greenrobot.eventbus.EventBus;

public class GitHubUserNameInputDialog
{
    private final static String TAG = "LEE: <" + GitHubUserNameInputDialog.class.getSimpleName() + ">";

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alert;

    public GitHubUserNameInputDialog(BaseActivity context, LayoutInflater inflater, String title, String message, final int callbackId)
    {
        LogHelper.v(TAG, "GitHubUserNameInputDialog");
        final ViewGroup nullParent = null;
        final View dialogView = inflater.inflate(R.layout.input_dialog, nullParent);
        final AppCompatEditText editText = dialogView.findViewById(R.id.the_input_user_name);

        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        alert.dismiss();
                        String textMessage = editText.getText().toString();
                        TextInputDialogEvent textInputDialogEvent = new TextInputDialogEvent(textMessage, callbackId);
                        textInputDialogEvent.post();
                    }
                });

        alert = alertDialogBuilder.create();
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    public static class TextInputDialogEvent {
        private String textMessage;
        private int callbackId;

        TextInputDialogEvent(String textMessage, int callbackId) {
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
            TextInputDialogEvent textInputDialogEvent = new TextInputDialogEvent(textMessage, callbackId);
            EventBus.getDefault().post(textInputDialogEvent);
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
