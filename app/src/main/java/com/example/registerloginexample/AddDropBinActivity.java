package com.example.registerloginexample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AddDropBinActivity extends AppCompatActivity {

    private static String TAG = "php_AddDropBinAcitivity";

    private static final String TAG_JSON = "webnautes";
    private static final String TAG_BINNAME = "binName";
    private static final String TAG_BINLOC = "binLoc";

    private Button btn_main, btn_add;

    private ListView binlistView;
    ArrayList<HashMap<String, String>> binArrayList;


    String binJsonString;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddropbin);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String address = intent.getStringExtra("address");

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddDropBinActivity.this, BinAddActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });



        btn_main = findViewById(R.id.btn_main);
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddDropBinActivity.this, MainActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });

        binlistView = (ListView) findViewById(R.id.bin_listview_innerframe);
        binArrayList = new ArrayList<>();

        AddDropBinActivity.GetData task = new AddDropBinActivity.GetData();
        task.execute(userID);

        binlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String binName = ((TextView) view.findViewById(R.id.tv_list_binName)).getText().toString();
                AlertDialog.Builder alertDig = new AlertDialog.Builder(view.getContext());
                alertDig.setTitle("삭제확인");
                alertDig.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success) {
                                        Toast.makeText(getApplicationContext(),"삭제 완료",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddDropBinActivity.this, AddDropBinActivity.class);
                                        intent.putExtra("kind",kind);
                                        intent.putExtra("userID",userID);
                                        intent.putExtra("userName",userName);
                                        intent.putExtra("address",address);

                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(getApplicationContext(),"삭제 실패",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (
                                        JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        DropBinRequest dropBinRequest = new DropBinRequest(userID, binName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AddDropBinActivity.this);
                        queue.add(dropBinRequest);
                    }
                });
                alertDig.setNegativeButton("아니오", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        dialog.dismiss();  // AlertDialog를 닫는다.
                    }
                });
                alertDig.setMessage(binName+"삭제하시겠습니까?");
                alertDig.show();
                return false;
            }
        });







    }
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AddDropBinActivity.this,
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

                binJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String userID = params[0];

            String serverURL = "http://gamhs44.ivyro.net/binList2.php";
            String postParameters = "userID=" + userID;

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
            JSONObject jsonObject = new JSONObject(binJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String binName = item.getString(TAG_BINNAME);
                String binLoc = item.getString(TAG_BINLOC);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_BINNAME, binName);
                hashMap.put(TAG_BINLOC, binLoc);

                binArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    AddDropBinActivity.this, binArrayList, R.layout.item_adbinlist,
                    new String[]{TAG_BINNAME, TAG_BINLOC},
                    new int[]{R.id.tv_list_binName, R.id.tv_list_binLoc}
            );

            binlistView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

    }


}



