package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button btn_logout, btn_sell, btn_list; // 판매하기, 목록 버튼
    private Spinner spinner_binName;
    private ArrayList<String> binList = new ArrayList<>();
    private ArrayAdapter<String> binNamesAdapter;
    RequestQueue requestQueue;


    // 서버에서 가져온 내용 보여주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");


        JSONObject postObject = new JSONObject();
        requestQueue = Volley.newRequestQueue(this);
        spinner_binName = (Spinner) findViewById(R.id.spinner_binName);

        String url = "http://gamhs44.ivyro.net/binList.php";
        try {
            postObject.put("userID",userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("binNames");
                    for(int i=0; i<jsonArray.length()){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String binName = jsonObject.optString("binNames");
                        binList.add(binName);
                        binNamesAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, binList);
                        binNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_binName.setAdapter(binNamesAdapter);
                    };
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);



       // TextView tv_id = findViewById(R.id.tv_id);
        TextView tv_name = findViewById(R.id.tv_userName);
       // TextView tv_mDate = findViewById(R.id.tv_mDate);
       // TextView tv_glassW = findViewById(R.id.tv_glassW);
       //TextView tv_plasticW = findViewById(R.id.tv_plasticW);
       // TextView tv_paperW = findViewById(R.id.tv_paperW);
       // TextView tv_metalW = findViewById(R.id.tv_metalW);



       // tv_id.setText(userID);
        tv_name.setText(userName);
     //   tv_mDate.setText(mDate);
     //   tv_glassW.setText(glass +"kg");
     //   tv_plasticW.setText(plastic+"kg");
      //  tv_paperW.setText(paper+"kg");
     //   tv_metalW.setText(metal+"kg");

     /*   btn_sell = findViewById(R.id.btn_sell); // 판매하기
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

                                Intent intent = new Intent(MainActivity.this, SellActivity.class);
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
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(sellinfoRequest);

            }
        });




        btn_list.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
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
        }); */

    }


}