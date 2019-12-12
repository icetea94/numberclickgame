package com.devshyeon.numberclickgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        backPressCloseHandler = new BackPressCloseHandler(this);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
    }
    public void start(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

        backPressCloseHandler.onBackPressed();
    }
}
