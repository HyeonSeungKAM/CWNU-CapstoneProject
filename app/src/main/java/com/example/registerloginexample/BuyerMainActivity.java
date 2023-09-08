package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


public class BuyerMainActivity extends AppCompatActivity {

    private Button btn_logout, btn_purchasedlist, btn_list, glass_btn, plastic_btn, paper_btn, metal_btn;
    private String p_type_kr;

    // 서버에서 가져온 내용 보여주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buyer);

        TextView tv_id = findViewById(R.id.tv_id);
        TextView tv_name = findViewById(R.id.tv_name);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");

        tv_id.setText(userID);
        tv_name.setText(userName);


        // 로그아웃 버튼
        btn_logout = findViewById(R.id.btn_logout);// 목록
        btn_logout.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyerMainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // 구매내역 버튼
        btn_purchasedlist = findViewById(R.id.btn_purchasedlist); // 판매하기
        btn_purchasedlist.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyerMainActivity.this, PurchasedListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);

                startActivity(intent);

            }
        });

        // 목록 버튼
        btn_list = findViewById(R.id.btn_list); // 목록
        btn_list.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                p_type_kr = "전체";
                Intent intent = new Intent(BuyerMainActivity.this, BuyerPurchableListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("p_type_kr",p_type_kr);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                startActivity(intent);

            }
        });

        //유리병 버튼
        glass_btn = findViewById(R.id.glass_btn);
        glass_btn.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                p_type_kr = "유리";
                Intent intent = new Intent(BuyerMainActivity.this, BuyerPurchableListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("p_type_kr",p_type_kr);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        //플라스틱 버튼
        plastic_btn = findViewById(R.id.plastic_btn);
        plastic_btn.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                p_type_kr = "플라스틱";
                Intent intent = new Intent(BuyerMainActivity.this, BuyerPurchableListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("p_type_kr",p_type_kr);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        //종이 버튼
        paper_btn = findViewById(R.id.paper_btn);
        paper_btn.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                p_type_kr = "p_type_kr";
                Intent intent = new Intent(BuyerMainActivity.this, BuyerPurchableListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("p_type_kr",p_type_kr);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        //고철 버튼
        metal_btn = findViewById(R.id.metal_btn);
        metal_btn.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                p_type_kr = "고철";
                Intent intent = new Intent(BuyerMainActivity.this, BuyerPurchableListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("p_type_kr",p_type_kr);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });
    }

}