package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private Button btn_sell2, btn_main; // 판매하기, 취소
    private ListView list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        list.setAdapter(adapter); // 어댑터를 리스트와 연결

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String binName = intent.getStringExtra("binName");
        String glass = intent.getStringExtra("glass");
        String plastic = intent.getStringExtra("plastic");
        String paper = intent.getStringExtra("paper");
        String metal = intent.getStringExtra("metal");

        btn_sell2 = findViewById(R.id.btn_sell2); // 판매하기 
        btn_main = findViewById(R.id.btn_main); // 메인


        btn_sell2.setOnClickListener(new View.OnClickListener() {    // 판매하기(올리기)
            @Override
            public void onClick(View view) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    public void onResponse(String response) {

                    }

                }

                    Intent intent = new Intent(ListActivity.this, SellActivity.class);
                    intent.putExtra("binName",binName);
                    intent.putExtra("userID",userID);
                    intent.putExtra("userName",userName);
                    intent.putExtra("glass",glass);
                    intent.putExtra("plastic",plastic);
                    intent.putExtra("paper",paper);
                    intent.putExtra("metal",metal);
                    startActivity(intent);

            }
        });

        btn_main.setOnClickListener(new View.OnClickListener() {    // 메인
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                intent.putExtra("binName",binName);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("glass",glass);
                intent.putExtra("plastic",plastic);
                intent.putExtra("paper",paper);
                intent.putExtra("metal",metal);

                startActivity(intent);
            }
        });









    }





}

