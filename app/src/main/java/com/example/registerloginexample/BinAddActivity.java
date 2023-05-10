package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BinAddActivity extends AppCompatActivity {

    private Button btn_add;
    private EditText et_binLoc, et_binName;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binadd);

        et_binName = findViewById(R.id.et_binName);
        et_binLoc = findViewById(R.id.et_binLoc);

        Intent intent = getIntent();
        String kind = intent.getStringExtra("kind");
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String address = intent.getStringExtra("address");

        CheckBox cb_loc = findViewById(R.id.cb_loc);
        cb_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()) {
                    et_binLoc.setText(address);
                } else {
                    et_binLoc.setText(null);
                }
            }
        });

        et_binLoc.setFocusable(false);
        et_binLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BinAddActivity.this, SearchActivity.class);
                getSearchResult.launch(intent);
            }
        });

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get해온다.
                String binName = et_binName.getText().toString();
                String binLoc = et_binLoc.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 회원등록에 성공한 경우
                                Toast.makeText(getApplicationContext(),"정보 수정에 성공하였습니다",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BinAddActivity.this, AddDropBinActivity.class);
                                intent.putExtra("kind",kind);
                                intent.putExtra("userID",userID);
                                intent.putExtra("userName",userName);
                                intent.putExtra("address",address);
                                startActivity(intent);
                            } else { // 회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(),"정보 수정에에 실패하였습니다",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 서버로 Volley를 이용해 요청을 함.
                BinAddRequest binAddRequest = new BinAddRequest(userID, binName, binLoc,responseListener);
                RequestQueue queue = Volley.newRequestQueue(BinAddActivity.this);
                queue.add(binAddRequest);

            }
        });

        Button btn_cancel = findViewById(R.id.btn_cancel); // 목록
        btn_cancel.setOnClickListener(new View.OnClickListener() {    // 취소
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if(result.getData() != null) {
                        String data = result.getData().getStringExtra("data");
                        et_binLoc.setText(data);
                    }
                }
            }
    );


}
