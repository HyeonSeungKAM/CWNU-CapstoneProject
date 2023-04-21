package com.example.registerloginexample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class SellActivity extends AppCompatActivity {

    private Button btn_sellContinue, btn_cancel; // 판매하기, 취소


    // 날짜 -----------------------------------------------------------
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    long dnow;
    Date ddate;
    private String getTime(){
        dnow = System.currentTimeMillis();
        ddate = new Date(dnow);
        return mFormat.format("yyyy-MM-dd hh:mm:ss");
    }
// -----------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        TextView tv_totalGlassPrice = findViewById(R.id.tv_totalGlassPrice);
        TextView tv_totalPlasticPrice = findViewById(R.id.tv_totalPlasticPrice);
        TextView tv_totalPaperPrice = findViewById(R.id.tv_totalPaperPrice);
        TextView tv_totalMetalPrice = findViewById(R.id.tv_totalMetalPrice);
        TextView tv_Total = findViewById(R.id.tv_Total);

        TextView tv_glass = findViewById(R.id.tv_glass);
        TextView tv_plastic = findViewById(R.id.tv_plastic);
        TextView tv_paper = findViewById(R.id.tv_paper);
        TextView tv_metal = findViewById(R.id.tv_metal);


        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String binName = intent.getStringExtra("binName");
        String glass = intent.getStringExtra("glass");
        String plastic = intent.getStringExtra("plastic");
        String paper = intent.getStringExtra("paper");
        String metal = intent.getStringExtra("metal");

        float glassW = Float.parseFloat(glass);
        float plasticW = Float.parseFloat(plastic);
        float paperW = Float.parseFloat(paper);
        float metalW = Float.parseFloat(metal);

        float glassTP = glassW * 50;
        float plasticTP = plasticW * 50;
        float paperTP = paperW * 10;
        float metalTP = metalW * 500;
        float TotalPrice = glassTP + glassTP + paperTP + metalTP;

        String str_glassTP = Float.toString(glassTP);
        String str_plasticTP = Float.toString(plasticTP);
        String str_paperTP = Float.toString(paperTP);
        String str_metalTP = Float.toString(metalTP);
        String str_TotalPrice = Float.toString(TotalPrice);

        String contents = glass + "," + str_glassTP + "," + plastic + "," + str_plasticTP + "," +
                paper + "," + str_paperTP + "," + metal + ',' + str_metalTP + "," + str_TotalPrice; // sellBoard에 전송할 contents 내용


        String Date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")); // 현재 날짜



        tv_glass.setText(glass +"kg");
        tv_plastic.setText(plastic + "kg");
        tv_paper.setText(paper + "kg");
        tv_metal.setText(metal + "kg");

        tv_totalGlassPrice.setText(str_glassTP + "원");
        tv_totalPlasticPrice.setText(str_plasticTP + "원");
        tv_totalPaperPrice.setText(str_paperTP + "원");
        tv_totalMetalPrice.setText(str_metalTP + "원");
        tv_Total.setText(str_TotalPrice + "원");


        btn_sellContinue = findViewById(R.id.btn_sellContinue); // 판매하기
        btn_cancel = findViewById(R.id.btn_cancel); // 목록


        btn_sellContinue.setOnClickListener(new View.OnClickListener() {    // 판매하기
            @Override
            public void onClick(View view) {


                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {

                                Toast.makeText(getApplicationContext(),"등록에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellActivity.this, ListActivity.class);
                                intent.putExtra("binName",binName);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);
                                intent.putExtra("glass",glass);
                                intent.putExtra("plastic",plastic);
                                intent.putExtra("paper",paper);
                                intent.putExtra("metal",metal);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(),"등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                postSellBoardRequest postsellboardRequest = new postSellBoardRequest(userID, binName, contents, Date, responseListner);
                RequestQueue queue = Volley.newRequestQueue(SellActivity.this);
                queue.add(postsellboardRequest);

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