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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BuyActivity extends AppCompatActivity {

    private Button btn_buy, btn_cancel; // 판매하기, 취소

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        TextView tv_glassW = (TextView) findViewById(R.id.tv_glassW);
        TextView tv_plasticW = (TextView) findViewById(R.id.tv_plasticW);
        TextView tv_paperW = (TextView) findViewById(R.id.tv_paperW);
        TextView tv_metalW = (TextView) findViewById(R.id.tv_metalW);

        TextView tv_totalGlassPrice = (TextView) findViewById(R.id.tv_totalGlassPrice);
        TextView tv_totalPlasticPrice = (TextView) findViewById(R.id.tv_totalPlasticPrice);
        TextView tv_totalPaperPrice = (TextView) findViewById(R.id.tv_totalPaperPrice);
        TextView tv_totalMetalPrice = (TextView) findViewById(R.id.tv_totalMetalPrice);
        TextView tv_Total = (TextView) findViewById(R.id.tv_Total);

        TextView tv_Total2 = (TextView) findViewById(R.id.tv_Total2);


        // 판매자 정보란
        TextView tv_seller_userIDName = (TextView) findViewById(R.id.tv_seller_userIDName);
        TextView tv_seller_phoneNum = (TextView) findViewById(R.id.tv_seller_phoneNum);
        TextView tv_seller_account = (TextView) findViewById(R.id.tv_seller_account);
        TextView tv_seller_address = (TextView) findViewById(R.id.tv_seller_address);



        TableRow glass_row = findViewById(R.id.glass_row);
        TableRow plastic_row = findViewById(R.id.plastic_row);
        TableRow paper_row = findViewById(R.id.paper_row);
        TableRow metal_row = findViewById(R.id.metal_row);


        Intent intent = getIntent();
        // 로그인한 유저 정보
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");

        String seller_userID = intent.getExtras().getString("seller_userID");
        String seller_userName = intent.getExtras().getString("seller_userName");
        String seller_address = intent.getExtras().getString("seller_address");
        String seller_account = intent.getExtras().getString("seller_account");
        String seller_phoneNum = intent.getExtras().getString("seller_phoneNum");
        String board_contents = intent.getExtras().getString("board_contents");

        // 쓰레기 정보표 구간 =========================================================
        tv_glassW.setText(board_contents.split(",")[0]);
        tv_totalGlassPrice.setText(board_contents.split(",")[1]);

        tv_plasticW.setText(board_contents.split(",")[2]);
        tv_totalPlasticPrice.setText(board_contents.split(",")[3]);

        tv_paperW.setText(board_contents.split(",")[4]);
        tv_totalPaperPrice.setText(board_contents.split(",")[5]);

        tv_metalW.setText(board_contents.split(",")[6]);
        tv_totalMetalPrice.setText(board_contents.split(",")[7]);

        tv_Total.setText(board_contents.split(",")[8] + " 원");

        tv_Total2.setText(board_contents.split(",")[8] + " 원");

        if (board_contents.split(",")[0].equals("0")) {
            glass_row.setVisibility(View.GONE);
        } else {
            glass_row.setVisibility(View.VISIBLE);
        }

        if (board_contents.split(",")[2].equals("0")) {
            plastic_row.setVisibility(View.GONE);
        } else {
            plastic_row.setVisibility(View.VISIBLE);
        }

        if (board_contents.split(",")[4].equals("0")) {
            paper_row.setVisibility(View.GONE);
        } else {
            paper_row.setVisibility(View.VISIBLE);
        }

        if (board_contents.split(",")[6].equals("0")) {
            metal_row.setVisibility(View.GONE);
        } else {
            metal_row.setVisibility(View.VISIBLE);
        }



        // 판매자 정보표 구간 =========================================================

        String seller_userIDName = seller_userID + "(" + seller_userName + ")";

        tv_seller_userIDName.setText(seller_userID + "(" + seller_userName + ")");
        tv_seller_phoneNum.setText(seller_phoneNum);
        tv_seller_account.setText(seller_account);
        tv_seller_address.setText(seller_address);



        String purchase_date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")); // 현재 날짜

        String contents = board_contents;
        String total_payment = board_contents.split(",")[8];

        btn_buy = findViewById(R.id.btn_buy);
        btn_cancel = findViewById(R.id.btn_cancel);


        btn_buy.setOnClickListener(new View.OnClickListener() { // 결제하기
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListner = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            System.out.println(success);
                            if (success) {
                                Toast.makeText(getApplicationContext(),"구매 완료.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BuyActivity.this, ListActivity.class);
                                intent.putExtra("kind",kind);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);

                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(),"구매에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                PurchaseUpdateRequest purchaseUpdateRequest = new PurchaseUpdateRequest(seller_userID,  userID, purchase_date,board_contents,
                        total_payment, seller_userIDName, seller_address, seller_phoneNum, responseListner);
                RequestQueue queue = Volley.newRequestQueue(BuyActivity.this);
                queue.add(purchaseUpdateRequest);

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