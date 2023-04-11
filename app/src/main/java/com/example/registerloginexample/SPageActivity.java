package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class SPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spagetest);

        TextView tv_b_userID = (TextView) findViewById(R.id.tv_b_userID);
        TextView tv_b_Date = (TextView) findViewById(R.id.tv_b_Date);

        TextView tv_glassW = (TextView) findViewById(R.id.tv_glassW);
        TextView tv_plasticW = (TextView) findViewById(R.id.tv_plasticW);
        TextView tv_paperW = (TextView) findViewById(R.id.tv_paperW);
        TextView tv_metalW = (TextView) findViewById(R.id.tv_metalW);

        TextView tv_totalGlassPrice = (TextView) findViewById(R.id.tv_totalGlassPrice);
        TextView tv_totalPlasticPrice = (TextView) findViewById(R.id.tv_totalPlasticPrice);
        TextView tv_totalPaperPrice = (TextView) findViewById(R.id.tv_totalPaperPrice);
        TextView tv_totalMetalPrice = (TextView) findViewById(R.id.tv_totalMetalPrice);
        TextView tv_Total = (TextView) findViewById(R.id.tv_Total);

        Intent intent = getIntent();
        String board_userID = intent.getExtras().getString("userID");
        String board_contents = intent.getExtras().getString("contents");
        String board_Date = intent.getExtras().getString("Date");


        tv_b_userID.setText(board_userID);
        tv_b_Date.setText(board_Date);

        tv_glassW.setText(board_contents.split(",")[0]);
        tv_totalGlassPrice.setText(board_contents.split(",")[1]);

        tv_plasticW.setText(board_contents.split(",")[2]);
        tv_totalPlasticPrice.setText(board_contents.split(",")[3]);

        tv_paperW.setText(board_contents.split(",")[4]);
        tv_totalPaperPrice.setText(board_contents.split(",")[5]);

        tv_metalW.setText(board_contents.split(",")[6]);
        tv_totalMetalPrice.setText(board_contents.split(",")[7]);

        tv_Total.setText(board_contents.split(",")[8]);

        Button btn_list = findViewById(R.id.btn_list);
        btn_list.setOnClickListener(new View.OnClickListener() {    // 취소
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
