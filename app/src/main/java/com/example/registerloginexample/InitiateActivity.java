package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class InitiateActivity extends AppCompatActivity {

    private static final int LOADING_DELAY = 2000; // 로딩 화면 표시 시간 (밀리초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // 일정 시간이 지난 후에 메인 화면으로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(InitiateActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, LOADING_DELAY);
    }
}

