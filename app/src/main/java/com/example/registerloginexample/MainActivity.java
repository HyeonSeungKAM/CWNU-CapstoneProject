package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button btn_sell, btn_list; // 판매하기, 목록 버튼


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



        btn_sell = findViewById(R.id.btn_sell); // 판매하기
        btn_list = findViewById(R.id.btn_list); // 목록
        btn_sell.setOnClickListener(new View.OnClickListener() {    // 판매하기
            @Override
            public void onClick(View view) {
                Intent SELLintent = new Intent(MainActivity.this, SellActivity.class);
                startActivity(SELLintent);

            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent LISTintent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(LISTintent);

            }
        });



    }


}