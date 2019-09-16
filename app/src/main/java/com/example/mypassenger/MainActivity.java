package com.example.mypassenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

public class MainActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    TextView mTextView;

    private Socket mSocket;{

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progress_bar);
        mTextView = findViewById(R.id.progress_text);
    }

    public void start(View view) {
    }
}
