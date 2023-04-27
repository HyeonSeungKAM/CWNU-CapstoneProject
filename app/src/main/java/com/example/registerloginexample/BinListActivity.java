package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BinListActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binlist);

        Intent intent = getIntent();
        ArrayList<String> binList = intent.getStringArrayListExtra("binList");

        ListView list_bins = findViewById(R.id.list_bins);
        ArrayAdapter<String> adpater = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,binList);
        list_bins.setAdapter(adpater);
        
    }
}
