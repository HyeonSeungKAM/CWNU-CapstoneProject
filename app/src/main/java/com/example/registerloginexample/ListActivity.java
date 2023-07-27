package com.example.registerloginexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // ------------- DB로 부터 데이터 불러오기 -----------------
    private static String TAG = "php_ListAcitivity";

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_USERID = "userID";
    private static final String TAG_BINNAME = "binName";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_GLASSW = "glassW";
    private static final String TAG_PLASTICW = "plasticW";
    private static final String TAG_PAPERW = "paperW";
    private static final String TAG_METALW = "metalW";

    private static String kind;
    private static String userID;
    private static String userName;


    private RecyclerView recyclerView, sellBoard_Recyclerview;


    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;

    String mJsonString;
// ------------- ----------------------------------


    private Button btn_purchasedlist, btn_sell2, btn_main; // 판매하기, 취소

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        
        // 랭크 정보 리사이클러뷰
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        String url = "http://gamhs44.ivyro.net/rank.php"; // PHP 파일의 URL
        new GetRankDataTask().execute(url);
        
        
        // 셀보드 정보 리사이클러뷰

        sellBoard_Recyclerview = findViewById(R.id.SellBoard_Recyclerview);
        LinearLayoutManager sellBoard_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        sellBoard_Recyclerview.setLayoutManager(sellBoard_layoutManager);

        String sellboard_url = "http://gamhs44.ivyro.net/sellboardlist.php"; // PHP 파일의 URL
        new GetSellboardDataTask().execute(sellboard_url);
        

        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        String address = intent.getStringExtra("address");
        String binName = intent.getStringExtra("binName");
        String binLoc = intent.getStringExtra("binLoc");
        String glass = intent.getStringExtra("glass");
        String plastic = intent.getStringExtra("plastic");
        String paper = intent.getStringExtra("paper");
        String metal = intent.getStringExtra("metal");
        String glass_full = intent.getStringExtra("glass_full");
        String plastic_full = intent.getStringExtra("plastic_full");
        String metal_full = intent.getStringExtra("metal_full");
        String paper_full = intent.getStringExtra("paper_full");


// ------------------ 버튼들 -----------------------------------------------
        btn_purchasedlist = findViewById(R.id.btn_purchasedlist);
        btn_sell2 = findViewById(R.id.btn_sell2); // 판매하기
        btn_main = findViewById(R.id.btn_main); // 메인

        if (kind.equals("user")) {
            btn_purchasedlist.setVisibility(View.INVISIBLE);
            btn_sell2.setVisibility(View.VISIBLE);
            btn_main.setVisibility(View.VISIBLE);

        } else if (kind.equals("buyer")) {
            btn_purchasedlist.setVisibility(View.VISIBLE);
            btn_sell2.setVisibility(View.INVISIBLE);
            btn_main.setVisibility(View.VISIBLE);
        }

        btn_purchasedlist.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, PurchasedListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);

                startActivity(intent);

            }
        });


        btn_sell2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 판매하기(올리기)

                Intent intent = new Intent(ListActivity.this, SellActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID", userID);
                intent.putExtra("userName", userName);
                intent.putExtra("address",address);
                intent.putExtra("binName", binName);
                intent.putExtra("binLoc",binLoc);
                intent.putExtra("glass", glass);
                intent.putExtra("plastic", plastic);
                intent.putExtra("paper", paper);
                intent.putExtra("metal", metal);
                startActivity(intent);

            }
        });

        btn_main.setOnClickListener(new View.OnClickListener() {    // 메인
            @Override
            public void onClick(View view) {

                if(kind.equals("buyer")){
                    Intent intent = new Intent(ListActivity.this, BuyerMainActivity.class);
                    intent.putExtra("kind",kind);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userName", userName);

                    startActivity(intent);

                } else if(kind.equals("user")){
                    Intent intent = new Intent(ListActivity.this, MainActivity.class);
                //    intent.putExtra("binName", binName);
                    intent.putExtra("kind",kind);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userName", userName);
                    intent.putExtra("address",address);
               //     intent.putExtra("glass", glass);
                //    intent.putExtra("plastic", plastic);
                //    intent.putExtra("paper", paper);
                //    intent.putExtra("metal", metal);

                    startActivity(intent);
                }

            }
        });
    }
//------------------------- -----------------------------------------------


    // --------------------------- 랭크 데이터 불러오기 -------------------------------------------

    private class GetRankDataTask extends AsyncTask<String, Void, List<DataItem>> {
        @Override
        protected List<DataItem> doInBackground(String... params) {
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
                List<DataItem> dataList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject itemObject = jsonArray.getJSONObject(i);
                    String board_userID = itemObject.getString("userID");
                    String board_userName = itemObject.getString("userName");
                    String board_T_sales = itemObject.getString("T_sales");
                    int rank = i;

                    DataItem dataItem = new DataItem(board_userID, board_userName, board_T_sales, rank);
                    dataList.add(dataItem);
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
        protected void onPostExecute(List<DataItem> result) {
            if (result != null) {
                MyAdapter adapter = new MyAdapter(result);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<DataItem> data;

        public MyAdapter(List<DataItem> data) {
            this.data = data;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_board_userName, tv_board_userID, tv_board_T_sales;
            public ImageView rankImg;
            public ViewHolder(View itemView) {
                super(itemView);
                tv_board_userName = itemView.findViewById(R.id.tv_board_userName);
                tv_board_userID = itemView.findViewById(R.id.tv_board_userID);
                tv_board_T_sales = itemView.findViewById(R.id.tv_board_T_sales);
                rankImg = itemView.findViewById(R.id.imageView);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ranklist, parent, false);

            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            DataItem item = data.get(position);
            holder.tv_board_userName.setText(item.getBoard_userName());
            holder.tv_board_userID.setText(item.getBoard_userID());
            holder.tv_board_T_sales.setText(item.getBoard_T_sales() + " 원");
            holder.rankImg.setImageResource(item.getRank());

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setMargins(5, 0, 5, 0); // 왼쪽과 오른쪽 마진을 16dp로 설정
            holder.itemView.setLayoutParams(layoutParams);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private static class DataItem {
        private String board_userID;
        private int rank;

        private int image[] = {R.drawable.img_first, R.drawable.img_second, R.drawable.img_third};
        private String board_userName;
        private String board_T_sales;

        public DataItem(String board_userID, String board_userName, String board_T_sales, int rank) {
            this.board_userID = board_userID;
            this.board_userName = board_userName;
            this.board_T_sales = board_T_sales;
            this.rank = rank;
        }
        public String getBoard_userID() {
            return board_userID;
        }
        public String getBoard_userName() {
            return board_userName;
        }
        public String getBoard_T_sales() {
            return board_T_sales;
        }
        public int getRank() { return image[rank]; }


    }






    /** contents -- 유리병 무게(0), 유리병 총 가격(1),
     플라스틱 무게(2), 플라스틱 총 가격(3),
     종이 무게(4), 종이 총 가격(5),
     고철 무게(6), 고철 총 가격(7) **/


    // --------------------------- 셀보드 리스트 불러오기 -------------------------------------------

    private class GetSellboardDataTask extends AsyncTask<String, Void, List<SellBoard_DataItem>> {
        @Override
        protected List<SellBoard_DataItem> doInBackground(String... params) {
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
                List<SellBoard_DataItem> dataList = new ArrayList<>();
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

                    SellBoard_DataItem sellBoard_dataItem = new SellBoard_DataItem(s_board_ID,s_board_userID, s_board_userName,
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
        protected void onPostExecute(List<SellBoard_DataItem> result2) {
            if (result2 != null) {
                Sellborad_MyAdapter sellboard_adapter = new Sellborad_MyAdapter(result2);
                sellBoard_Recyclerview.setAdapter(sellboard_adapter);
            }
        }
    }

    private static class Sellborad_MyAdapter extends RecyclerView.Adapter<Sellborad_MyAdapter.ViewHolder> {

        private List<SellBoard_DataItem> sellboard_data;

        public interface OnItemClickListener {
            void onItemClicked(int position, String sellboard_data);
        }

        private OnItemClickListener itemClickListener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            itemClickListener = listener;
        }

        public Sellborad_MyAdapter (List<SellBoard_DataItem> sellboard_data) {
            this.sellboard_data = sellboard_data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_list_id, textView_list_userid, textView_list_binname;
            public TextView glass_weight, plastic_weight, paper_weight, metal_weight;
            public LinearLayout glass_row, plastic_row, paper_row, metal_row;
            public ViewHolder(View itemView) {
                super(itemView);
                textView_list_id = itemView.findViewById(R.id.textView_list_id);
                textView_list_userid = itemView.findViewById(R.id.textView_list_userid);
                textView_list_binname = itemView.findViewById(R.id.textView_list_binname);

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
                            SellBoard_DataItem item = sellboard_data.get(pos);
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);

            Sellborad_MyAdapter.ViewHolder viewHolder = new Sellborad_MyAdapter.ViewHolder(view);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SellBoard_DataItem item = sellboard_data.get(position);
            holder.textView_list_id.setText(item.getS_board_ID());
            holder.textView_list_userid.setText(item.getS_board_userID());
            holder.textView_list_binname.setText(item.getS_board_binName());
            holder.glass_weight.setText(item.getS_board_glassW() + " kg");
            holder.plastic_weight.setText(item.getS_board_plasticW() + " kg");
            holder.paper_weight.setText(item.getS_board_paperW() + " kg");
            holder.metal_weight.setText(item.getS_board_metalW() + " kg");

            holder.glass_row.setVisibility(View.VISIBLE);
            holder.plastic_row.setVisibility(View.VISIBLE);
            holder.paper_row.setVisibility(View.VISIBLE);
            holder.metal_row.setVisibility(View.VISIBLE);

            if(item.getS_board_glassW().equals("0")) {
                holder.glass_row.setVisibility(View.GONE);
            }
            if(item.getS_board_plasticW().equals("0")) {
                holder.plastic_row.setVisibility(View.GONE);
            }
            if(item.getS_board_paperW().equals("0")) {
                holder.paper_row.setVisibility(View.GONE);
            }
            if(item.getS_board_metalW().equals("0")) {
                holder.metal_row.setVisibility(View.GONE);
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


