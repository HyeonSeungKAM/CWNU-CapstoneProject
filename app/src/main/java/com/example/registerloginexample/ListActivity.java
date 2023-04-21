package com.example.registerloginexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class ListActivity extends AppCompatActivity {

    // ------------- DB로 부터 데이터 불러오기 -----------------
    private static String TAG = "php_ListAcitivity";

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_USERID = "userID";
    private static final String TAG_BINNAME = "binName";


    private static final String TAG_RANK = "rank";
    private static final String TAG_USERNAME = "userName";




    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ArrayList<HashMap<String, String>> RArrayList;
    ListView mlistView, RlistView;


    String mJsonString;
// ------------- ----------------------------------


    private Button btn_purchasedlist, btn_sell2, btn_main; // 판매하기, 취소

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String binName = intent.getStringExtra("binName");
        String glass = intent.getStringExtra("glass");
        String plastic = intent.getStringExtra("plastic");
        String paper = intent.getStringExtra("paper");
        String metal = intent.getStringExtra("metal");

//==== 판매 리스트 ==========================================================//
        mlistView = (ListView) findViewById(R.id.listview_innerframe);
        mArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://gamhs44.ivyro.net/sellboardlist.php");

// 랭킹 리스트====================================================================//
        RlistView = (ListView) findViewById(R.id.listview_rank_innerframe);
        RArrayList = new ArrayList<>();

        GetRankData task2 = new GetRankData();
        task2.execute("http://gamhs44.ivyro.net/rank.php");

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
                                                                 String board_binName = jsonObject.getString("binName");
                                                                 String board_contents = jsonObject.getString("contents");
                                                                 String board_Date = jsonObject.getString("Date");

                                                                 Intent intent = new Intent(ListActivity.this, SPageActivity.class);
                                                                 intent.putExtra("board_userID", board_userID);
                                                                 intent.putExtra("board_contents",board_contents);
                                                                 intent.putExtra("board_Date", board_Date);

                                                                 // 로그인한 유저 정보들
                                                                 intent.putExtra("kind",kind);
                                                                 intent.putExtra("binName",binName);
                                                                 intent.putExtra("kind",kind);
                                                                 intent.putExtra("userID",userID);
                                                                 intent.putExtra("userName",userName);
                                                                 intent.putExtra("glass",glass);
                                                                 intent.putExtra("plastic",plastic);
                                                                 intent.putExtra("paper",paper);
                                                                 intent.putExtra("metal",metal);

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
                                                 RequestQueue queue = Volley.newRequestQueue(ListActivity.this);
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
                Intent intent = new Intent(ListActivity.this, PurchasedListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);

                startActivity(intent);

            }
        });


        btn_sell2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 판매하기(올리기)

                Intent intent = new Intent(ListActivity.this, SellActivity.class);
                intent.putExtra("binName", binName);
                intent.putExtra("kind",kind);
                intent.putExtra("userID", userID);
                intent.putExtra("userName", userName);
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
                    intent.putExtra("binName", binName);
                    intent.putExtra("kind",kind);
                    intent.putExtra("userID", userID);
                    intent.putExtra("userName", userName);
                    intent.putExtra("glass", glass);
                    intent.putExtra("plastic", plastic);
                    intent.putExtra("paper", paper);
                    intent.putExtra("metal", metal);

                    startActivity(intent);
                }

            }
        });
    }
//------------------------- -----------------------------------------------

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ListActivity.this,
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
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String userID = item.getString(TAG_USERID);
                String binName = item.getString(TAG_BINNAME);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_ID, id);
                hashMap.put(TAG_USERID, userID);
                hashMap.put(TAG_BINNAME, binName);

                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    ListActivity.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_ID, TAG_USERID, TAG_BINNAME},
                    new int[]{R.id.textView_list_id, R.id.textView_list_userid, R.id.textView_list_binname}
            );

            mlistView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

    }



    private class GetRankData extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    String errorString = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(ListActivity.this,
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
            showRankResult();
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

    private void showRankResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);
                String rank = String.valueOf(i+1);
                String userID = item.getString(TAG_USERID);
                String userName = item.getString(TAG_USERNAME);

                String userIDName = userID + " (" + userName + ")";

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_RANK, rank);
                hashMap.put(TAG_USERID, userIDName);

                RArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    ListActivity.this, RArrayList, R.layout.item_ranklist,
                    new String[]{TAG_RANK, TAG_USERID},
                    new int[]{R.id.textView_list_rank,R.id.textView_list_useridname}
            );

            RlistView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.d(TAG, "showRankResult : ", e);
        }

    }

}


