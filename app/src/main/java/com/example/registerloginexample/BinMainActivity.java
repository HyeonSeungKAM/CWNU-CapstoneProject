package com.example.registerloginexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BinMainActivity extends AppCompatActivity {

    private static final String TAG_BinName = "binName";
    private static final String TAG_STATUS = "glass_full";
    private static final String TAG_TYPE = "type";

    private Button btn_salesList, btn_map, btn_main, btn_editBin, btn_sell, btn_list, btn_binList;
    private String userID, userName, kind, address, binName, binLoc,
            mDate, glass, plastic, paper, metal, glass_full, plastic_full,paper_full, metal_full;

    List<Map<String, String>> dialogItemList;

    // 서버에서 가져온 내용 보여주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binmain);

      /**  TextView tv_id = findViewById(R.id.tv_id);
        TextView tv_name = findViewById(R.id.tv_name); **/
        TextView tv_binName = findViewById(R.id.tv_binName);
        TextView tv_binLoc = findViewById(R.id.tv_binLoc);
        TextView tv_mDate = findViewById(R.id.tv_mDate);
        TextView tv_glassW = findViewById(R.id.tv_glassW);
        TextView tv_plasticW = findViewById(R.id.tv_plasticW);
        TextView tv_paperW = findViewById(R.id.tv_paperW);
        TextView tv_metalW = findViewById(R.id.tv_metalW);

        TextView ic_glass = findViewById(R.id.gf);
        TextView ic_plastic = findViewById(R.id.plf);
        TextView ic_paper = findViewById(R.id.pf);
        TextView ic_metal = findViewById(R.id.mf);

        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        address = intent.getStringExtra("address");
        binName = intent.getStringExtra("binName");
        binLoc = intent.getStringExtra("binLoc");
        mDate = intent.getStringExtra("mDate");
        glass = intent.getStringExtra("glass");
        plastic = intent.getStringExtra("plastic");
        paper = intent.getStringExtra("paper");
        metal = intent.getStringExtra("metal");
        glass_full = intent.getStringExtra("glass_full");
        plastic_full = intent.getStringExtra("plastic_full");
        paper_full = intent.getStringExtra("paper_full");
        metal_full = intent.getStringExtra("metal_full");


     //   tv_id.setText(userID);
        tv_binName.setText(binName);
     //   tv_name.setText(userName);
        tv_binLoc.setText(binLoc);
        tv_mDate.setText(mDate);
        tv_glassW.setText(glass +"kg");
        tv_plasticW.setText(plastic+"kg");
        tv_paperW.setText(paper+"kg");
        tv_metalW.setText(metal+"kg");


        ic_glass.setVisibility(View.GONE);
        ic_plastic.setVisibility(View.GONE);
        ic_paper.setVisibility(View.GONE);
        ic_metal.setVisibility(View.GONE);

        if (glass_full.equals("1")) {
            ic_glass.setVisibility(View.VISIBLE);
        }
        if (plastic_full.equals("1")) {
            ic_plastic.setVisibility(View.VISIBLE);
        }
        if (paper_full.equals("1")) {
            ic_paper.setVisibility(View.VISIBLE);
        }
        if (metal_full.equals("1")) {
            ic_metal.setVisibility(View.VISIBLE);
        }



        btn_main = findViewById(R.id.btn_main);
        btn_main.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, MainActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });


        btn_editBin = findViewById(R.id.btn_editBin);
        btn_editBin.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, BinEditActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                intent.putExtra("binName",binName);
                intent.putExtra("binLoc",binLoc);
                intent.putExtra("mDate",mDate);
                intent.putExtra("glass",glass);
                intent.putExtra("plastic",plastic);
                intent.putExtra("paper",paper);
                intent.putExtra("metal",metal);
                startActivity(intent);
            }
        });


        btn_list = findViewById(R.id.btn_list); // 목록
        btn_list.setOnClickListener(new View.OnClickListener() {    // 목록
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, ListActivity.class);

                intent.putExtra("kind",kind);
                intent.putExtra("binName",binName);
                intent.putExtra("binLoc",binLoc);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                intent.putExtra("glass",glass);
                intent.putExtra("plastic",plastic);
                intent.putExtra("paper",paper);
                intent.putExtra("metal",metal);
                startActivity(intent);

            }
        });

        btn_sell = findViewById(R.id.btn_sell); // 판매하기

        btn_sell.setOnClickListener(new View.OnClickListener() {    // 판매하기
            @Override
            public void onClick(View view) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (glass_full.equals("0") && plastic_full.equals("0") && paper_full.equals("0") && metal_full.equals("0")) {
                            Toast.makeText(getApplicationContext(),"수거함이 가득차지 않았습니다.",Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    String glass = jsonObject.getString("glass");
                                    String plastic = jsonObject.getString("plastic");
                                    String paper = jsonObject.getString("paper");
                                    String metal = jsonObject.getString("metal");

                                    Intent intent = new Intent(BinMainActivity.this, SellActivity.class);
                                    intent.putExtra("kind",kind);
                                    intent.putExtra("binName",binName);
                                    intent.putExtra("binLoc",binLoc);
                                    intent.putExtra("userID",userID);
                                    intent.putExtra("userName",userName);
                                    intent.putExtra("address",address);
                                    intent.putExtra("glass",glass);
                                    intent.putExtra("plastic",plastic);
                                    intent.putExtra("paper",paper);
                                    intent.putExtra("metal",metal);
                                    intent.putExtra("glass_full",glass_full);
                                    intent.putExtra("plastic_full",plastic_full);
                                    intent.putExtra("paper_full",paper_full);
                                    intent.putExtra("metal_full",metal_full);

                                    startActivity(intent);

                                } else {
                                    Toast.makeText(getApplicationContext(),"정보를 불러오는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                SellInfoRequest sellinfoRequest = new SellInfoRequest(binName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(BinMainActivity.this);
                queue.add(sellinfoRequest);
            }
        });



        btn_salesList = findViewById(R.id.btn_salesList);
        btn_salesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this, SalesListActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("binName",binName);
                intent.putExtra("binLoc",binLoc);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                intent.putExtra("glass",glass);
                intent.putExtra("plastic",plastic);
                intent.putExtra("paper",paper);
                intent.putExtra("metal",metal);
                intent.putExtra("glass_full",glass_full);
                intent.putExtra("plastic_full",plastic_full);
                intent.putExtra("paper_full",paper_full);
                intent.putExtra("metal_full",metal_full);
                startActivity(intent);
            }
        });


        dialogItemList = new ArrayList<>();

        btn_binList = findViewById(R.id.btn_binList);
        btn_binList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            System.out.println(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                String binName = jsonObject.getString(TAG_BinName);
                                String glass_full = jsonObject.getString("glass_full");
                                String plastic_full = jsonObject.getString("plastic_full");
                                String paper_full = jsonObject.getString("paper_full");
                                String metal_full = jsonObject.getString("metal_full");

                                Map<String, String> itemMap = new HashMap<>();
                                if (glass_full.equals("1")) {
                                    itemMap.put(TAG_TYPE, "유리병 수거함");
                                    itemMap.put(TAG_STATUS, TAG_STATUS);

                                } else if (plastic_full.equals("1")) {
                                    itemMap.put(TAG_TYPE, "플라스틱 수거함");
                                    itemMap.put(TAG_STATUS, TAG_STATUS);

                                } else if(paper_full.equals("1")) {
                                    itemMap.put(TAG_TYPE, "종이 수거함");
                                    itemMap.put(TAG_STATUS, TAG_STATUS);

                                } else if(metal_full.equals("1")) {
                                    itemMap.put(TAG_TYPE, "고철 수거함");
                                    itemMap.put(TAG_STATUS, TAG_STATUS);
                                };
                                itemMap.put(TAG_BinName, binName);
                                dialogItemList.add(itemMap);
                            }
                            showAlertDialog();
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                BinListRequest binListRequest = new BinListRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(BinMainActivity.this);
                queue.add(binListRequest);
            }
        });

    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BinMainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null);
        builder.setView(view);

        TextView tv_bin_add_delete = view.findViewById(R.id.tv_bin_add_delete);
        tv_bin_add_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinMainActivity.this,AddDropBinActivity.class);
                intent.putExtra("kind",kind);
                intent.putExtra("userID",userID);
                intent.putExtra("userName",userName);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });



        final ListView listview = (ListView)view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();

        SimpleAdapter simpleAdapter = new SimpleAdapter(BinMainActivity.this, dialogItemList,
                R.layout.alert_dialog_row,
                new String[]{TAG_BinName, TAG_STATUS},
                new int[]{R.id.alertDialogItemBins, R.id.alertDialogItemStatus});

        listview.setAdapter(simpleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String binName = ((TextView) view.findViewById(R.id.alertDialogItemBins)).getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                String binLoc = jsonObject.getString("binLoc");
                                String mDate = jsonObject.getString("mDate");
                                String glass = jsonObject.getString("glass");
                                String plastic = jsonObject.getString("plastic");
                                String paper = jsonObject.getString("paper");
                                String metal = jsonObject.getString("metal");
                                String glass_full = jsonObject.getString("glass_full");
                                String plastic_full = jsonObject.getString("plastic_full");
                                String paper_full = jsonObject.getString("paper_full");
                                String metal_full = jsonObject.getString("metal_full");

                                Intent intent = new Intent(BinMainActivity.this, BinMainActivity.class);
                                intent.putExtra("kind",kind);
                                intent.putExtra("binName",binName);
                                intent.putExtra("binLoc",binLoc);
                                intent.putExtra("mDate",mDate);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);
                                intent.putExtra("address",address);
                                intent.putExtra("glass",glass);
                                intent.putExtra("plastic",plastic);
                                intent.putExtra("paper",paper);
                                intent.putExtra("metal",metal);
                                intent.putExtra("glass_full",glass_full);
                                intent.putExtra("plastic_full",plastic_full);
                                intent.putExtra("paper_full",paper_full);
                                intent.putExtra("metal_full",metal_full);

                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(),"정보를 불러오는데 실패하였습니다.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                BinMainRequest binMainRequest = new BinMainRequest(userID, binName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(BinMainActivity.this);
                queue.add(binMainRequest);
            }
        });

        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                dialogItemList.clear();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}