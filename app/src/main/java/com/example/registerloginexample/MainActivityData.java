package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivityData extends AppCompatActivity {

    // 서버에서 가져온 내용 보여주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_id = findViewById(R.id.tv_id);
        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_glassW = findViewById(R.id.tv_glassW);
        TextView tv_binName = findViewById(R.id.tv_binName);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String glass = intent.getStringExtra("glass");
        String binName = intent.getStringExtra("binName");

        tv_id.setText(userID);
        tv_name.setText(userName);
        tv_glassW.setText(glass);
        tv_binName.setText(binName);

    }


}