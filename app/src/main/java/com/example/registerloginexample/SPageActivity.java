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
        setContentView(R.layout.activity_spagetest);

        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);


        Intent intent = getIntent();
        String board_userID = intent.getExtras().getString("userID");
        String board_binname = intent.getExtras().getString("binName");
        String board_contents = intent.getExtras().getString("contents");
        String board_Date = intent.getExtras().getString("Date");

        textView1.setText(board_userID);
        textView2.setText(board_binname);
        textView3.setText(board_contents);
        textView4.setText(board_Date);




    }
}
