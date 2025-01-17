package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id, et_pass;
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);


        //회원가입 버튼을 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        
        //로그인 버튼을 클릭 시 수행
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get해온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                String kind = jsonObject.getString("kind");
                                if(kind.equals("buyer")) {
                                    String userID = jsonObject.getString("userID");
                                    String userName = jsonObject.getString("userName");

                                    // BuyerMainActivity : 구매자 메인 페이지
                                    Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, BuyerMainActivity.class);
                                    intent.putExtra("kind",kind);
                                    intent.putExtra("userID",userID);
                                    intent.putExtra("userName",userName);

                                    startActivity(intent);

                                } else if (kind.equals("user")) {
                                    String userID = jsonObject.getString("userID");
                                    String userName = jsonObject.getString("userName");
                                    String address = jsonObject.getString("address");

                                    // MainActivity : 관리자(사용자) 메인 페이지
                                    Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("kind",kind);
                                    intent.putExtra("userID",userID);
                                    intent.putExtra("userName",userName);
                                    intent.putExtra("address",address);

                                    startActivity(intent);
                                }

                            } else { // 로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }

        });
    }

}