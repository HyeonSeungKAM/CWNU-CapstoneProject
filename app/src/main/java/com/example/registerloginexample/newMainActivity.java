package com.example.registerloginexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class newMainActivity extends AppCompatActivity {

    private static final String TAG_BinName = "binName";
    private static final String TAG_STATUS = "status";

    private Button btn_salesList,btn_logout, btn_binList, btn_list; // 판매하기, 목록 버튼
    private String userID, userName, kind, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_binList = findViewById(R.id.btn_binList);
        btn_binList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        dialogItemList = new ArrayList<>();

        for(int i=0; i<image.length;i++)
        {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put(TAG_BinName, );
        }

    }

    private void showAlertDialog() {
    }
}
