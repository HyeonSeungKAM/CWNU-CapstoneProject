package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class SellActivity extends AppCompatActivity {

    private Button btn_sellContinue, btn_cancel; // 판매하기, 취소

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        TextView tv_totalGlassPrice = findViewById(R.id.tv_totalGlassPrice);
        TextView tv_totalPlasticPrice = findViewById(R.id.tv_totalPlasticPrice);
        TextView tv_totalPaperPrice = findViewById(R.id.tv_totalPaperPrice);
        TextView tv_totalMetalPrice = findViewById(R.id.tv_totalMetalPrice);

        Intent sellActivityintent = getIntent();
        String glass = sellActivityintent.getStringExtra("glass");
        String plastic = sellActivityintent.getStringExtra("plastic");
        String paper = sellActivityintent.getStringExtra("paper");
        String metal = sellActivityintent.getStringExtra("metal");

        tv_totalGlassPrice.setText(glass);
        tv_totalPlasticPrice.setText(plastic);
        tv_totalPaperPrice.setText(paper);
        tv_totalMetalPrice.setText(metal);

        btn_sellContinue = findViewById(R.id.btn_sellContinue); // 판매하기
        btn_cancel = findViewById(R.id.btn_cancel); // 목록


        btn_sellContinue.setOnClickListener(new View.OnClickListener() {    // 판매하기
            @Override
            public void onClick(View view) {
                Intent sellContinue_intent = new Intent(SellActivity.this, ListActivity.class);
                startActivity(sellContinue_intent);

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