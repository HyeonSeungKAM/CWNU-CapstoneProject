package com.example.registerloginexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class SPageActivity extends AppCompatActivity {


    private Button btn_list, btn_buy; // 판매하기, 취소
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spage);

        TextView tv_b_userName = (TextView) findViewById(R.id.tv_b_userName);
        TextView tv_b_userID = (TextView) findViewById(R.id.tv_b_userID);
        TextView tv_b_binName = (TextView) findViewById(R.id.tv_b_binName);
        TextView tv_b_binLoc = (TextView) findViewById(R.id.tv_b_binLoc);
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

        TableRow glass_row = findViewById(R.id.glass_row);
        TableRow plastic_row = findViewById(R.id.plastic_row);
        TableRow paper_row = findViewById(R.id.paper_row);
        TableRow metal_row = findViewById(R.id.metal_row);


        Intent intent = getIntent();
        String board_userID = intent.getExtras().getString("s_board_userID");
        String board_userName = intent.getExtras().getString("s_board_userName");
        String board_binName = intent.getExtras().getString("s_board_binName");
        String board_binLoc = intent.getExtras().getString("s_board_binLoc");
        String board_Date = intent.getExtras().getString("s_board_date");
        String board_contents = intent.getExtras().getString("s_board_contents");

        // 로그인한 유저 정보
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String p_type_kr = intent.getStringExtra("p_type_kr");


        tv_b_userName.setText(board_userName);
        tv_b_userID.setText("(" + board_userID + ")");
        tv_b_Date.setText(board_Date);
        tv_b_binName.setText(board_binName);
        tv_b_binLoc.setText(board_binLoc);

        tv_glassW.setText(board_contents.split(",")[0]);
        tv_totalGlassPrice.setText(board_contents.split(",")[1]);

        tv_plasticW.setText(board_contents.split(",")[2]);
        tv_totalPlasticPrice.setText(board_contents.split(",")[3]);

        tv_paperW.setText(board_contents.split(",")[4]);
        tv_totalPaperPrice.setText(board_contents.split(",")[5]);

        tv_metalW.setText(board_contents.split(",")[6]);
        tv_totalMetalPrice.setText(board_contents.split(",")[7]);

        tv_Total.setText(board_contents.split(",")[8]);


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


        btn_buy = findViewById(R.id.btn_buy);
        btn_list = findViewById(R.id.btn_list);

        if (kind.equals("user")) {
            btn_buy.setVisibility(View.GONE);
            btn_list.setVisibility(View.VISIBLE);

        } else if (kind.equals("buyer")) {
            btn_buy.setVisibility(View.VISIBLE);
            btn_list.setVisibility(View.VISIBLE);

            if (p_type_kr.equals("전체")) {
                btn_buy.setOnClickListener(new View.OnClickListener() {    // 취소
                    @Override
                    public void onClick(View view) {
                        String option = "yes";

                        Response.Listener<String> responseListner = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {

                                        String seller_userID = jsonObject.getString("seller_userID");
                                        String seller_userName = jsonObject.getString("seller_userName");
                                        String seller_address = jsonObject.getString("seller_address");
                                        String seller_account = jsonObject.getString("seller_account");
                                        String seller_phoneNum = jsonObject.getString("seller_phoneNum");

                                        Toast.makeText(getApplicationContext(),"결제 페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SPageActivity.this, BuyActivity.class);

                                        intent.putExtra("kind",kind);
                                        intent.putExtra("userID",userID);
                                        intent.putExtra("userName",userName);
                                        intent.putExtra("p_type_kr",p_type_kr);
                                        intent.putExtra("option",option);

                                        intent.putExtra("seller_userID",seller_userID);
                                        intent.putExtra("seller_userName",seller_userName);
                                        intent.putExtra("seller_address",seller_address);
                                        intent.putExtra("seller_account",seller_account);
                                        intent.putExtra("seller_phoneNum",seller_phoneNum);
                                        intent.putExtra("board_contents",board_contents);

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
                        BuyInfoRequest buyInfoRequest = new BuyInfoRequest(board_userID, responseListner);
                        RequestQueue queue = Volley.newRequestQueue(SPageActivity.this);
                        queue.add(buyInfoRequest);

                    }
                });

            } else {
                // 구매하기
                btn_buy.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        showDialog();
                    }

                    void showDialog() {

                        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(SPageActivity.this)


                                .setMessage(p_type_kr + " 외 다른 품목도 구매하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String option = "yes";
                                        Response.Listener<String> responseListner = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if (success) {

                                                        String seller_userID = jsonObject.getString("seller_userID");
                                                        String seller_userName = jsonObject.getString("seller_userName");
                                                        String seller_address = jsonObject.getString("seller_address");
                                                        String seller_account = jsonObject.getString("seller_account");
                                                        String seller_phoneNum = jsonObject.getString("seller_phoneNum");

                                                        Toast.makeText(getApplicationContext(),"결제 페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SPageActivity.this, BuyActivity.class);

                                                        intent.putExtra("kind",kind);
                                                        intent.putExtra("userID",userID);
                                                        intent.putExtra("userName",userName);
                                                        intent.putExtra("p_type_kr",p_type_kr);
                                                        intent.putExtra("option",option);

                                                        intent.putExtra("seller_userID",seller_userID);
                                                        intent.putExtra("seller_userName",seller_userName);
                                                        intent.putExtra("seller_address",seller_address);
                                                        intent.putExtra("seller_account",seller_account);
                                                        intent.putExtra("seller_phoneNum",seller_phoneNum);
                                                        intent.putExtra("board_contents",board_contents);

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
                                        BuyInfoRequest buyInfoRequest = new BuyInfoRequest(board_userID, responseListner);
                                        RequestQueue queue = Volley.newRequestQueue(SPageActivity.this);
                                        queue.add(buyInfoRequest);
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String option = "no";

                                        Response.Listener<String> responseListner = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if (success) {

                                                        String seller_userID = jsonObject.getString("seller_userID");
                                                        String seller_userName = jsonObject.getString("seller_userName");
                                                        String seller_address = jsonObject.getString("seller_address");
                                                        String seller_account = jsonObject.getString("seller_account");
                                                        String seller_phoneNum = jsonObject.getString("seller_phoneNum");

                                                        Toast.makeText(getApplicationContext(),"결제 페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SPageActivity.this, BuyActivity.class);

                                                        intent.putExtra("kind",kind);
                                                        intent.putExtra("userID",userID);
                                                        intent.putExtra("userName",userName);
                                                        intent.putExtra("p_type_kr",p_type_kr);
                                                        intent.putExtra("option",option);

                                                        intent.putExtra("seller_userID",seller_userID);
                                                        intent.putExtra("seller_userName",seller_userName);
                                                        intent.putExtra("seller_address",seller_address);
                                                        intent.putExtra("seller_account",seller_account);
                                                        intent.putExtra("seller_phoneNum",seller_phoneNum);
                                                        intent.putExtra("board_contents",board_contents);

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
                                        BuyInfoRequest buyInfoRequest = new BuyInfoRequest(board_userID, responseListner);
                                        RequestQueue queue = Volley.newRequestQueue(SPageActivity.this);
                                        queue.add(buyInfoRequest);
                                    }
                                });
                        AlertDialog msgDig = msgBuilder.create();
                        msgDig.show();
                    }
                });
            }

        }







        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SPageActivity.this, ListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);

                startActivity(intent);
                finish();
            }
        });

    }
}