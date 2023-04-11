package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SellBoardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellboard);

        TextView tv_test = findViewById(R.id.tv_test);

        Intent intent = getIntent();
        String t_value = intent.getExtras().getString("key");

        tv_test.setText(t_value);

    }
}
