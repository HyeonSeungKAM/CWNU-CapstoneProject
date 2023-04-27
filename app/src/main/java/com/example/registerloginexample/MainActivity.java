package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> binList = new ArrayList<>();
    private Button btn_salesList,btn_logout, btn_binList, btn_list; // 판매하기, 목록 버튼

    private String userID, userName, kind;

    // 서버에서 가져온 내용 보여주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_id = findViewById(R.id.tv_id);
        TextView tv_name = findViewById(R.id.tv_name);

        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");

        tv_id.setText(userID);
        tv_name.setText(userName);

        btn_list = findViewById(R.id.btn_list);// 목록
        btn_list.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                startActivity(intent);

            }
        });


        btn_binList = findViewById(R.id.btn_binList);
        btn_binList.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                                JSONArray jsonArray = new JSONArray(response);
                                System.out.println(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                                    String binName = jsonObject.getString("binName");
                                    binList.add(binName);
                                }
                                Intent intent = new Intent(MainActivity.this, BinListActivity.class);
                                intent.putExtra("kind",kind);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);
                                intent.putExtra("binList", binList);
                                startActivity(intent);

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                BinListRequest binListRequest = new BinListRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(binListRequest);

            }
        });

        btn_logout = findViewById(R.id.btn_logout);// 목록
        btn_logout.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });



    }
}