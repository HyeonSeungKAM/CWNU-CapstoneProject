package com.example.registerloginexample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // ------------- DB로 부터 데이터 불러오기 -----------------
    private static String TAG = "php_ListAcitivity";

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_USERID = "userID";
    private static final String TAG_BINNAME = "binName";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;

    String mJsonString;
// ------------- ----------------------------------


    private Button btn_sell2, btn_main; // 판매하기, 취소

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String binName = intent.getStringExtra("binName");
        String glass = intent.getStringExtra("glass");
        String plastic = intent.getStringExtra("plastic");
        String paper = intent.getStringExtra("paper");
        String metal = intent.getStringExtra("metal");


        mlistView = (ListView) findViewById(R.id.listview_innerframe);
        mArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://gamhs44.ivyro.net/sellboardlist.php");

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.item_list, null);

        TextView textView_list_userid = (TextView) vi.findViewById(R.id.textView_list_userid);
        TextView textView_list_binname = (TextView) vi.findViewById(R.id.textView_list_binname);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {

                                                 String board_userID = textView_list_userid.getText().toString();
                                                 String board_binname = textView_list_binname.getText().toString();
                                                 Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                     @Override
                                                     public void onResponse(String response) {
                                                         try {
                                                             JSONObject jsonObject = new JSONObject(response);
                                                             boolean success = jsonObject.getBoolean("sucess");
                                                             if (success) {
                                                                 String userID = jsonObject.getString("userID");
                                                                 String binName = jsonObject.getString("binName");
                                                                 String contents = jsonObject.getString("contents");
                                                                 String Date = jsonObject.getString("Date");
                                                                // String glassW = jsonObject.getString("glassW");
                                                                // String plasticW = jsonObject.getString("plasticW");
                                                                // String paperW = jsonObject.getString("paperW");
                                                                // String metalW = jsonObject.getString("metalW");
                                                                // String totalGlassPrice = jsonObject.getString("totalGlassPrice");
                                                                // String totalPlasticPrice = jsonObject.getString("totalPlasticPrice");
                                                                // String totalPaperPrice = jsonObject.getString("totalPaperPrice");
                                                                // String totalMetalPrice = jsonObject.getString("totalMetalPrice");
                                                                // String Total = jsonObject.getString("Total");

                                                                 Intent intent = new Intent(ListActivity.this, SPageActivity.class);
                                                                 intent.putExtra("userID", userID);
                                                                 intent.putExtra("binName", binName);
                                                                 intent.putExtra("contents",contents);
                                                                 intent.putExtra("Date", Date);
                                                                 // intent.putExtra("glassW", glassW);
                                                                 // intent.putExtra("plasticW", plasticW);
                                                                 // intent.putExtra("paperW", paperW);
                                                                 // intent.putExtra("metalW", metalW);
                                                                 // intent.putExtra("totalGlassPrice", totalGlassPrice);
                                                                 // intent.putExtra("totalPlasticPrice", totalPlasticPrice);
                                                                 // intent.putExtra("totalPaperPrice", totalPaperPrice);
                                                                 // intent.putExtra("totalMetalPrice", totalMetalPrice);
                                                                 // intent.putExtra("Total", Total);

                                                                 startActivity(intent);
                                                             } else {
                                                                 Toast.makeText(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                                                                 return;
                                                             }
                                                         } catch (JSONException ex) {
                                                             ex.printStackTrace();
                                                         }

                                                     }
                                                 };
                                                 SPageRequest sPageRequest = new SPageRequest(board_userID,board_binname,responseListener);
                                                 RequestQueue queue = Volley.newRequestQueue(ListActivity.this);
                                                 queue.add(sPageRequest);
                                             }
                                         });


// ------------------ 버튼들 -----------------------------------------------
        btn_sell2 = findViewById(R.id.btn_sell2); // 판매하기
        btn_main = findViewById(R.id.btn_main); // 메인


        btn_sell2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 판매하기(올리기)

                Intent intent = new Intent(ListActivity.this, SellActivity.class);
                intent.putExtra("binName", binName);
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
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                intent.putExtra("binName", binName);
                intent.putExtra("userID", userID);
                intent.putExtra("userName", userName);
                intent.putExtra("glass", glass);
                intent.putExtra("plastic", plastic);
                intent.putExtra("paper", paper);
                intent.putExtra("metal", metal);

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
}




