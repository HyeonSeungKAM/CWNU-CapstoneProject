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
import android.widget.TextView;

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

public class PurchasedListActivity extends AppCompatActivity {

    private static String TAG = "php_PurchasedListAcitivity";

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_PDATE = "Date";
    private static final String TAG_SELLER = "seller_IDName";
    private static final String TAG_CONTENTS = "contents";

    private static final String TAG_TPAYMENT = "total_payment";
    private static final String TAG_SPHONENUM = "seller_phoneNum";
    private static final String TAG_SADDRESS = "seller_address";



    private Button btn_main, btn_list; // 판매하기, 취소
   // private TextView mTextViewResult;

    String pJsonString;

    ListView plistView;
    ArrayList<HashMap<String, String>> pArrayList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasedlist);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");

        String buyer_ID = userID;

      //  mTextViewResult = (TextView)findViewById(R.id.textView_main_result);



        //==== 판매 리스트 ==========================================================//
        plistView = (ListView) findViewById(R.id.listview_purchase_innerframe);
        pArrayList = new ArrayList<>();

        PurchasedListActivity.GetData task = new PurchasedListActivity.GetData();
        task.execute(buyer_ID);



        btn_main = findViewById(R.id.btn_main); // 메인
        btn_list = findViewById(R.id.btn_list);

        btn_main.setOnClickListener(new View.OnClickListener() {    // 메인
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(PurchasedListActivity.this, BuyerMainActivity.class);
                    intent.putExtra("kind",kind);
                    intent.putExtra("userID", userID);

                    startActivity(intent);


            }
        });


        btn_list.setOnClickListener(new View.OnClickListener() {    // 메인
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PurchasedListActivity.this, ListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID", userID);

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

            progressDialog = ProgressDialog.show(PurchasedListActivity.this,
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

                pJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String buyer_ID = params[0];

            String serverURL = "http://gamhs44.ivyro.net/purchasedlist.php";
            String postParameters = "buyer_ID=" + buyer_ID;

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
            JSONObject jsonObject = new JSONObject(pJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String pdate = item.getString(TAG_PDATE);
                String content = item.getString(TAG_CONTENTS);

                String edit_con = "유리병"+content.split(",")[0]+"kg" + "플라스틱"+content.split(",")[2]+"kg" 
                        + "\n" +"종이"+content.split(",")[4]+"kg" + "고철"+content.split(",")[6]+"kg";

                String tpayment = item.getString(TAG_TPAYMENT);
                String seller = item.getString(TAG_SELLER);
                String saddress = item.getString(TAG_SADDRESS);
                String sphonenum = item.getString(TAG_SPHONENUM);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_PDATE, pdate);
                hashMap.put(TAG_CONTENTS, edit_con);
                hashMap.put(TAG_TPAYMENT, tpayment);
                hashMap.put(TAG_SELLER, seller);
                hashMap.put(TAG_SADDRESS, saddress);
                hashMap.put(TAG_SPHONENUM, sphonenum);

                pArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    PurchasedListActivity.this, pArrayList, R.layout.item_purchasedlist,
                    new String[]{TAG_PDATE, TAG_SELLER, TAG_CONTENTS,TAG_TPAYMENT,TAG_SPHONENUM,TAG_SADDRESS},
                    new int[]{R.id.tv_pdate, R.id.tv_seller, R.id.tv_content,R.id.tv_tpayment ,R.id.tv_sphoneNum ,R.id.tv_saddress}
            );

            plistView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

    }

}
