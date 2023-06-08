package com.example.registerloginexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SalesListActivity extends AppCompatActivity {

    private static String TAG = "php_SalesListAcitivity";

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_SDATE = "sdate";
    private static final String TAG_BUYER = "buyer_ID";
    private static final String TAG_CONTENTS = "contents";

    private static final String TAG_TPAYMENT = "total_payment";
    
    private Button btn_main, btn_list; // 판매하기, 취소
    // private TextView mTextViewResult;

    String sJsonString;

    ListView slistView;
    ArrayList<HashMap<String, String>> sArrayList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleslist);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String binName = intent.getStringExtra("binName");
        String binLoc = intent.getStringExtra("binLoc");
        String address = intent.getStringExtra("address");
        String seller_ID = userID;

        //  mTextViewResult = (TextView)findViewById(R.id.textView_main_result);



        //==== 판매 리스트 ==========================================================//
        slistView = (ListView) findViewById(R.id.listview_sales_innerframe);
        sArrayList = new ArrayList<>();

        SalesListActivity.GetData task = new SalesListActivity.GetData();
        task.execute(seller_ID);



        btn_main = findViewById(R.id.btn_main); // 메인
        btn_list = findViewById(R.id.btn_list);

        btn_main.setOnClickListener(new View.OnClickListener() {    // 메인
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SalesListActivity.this, MainActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID", userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);

                startActivity(intent);


            }
        });


        btn_list.setOnClickListener(new View.OnClickListener() {    // 메인
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SalesListActivity.this, ListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID", userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);

                startActivity(intent);
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

            progressDialog = ProgressDialog.show(SalesListActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //  mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null) {
                //     mTextViewResult.setText(errorString);
            } else {

                sJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String seller_ID = params[0];

            String serverURL = "http://gamhs44.ivyro.net/saleslist.php";
            String postParameters = "seller_ID=" + seller_ID;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

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
            JSONObject jsonObject = new JSONObject(sJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String pdate = item.getString(TAG_SDATE);
                String content = item.getString(TAG_CONTENTS);

                String edit_con = "유리병"+content.split(",")[0]+"kg " + "플라스틱"+content.split(",")[2]+"kg "
                        + "\n" +"종이"+content.split(",")[4]+"kg " + "고철"+content.split(",")[6]+"kg ";

                if (content.split(",")[0].equals("0")) {
                    edit_con.split("유리병")[0] = "";
                }
                if (content.split(",")[2].equals("0")) {
                    edit_con.split("\\s")[1] = "";
                }
                if (content.split(",")[4].equals("0")) {
                    edit_con.split("\\s")[2] = "";
                }
                if (content.split(",")[6].equals("0")) {
                    edit_con.split("\\s")[3] = "";
                }

                String tpayment = item.getString(TAG_TPAYMENT);
                String buyer_ID = item.getString(TAG_BUYER);


                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_SDATE, pdate);
                hashMap.put(TAG_CONTENTS, edit_con);
                hashMap.put(TAG_TPAYMENT, tpayment);
                hashMap.put(TAG_BUYER, buyer_ID);

                sArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    SalesListActivity.this, sArrayList, R.layout.item_saleslist,
                    new String[]{TAG_SDATE, TAG_BUYER, TAG_CONTENTS,TAG_TPAYMENT},
                    new int[]{R.id.tv_sdate, R.id.tv_buyer, R.id.tv_content,R.id.tv_tpayment}
            );

            slistView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

    }
}
