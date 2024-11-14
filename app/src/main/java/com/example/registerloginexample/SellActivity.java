package com.example.registerloginexample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
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
    private String kind, userID, userName, binName, binLoc,
                glass, plastic, paper, metal, glass_full, plastic_full,
            paper_full ,metal_full;

    private float glassW, glassTP, plasticW, plasticTP, paperW, paperTP, metalW, metalTP;


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

        TextView tv_binName = findViewById(R.id.tv_binName);
        TextView tv_binLoc = findViewById(R.id.tv_binLoc);

        TableRow glass_row = findViewById(R.id.glass_row);
        TableRow plastic_row = findViewById(R.id.plastic_row);
        TableRow paper_row = findViewById(R.id.paper_row);
        TableRow metal_row = findViewById(R.id.metal_row);



        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        binName = intent.getStringExtra("binName");
        binLoc = intent.getStringExtra("binLoc");
        glass = intent.getStringExtra("glass");
        plastic = intent.getStringExtra("plastic");
        paper = intent.getStringExtra("paper");
        metal = intent.getStringExtra("metal");
        glass_full = intent.getStringExtra("glass_full");
        plastic_full = intent.getStringExtra("plastic_full");
        paper_full = intent.getStringExtra("paper_full");
        metal_full = intent.getStringExtra("metal_full");



        if (glass_full.equals("1")) {
            glassW = Float.parseFloat(glass);
            glassTP = glassW * 10;
            glass_row.setVisibility(View.VISIBLE);

        } else {
            glass = "0";
            glassTP = 0;
            glass_row.setVisibility(View.GONE);

        }

        if (plastic_full.equals("1")) {
            plasticW = Float.parseFloat(plastic);
            plasticTP = plasticW * 50;
            plastic_row.setVisibility(View.VISIBLE);

        } else {
            plastic = "0";
            plasticTP = 0;
            plastic_row.setVisibility(View.GONE);
        }

        if (paper_full.equals("1")) {
            paperW = Float.parseFloat(paper);
            paperTP = paperW * 50;
            paper_row.setVisibility(View.VISIBLE);

        } else {
            paper = "0";
            paperTP = 0;
            paper_row.setVisibility(View.GONE);
        }

        if (metal_full.equals("1")) {
            metalW = Float.parseFloat(metal);
            metalTP = metalW * 500;
            metal_row.setVisibility(View.VISIBLE);

        } else {
            metal = "0";
            metalTP = 0;
            metal_row.setVisibility(View.GONE);
        }

        float TotalPrice = glassTP + plasticTP + paperTP + metalTP;

        String str_glassTP = Float.toString(glassTP);
        String str_plasticTP = Float.toString(plasticTP);
        String str_paperTP = Float.toString(paperTP);
        String str_metalTP = Float.toString(metalTP);
        String str_TotalPrice = Float.toString(TotalPrice);


/** contents 값 구성 :
 유리병 무게(0), 유리병 가격(1), 플라스틱 무게(2), 플라스틱 가격(3), 종이 무게(4), 종이 가격(5), 고철 무게(6), 고철 가격(7) **/

        String contents = glass + "," + str_glassTP + "," + plastic + "," + str_plasticTP + "," +
                paper + "," + str_paperTP + "," + metal + ',' + str_metalTP + "," + str_TotalPrice; // sellBoard에 전송할 contents 내용


        String Date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")); // 현재 날짜



        tv_binName.setText(binName);
        tv_binLoc.setText(binLoc);

        tv_glass.setText(Float.toString(glassW));
        tv_plastic.setText(Float.toString(plasticW));
        tv_paper.setText(Float.toString(paperW));
        tv_metal.setText(Float.toString(metalW));

        tv_totalGlassPrice.setText(str_glassTP);
        tv_totalPlasticPrice.setText(str_plasticTP);
        tv_totalPaperPrice.setText(str_paperTP);
        tv_totalMetalPrice.setText(str_metalTP);
        tv_Total.setText(str_TotalPrice + "원");


        btn_sellContinue = findViewById(R.id.btn_sellContinue); // 판매하기
        btn_cancel = findViewById(R.id.btn_cancel); // 목록


        String finalPaper = paper;
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
                                intent.putExtra("kind",kind);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);
                                intent.putExtra("binName",binName);
                                intent.putExtra("binLoc", binLoc);
                                intent.putExtra("glass",glass);
                                intent.putExtra("plastic",plastic);
                                intent.putExtra("paper", paper);
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
                postSellBoardRequest postsellboardRequest = new postSellBoardRequest(userID, binName, binLoc, contents, Date, responseListner);
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