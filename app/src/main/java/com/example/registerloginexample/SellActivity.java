package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SellActivity extends AppCompatActivity {

    private Button btn_sellContinue, btn_cancel; // 판매하기, 취소

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String glass = intent.getStringExtra("glass");
        String binName = intent.getStringExtra("binName");



        btn_sellContinue = findViewById(R.id.btn_sellContinue); // 판매하기
        btn_cancel = findViewById(R.id.btn_cancel); // 목록


        btn_sellContinue.setOnClickListener(new View.OnClickListener() {    // 판매하기
            @Override
            public void onClick(View view) {
                Intent sellContinue_intent = new Intent(SellActivity.this, ListActivity.class);
                startActivity(sellContinue_intent);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {    // 취소
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}