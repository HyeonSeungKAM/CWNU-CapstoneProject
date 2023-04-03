package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {


    private Button btn_sell2, btn_main; // 판매하기, 취소

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        btn_sell2 = findViewById(R.id.btn_sell2); // 판매하기 
        btn_main = findViewById(R.id.btn_main); // 메인


        btn_sell2.setOnClickListener(new View.OnClickListener() {    // 판매하기
            @Override
            public void onClick(View view) {
                Intent sellContinue_intent = new Intent(ListActivity.this, SellActivity.class);
                startActivity(sellContinue_intent);

            }
        });

        btn_main.setOnClickListener(new View.OnClickListener() {    // 메인
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    }

