package com.example.mypassenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.nkzawa.socketio.client.Socket;

public class MainActivity extends AppCompatActivity {

    private Socket mSocket;{

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
