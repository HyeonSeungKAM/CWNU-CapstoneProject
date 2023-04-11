package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class SPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spage);

        TextView userid = (TextView) findViewById(R.id.userid);

        Intent intent = getIntent();
        String userID = intent.getExtras().getString("userID");
        userid.setText(userID);



    }
}
