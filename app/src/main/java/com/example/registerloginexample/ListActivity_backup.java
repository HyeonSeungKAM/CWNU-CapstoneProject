/** package com.example.registerloginexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity_backup extends AppCompatActivity {

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


    private RecyclerView recyclerView;


    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;

    String mJsonString;
// ------------- ----------------------------------


    private Button btn_purchasedlist, btn_sell2, btn_main; // 판매하기, 취소

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        String url = "http://gamhs44.ivyro.net/rank.php"; // PHP 파일의 URL
        new GetRankDataTask().execute(url);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
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


//==== 판매 리스트 ==========================================================//
        mlistView = (ListView) findViewById(R.id.listview_innerframe);
        mArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://gamhs44.ivyro.net/sellboardlist.php");

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String board_id = ((TextView) view.findViewById(R.id.textView_list_id)).getText().toString();
                String board_userid = ((TextView) view.findViewById(R.id.textView_list_userid)).getText().toString();
                System.out.println(board_id);
                System.out.println(board_userid);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                String board_userID = jsonObject.getString("userID");
                                String board_userName = jsonObject.getString("userName");
                                String board_binName = jsonObject.getString("binName");
                                String board_binLoc = jsonObject.getString("binLoc");
                                String board_contents = jsonObject.getString("contents");
                                String board_Date = jsonObject.getString("Date");

                                Intent intent = new Intent(ListActivity_backup.this, SPageActivity.class);
                                intent.putExtra("board_userID", board_userID);
                                intent.putExtra("board_userName", board_userName);
                                intent.putExtra("board_binName", board_binName);
                                intent.putExtra("board_binLoc", board_binLoc);
                                intent.putExtra("board_contents",board_contents);
                                intent.putExtra("board_Date", board_Date);

                                // 로그인한 유저 정보들
                                intent.putExtra("kind",kind);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);
                                intent.putExtra("address",address);


                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                };
                SPageRequest sPageRequest = new SPageRequest(board_id, board_userid, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ListActivity_backup.this);
                queue.add(sPageRequest);
            }
        });


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
                Intent intent = new Intent(ListActivity_backup.this, PurchasedListActivity.class);
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

                Intent intent = new Intent(ListActivity_backup.this, SellActivity.class);
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
                    Intent intent = new Intent(ListActivity_backup.this, BuyerMainActivity.class);
                    intent.putExtra("kind",kind);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userName", userName);

                    startActivity(intent);

                } else if(kind.equals("user")){
                    Intent intent = new Intent(ListActivity_backup.this, MainActivity.class);
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


// ------------- 판매목록 리스트 관련 코드 --------------------------------------------
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ListActivity_backup.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            if (result == null) {
                //
            } else {

                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult() {

        LinearLayout glass_row = findViewById(R.id.glass_row);
        LinearLayout plastic_row = findViewById(R.id.plastic_row);
        LinearLayout paper_row = findViewById(R.id.paper_row);
        LinearLayout metal_row = findViewById(R.id.metal_row);


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);
                String id = item.getString(TAG_ID);
                String userID = item.getString(TAG_USERID);
                String binName = item.getString(TAG_BINNAME);
                String contents = item.getString(TAG_CONTENTS);
                String glassW = contents.split(",")[0];
                String plasticW = contents.split(",")[2];
                String paperW = contents.split(",")[4];
                String metalW = contents.split(",")[6];

                // contents 유리병 무게(0), 유리병 총 가격(1),
                //    플라스틱 무게(2), 플라스틱 총 가격(3),
                //        종이 무게(4), 종이 총 가격(5),
                 //       고철 무게(6), 고철 총 가격(7)

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_ID, id);
                hashMap.put(TAG_USERID, userID);
                hashMap.put(TAG_BINNAME, binName);
                hashMap.put(TAG_GLASSW, glassW);
                hashMap.put(TAG_PLASTICW, plasticW);
                hashMap.put(TAG_PAPERW, paperW);
                hashMap.put(TAG_METALW, metalW);

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    ListActivity_backup.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_ID, TAG_USERID, TAG_BINNAME, TAG_GLASSW, TAG_PLASTICW, TAG_PAPERW, TAG_METALW},
                    new int[]{R.id.textView_list_id, R.id.textView_list_userid, R.id.textView_list_binname,
                                R.id.glass_full, R.id.plastic_full, R.id.paper_full, R.id.metal_full }
            );
            mlistView.setAdapter(adapter);


        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

    }

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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ranklist, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
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
    // --------------------------- 랭크 데이터 불러오기 -------------------------------------------

}

**/
