package com.example.mauiviewcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mauiviewcontrol.BeamformerClient;
import com.example.mauiviewcontrol.R;

import k3900.K3900;

public class LogInActivity extends AppCompatActivity {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void requestUserLogIn(View view) {
        EditText userNameEditText = findViewById(R.id.userNameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        String result = mBackend.onUserLogIn(userNameEditText.getText().toString(), passwordEditText.getText().toString());
        TextView outputTextView = findViewById(R.id.outputTextView);
        outputTextView.setText(result);
        userNameEditText.clearComposingText();
        passwordEditText.clearComposingText();
    }

    public void requestUserLogOut(View view) {
        String result = mBackend.onUserLogOut();
        TextView outputTextView = findViewById(R.id.outputTextView);
        String text = result + "\n\nUser Logged Out.\n";
        outputTextView.setText(text);
    }

    public void requestAbout(View view) {
        //K3900.AboutMsg aboutMsg = mBackend.onGetAbout();
        //TextView aboutTextView = findViewById(R.id.aboutTextView);
        //aboutTextView.setText(aboutMsg.toString());
    }

    public void requestMauiGui(View view) {
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.textView);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
        //Bundle extras = new Bundle();
        //extras.putString("USER_NAME","jhon Doe");
        //extras.putInt("USER_ID", 21);
        //extras.putIntArray("USER", , [1, 2, 3, 4, 5]);
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("org.qtproject.example.MauiGuiMaster");
        //launchIntent.putExtras(extras);
        //launchIntent.arg
        //Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            //Toast.makeText(MainActivity.this, "There is no package available in android", Toast.LENGTH_LONG).show();
        }
    }
}
