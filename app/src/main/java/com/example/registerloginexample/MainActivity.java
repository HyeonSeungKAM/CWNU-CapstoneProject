package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


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

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                String glass = jsonObject.getString("glass");
                                String plastic = jsonObject.getString("plastic");
                                String paper = jsonObject.getString("paper");
                                String metal = jsonObject.getString("metal");

                                Toast.makeText(getApplicationContext(),"정보를 불러오는데 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent SELLintent = new Intent(MainActivity.this, SellActivity.class);
                                SELLintent.putExtra("glass",glass);
                                SELLintent.putExtra("plastic",plastic);
                                SELLintent.putExtra("paper",paper);
                                SELLintent.putExtra("metal",metal);

                                startActivity(SELLintent);

                            } else {
                                Toast.makeText(getApplicationContext(),"정보를 불러오는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                SellInfoRequest sellinfoRequest = new SellInfoRequest(binName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(sellinfoRequest);

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