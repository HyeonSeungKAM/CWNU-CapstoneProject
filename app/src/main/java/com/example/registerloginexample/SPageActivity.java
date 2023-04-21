package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;



public class SPageActivity extends AppCompatActivity {


    private Button btn_list, btn_buy; // 판매하기, 취소
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
        String board_userID = intent.getExtras().getString("board_userID");
        String board_contents = intent.getExtras().getString("board_contents");
        String board_Date = intent.getExtras().getString("board_Date");

        // 로그인한 유저 정보
        String kind = intent.getStringExtra("kind");
        String binName = intent.getStringExtra("binName");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String glass = intent.getStringExtra("glass");
        String plastic = intent.getStringExtra("plastic");
        String paper = intent.getStringExtra("paper");
        String metal = intent.getStringExtra("metal");


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



        btn_buy = findViewById(R.id.btn_buy);
        btn_list = findViewById(R.id.btn_list);

        if (kind.equals("user")) {
            btn_buy.setVisibility(View.GONE);
            btn_list.setVisibility(View.VISIBLE);

        } else if (kind.equals("buyer")) {
            btn_buy.setVisibility(View.VISIBLE);
            btn_list.setVisibility(View.VISIBLE);
        }

        btn_list.setOnClickListener(new View.OnClickListener() {    // 취소
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SPageActivity.this, ListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("binName",binName);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("glass",glass);
                intent.putExtra("plastic",plastic);
                intent.putExtra("paper",paper);
                intent.putExtra("metal",metal);

                startActivity(intent);
                finish();
            }
        });

    }
}
