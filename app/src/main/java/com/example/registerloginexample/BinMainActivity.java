package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BinMainActivity extends AppCompatActivity {

    private Button btn_salesList, btn_map, btn_logout, btn_editBin, btn_sell, btn_list, btn_binList;



    // 서버에서 가져온 내용 보여주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binmain);

        TextView tv_id = findViewById(R.id.tv_id);
        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_binName = findViewById(R.id.tv_binName);
        TextView tv_binLoc = findViewById(R.id.tv_binLoc);
        TextView tv_mDate = findViewById(R.id.tv_mDate);
        TextView tv_glassW = findViewById(R.id.tv_glassW);
        TextView tv_plasticW = findViewById(R.id.tv_plasticW);
        TextView tv_paperW = findViewById(R.id.tv_paperW);
        TextView tv_metalW = findViewById(R.id.tv_metalW);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String address = intent.getStringExtra("address");
        String binName = intent.getStringExtra("binName");
        String binLoc = intent.getStringExtra("binLoc");
        String mDate = intent.getStringExtra("mDate");
        String glass = intent.getStringExtra("glass");
        String plastic = intent.getStringExtra("plastic");
        String paper = intent.getStringExtra("paper");
        String metal = intent.getStringExtra("metal");

        tv_id.setText(userID);
        tv_binName.setText(binName);
        tv_name.setText(userName);
        tv_binLoc.setText(binLoc);
        tv_mDate.setText(mDate);
        tv_glassW.setText(glass +"kg");
        tv_plasticW.setText(plastic+"kg");
        tv_paperW.setText(paper+"kg");
        tv_metalW.setText(metal+"kg");

        btn_map = findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, MapActivity.class);
                intent.putExtra("binLoc",binLoc);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                intent.putExtra("binName",binName);
                intent.putExtra("binLoc",binLoc);
            }
        });


        btn_editBin = findViewById(R.id.btn_editBin);
        btn_editBin.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, BinEditActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                intent.putExtra("binName",binName);
                intent.putExtra("binLoc",binLoc);
                startActivity(intent);
            }
        });


        btn_list = findViewById(R.id.btn_list); // 목록
        btn_list.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, ListActivity.class);

                intent.putExtra("kind",kind);
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


        btn_sell = findViewById(R.id.btn_sell); // 판매하기
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

                                Intent intent = new Intent(BinMainActivity.this, SellActivity.class);
                                intent.putExtra("kind",kind);
                                intent.putExtra("binName",binName);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);
                                intent.putExtra("glass",glass);
                                intent.putExtra("plastic",plastic);
                                intent.putExtra("paper",paper);
                                intent.putExtra("metal",metal);

                                startActivity(intent);

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
                RequestQueue queue = Volley.newRequestQueue(BinMainActivity.this);
                queue.add(sellinfoRequest);

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

                            ArrayList<String> binList = new ArrayList<>();
                            String binList_edit = "쓰레기통 추가/삭제";
                            binList.add(binList_edit);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                String binName = jsonObject.getString("binName");
                                binList.add(binName);
                            }
                            Intent intent = new Intent(BinMainActivity.this, BinListActivity.class);
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
                RequestQueue queue = Volley.newRequestQueue(BinMainActivity.this);
                queue.add(binListRequest);

            }
        });

        btn_salesList = findViewById(R.id.btn_salesList);
        btn_salesList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, SalesListActivity.class);
                intent.putExtra("binLoc",binLoc);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                intent.putExtra("binName",binName);
                intent.putExtra("binLoc",binLoc);
                startActivity(intent);
            }
        });




        btn_logout = findViewById(R.id.btn_logout);// 목록
        btn_logout.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });

    }
}