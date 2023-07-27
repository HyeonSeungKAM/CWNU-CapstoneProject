package com.example.registerloginexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BuyerPurchableListActivity extends AppCompatActivity {

    private static String kind;
    private static String userID;
    private static String userName;
    private static String p_type;

    private RecyclerView recyclerView, sellBoard_Recyclerview;

    private Button btn_purchasedlist, btn_main; // 판매하기, 취소


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyerpurchablelistactivity);

        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        p_type = intent.getStringExtra("p_type");

        // 구매 가능한 재활용품 목록 불러오기
        sellBoard_Recyclerview = findViewById(R.id.SellBoard_Recyclerview);
        LinearLayoutManager sellBoard_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        sellBoard_Recyclerview.setLayoutManager(sellBoard_layoutManager);

        String sellboard_url = "http://gamhs44.ivyro.net/purchasablelist.php?p_type="+p_type; // PHP 파일의 URL
        new GetSellboardDataTask().execute(sellboard_url);


        // 메인 버튼
        btn_main = findViewById(R.id.btn_main);
        btn_main.setOnClickListener(new View.OnClickListener() {    // 메인
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BuyerPurchableListActivity.this, BuyerMainActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID", userID);
                intent.putExtra("userName", userName);

                startActivity(intent);
            }
        });

    }


    // 구매 가능한 재활용품 목록 불러오기 클래스
        private class GetSellboardDataTask extends AsyncTask<String, Void, List<BuyerPurchableListActivity.SellBoard_DataItem>> {
            @Override
            protected List<BuyerPurchableListActivity.SellBoard_DataItem> doInBackground(String... params) {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject  jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("webnautes");
                    List<BuyerPurchableListActivity.SellBoard_DataItem> dataList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject itemObject = jsonArray.getJSONObject(i);
                        String s_board_ID = itemObject.getString("id");
                        String s_board_userID = itemObject.getString("userID");
                        String s_board_userName = itemObject.getString("userName");
                        String s_board_binName = itemObject.getString("binName");
                        String s_board_binLoc = itemObject.getString("binLoc");
                        String s_board_contents = itemObject.getString("contents");
                        String s_board_date = itemObject.getString("Date");

                        String s_board_glassW = s_board_contents.split(",")[0];
                        String s_board_plasticW = s_board_contents.split(",")[2];
                        String s_board_paperW = s_board_contents.split(",")[4];
                        String s_board_metalW = s_board_contents.split(",")[6];

                        BuyerPurchableListActivity.SellBoard_DataItem sellBoard_dataItem = new BuyerPurchableListActivity.SellBoard_DataItem(s_board_ID,s_board_userID, s_board_userName,
                                s_board_binName, s_board_binLoc, s_board_contents, s_board_date, s_board_glassW,
                                s_board_plasticW, s_board_paperW, s_board_metalW);

                        dataList.add(sellBoard_dataItem);
                    }
                    return dataList;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<BuyerPurchableListActivity.SellBoard_DataItem> result2) {
                if (result2 != null) {
                    BuyerPurchableListActivity.Sellborad_MyAdapter sellboard_adapter = new BuyerPurchableListActivity.Sellborad_MyAdapter(result2);
                    sellBoard_Recyclerview.setAdapter(sellboard_adapter);
                }
            }
        }

        private static class Sellborad_MyAdapter extends RecyclerView.Adapter<BuyerPurchableListActivity.Sellborad_MyAdapter.ViewHolder> {

            private List<BuyerPurchableListActivity.SellBoard_DataItem> sellboard_data;

            public interface OnItemClickListener {
                void onItemClicked(int position, String sellboard_data);
            }

            private BuyerPurchableListActivity.Sellborad_MyAdapter.OnItemClickListener itemClickListener;

            public void setOnItemClickListener(BuyerPurchableListActivity.Sellborad_MyAdapter.OnItemClickListener listener) {
                itemClickListener = listener;
            }

            public Sellborad_MyAdapter (List<BuyerPurchableListActivity.SellBoard_DataItem> sellboard_data) {
                this.sellboard_data = sellboard_data;
            }

            public class ViewHolder extends RecyclerView.ViewHolder {
                public TextView textView_list_id, textView_list_userid, textView_list_binname;
                public TextView glass_weight, plastic_weight, paper_weight, metal_weight;
                public LinearLayout glass_row, plastic_row, paper_row, metal_row;
                public ViewHolder(View itemView) {
                    super(itemView);
                    textView_list_id = itemView.findViewById(R.id.textView_list_id);

                    glass_weight = itemView.findViewById(R.id.glass_weight);
                    plastic_weight = itemView.findViewById(R.id.plastic_weight);
                    paper_weight = itemView.findViewById(R.id.paper_weight);
                    metal_weight = itemView.findViewById(R.id.metal_weight);

                    glass_row = itemView.findViewById(R.id.glass_row);
                    plastic_row = itemView.findViewById(R.id.plastic_row);
                    paper_row = itemView.findViewById(R.id.paper_row);
                    metal_row = itemView.findViewById(R.id.metal_row);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos = getAbsoluteAdapterPosition();
                            if(pos != RecyclerView.NO_POSITION) {
                                BuyerPurchableListActivity.SellBoard_DataItem item = sellboard_data.get(pos);
                                String s_board_userID = item.getS_board_userID();
                                String s_board_userName = item.getS_board_userName();
                                String s_board_binName = item.getS_board_binName();
                                String s_board_binLoc = item.getS_board_binLoc();
                                String s_board_contents = item.getS_board_contents();
                                String s_board_date = item.getS_board_date();

                                Intent intent = new Intent(itemView.getContext(), SPageActivity.class);intent.putExtra("s_board_userName", s_board_userID);
                                intent.putExtra("s_board_userID", s_board_userID);
                                intent.putExtra("s_board_userName", s_board_userName);
                                intent.putExtra("s_board_binName", s_board_binName);
                                intent.putExtra("s_board_binLoc", s_board_binLoc);
                                intent.putExtra("s_board_date", s_board_date);
                                intent.putExtra("s_board_contents",s_board_contents);

                                // 로그인한 유저 정보들
                                intent.putExtra("kind",kind);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);

                                itemView.getContext().startActivity(intent);
                            }
                        }
                    });
                }
            }

            @Override
            public BuyerPurchableListActivity.Sellborad_MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list, parent, false);

                BuyerPurchableListActivity.Sellborad_MyAdapter.ViewHolder viewHolder = new BuyerPurchableListActivity.Sellborad_MyAdapter.ViewHolder(view);

                return new BuyerPurchableListActivity.Sellborad_MyAdapter.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(BuyerPurchableListActivity.Sellborad_MyAdapter.ViewHolder holder, int position) {
                BuyerPurchableListActivity.SellBoard_DataItem item = sellboard_data.get(position);
                holder.textView_list_id.setText(item.getS_board_ID());
                holder.textView_list_userid.setText(item.getS_board_userID());
                holder.textView_list_binname.setText(item.getS_board_binName());
                holder.glass_weight.setText(item.getS_board_glassW());
                holder.plastic_weight.setText(item.getS_board_plasticW());
                holder.paper_weight.setText(item.getS_board_paperW());
                holder.metal_weight.setText(item.getS_board_metalW());

                holder.glass_row.setVisibility(View.VISIBLE);
                holder.plastic_row.setVisibility(View.VISIBLE);
                holder.paper_row.setVisibility(View.VISIBLE);
                holder.metal_row.setVisibility(View.VISIBLE);

                if(item.getS_board_glassW().equals("0")) {
                    holder.glass_row.setVisibility(View.INVISIBLE);
                }
                if(item.getS_board_plasticW().equals("0")) {
                    holder.plastic_row.setVisibility(View.INVISIBLE);
                }
                if(item.getS_board_paperW().equals("0")) {
                    holder.paper_row.setVisibility(View.INVISIBLE);
                }
                if(item.getS_board_metalW().equals("0")) {
                    holder.metal_row.setVisibility(View.INVISIBLE);
                }
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setMargins(5, 0, 5, 0); // 왼쪽과 오른쪽 마진을 16dp로 설정
                holder.itemView.setLayoutParams(layoutParams);
            }



            @Override
            public int getItemCount()
            {
                return sellboard_data.size();
            }
        }

        private static class SellBoard_DataItem {
            private String s_board_ID, s_board_userID, s_board_userName, s_board_binName, s_board_binLoc, s_board_contents, s_board_date, s_board_glassW, s_board_plasticW , s_board_paperW , s_board_metalW;


            public SellBoard_DataItem(String s_board_ID, String s_board_userID, String s_board_userName, String s_board_binName, String s_board_binLoc, String s_board_contents, String s_board_date,
                                      String s_board_glassW, String s_board_plasticW, String s_board_paperW, String s_board_metalW ) {

                this.s_board_ID = s_board_ID;
                this.s_board_userID = s_board_userID;
                this.s_board_userName = s_board_userName;
                this.s_board_binName = s_board_binName;
                this.s_board_binLoc = s_board_binLoc;
                this.s_board_contents = s_board_contents;
                this.s_board_date = s_board_date;
                this.s_board_glassW = s_board_glassW;
                this.s_board_plasticW = s_board_plasticW;
                this.s_board_paperW = s_board_paperW;
                this.s_board_metalW = s_board_metalW;
            }
            public String getS_board_ID() {return s_board_ID;}
            public String getS_board_userID() {return s_board_userID;}
            public String getS_board_userName() {return s_board_userName;}
            public String getS_board_binName() {return s_board_binName;}
            public String getS_board_binLoc() {return s_board_binLoc;}
            public String getS_board_contents() {return s_board_contents;}
            public String getS_board_date() {return s_board_date;}
            public String getS_board_glassW() {return s_board_glassW;}
            public String getS_board_plasticW() {return s_board_plasticW;}
            public String getS_board_paperW() {return s_board_paperW;}
            public String getS_board_metalW() {return s_board_metalW;}


        }

}

