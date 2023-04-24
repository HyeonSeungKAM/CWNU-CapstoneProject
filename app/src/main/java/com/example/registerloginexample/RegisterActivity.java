package com.example.registerloginexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    public String type = "user";
    private EditText et_id, et_pass, et_name, et_phoneNum, et_accountNumber, et_address;

    private Spinner spinner_bankMenu;
    private Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 시작시 처음으로 실행되는 생명주기!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        

        RadioGroup radioGroup = findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId){
                    case R.id.radio_button_user:
                        type = "user";
                        spinner_bankMenu.setVisibility(View.VISIBLE);
                        et_accountNumber.setVisibility(View.VISIBLE);
                        et_address.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radio_button_buyer:
                        type = "buyer";
                        spinner_bankMenu.setVisibility(View.GONE);
                        et_accountNumber.setVisibility(View.GONE);
                        et_address.setVisibility(View.GONE);
                        break;
                }

            }
        });


        // 회원가입 정보 기입 관련 =============================================

        et_id = findViewById(R.id.et_id);
        

        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_phoneNum = findViewById(R.id.et_phoneNum);
        et_phoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        spinner_bankMenu = findViewById(R.id.spinner_bankMenu);
        final String[] banks = getResources().getStringArray(R.array.banks_array);

        ArrayAdapter menuAdapter = ArrayAdapter.createFromResource(this, R.array.banks_array, android.R.layout.simple_spinner_item);

        // 스피너 클릭 시 DropDown 모양을 설정
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 스피너 어댑터에 연결
        spinner_bankMenu.setAdapter(menuAdapter);

        et_accountNumber = findViewById(R.id.et_accountNumber);

        // 회원가입 버튼 클릭 시 수정
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get해온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                String phoneNum = et_phoneNum.getText().toString();
                String accountBank = spinner_bankMenu.getSelectedItem().toString();
                String accountNumber = et_accountNumber.getText().toString();

                String account = accountBank + " " +accountNumber;

                String address = et_address.getText().toString();

                String kind = type;
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 회원등록에 성공한 경우
                                Toast.makeText(getApplicationContext(),"회원 등록에 성공하였습니다",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else { // 회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(),"회원 등록에 실패하였습니다",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                // 서버로 Volley를 이용해 요청을 함.
                RegisterRequest registerRequest = new RegisterRequest(kind, userID, userPass, userName,phoneNum, account, address, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });

        // 주소검색 api 사용 ============================================================

        et_address = findViewById(R.id.et_address);
        // 클릭 시
        et_address.setFocusable(false);
        et_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 주소 검색 웹뷰 화면으로 이동
                Intent intent = new Intent(RegisterActivity.this, SearchActivity.class );
                getSearchResult.launch(intent);
            }
        });


    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Search Activity 로부터의 결과 값이 이곳으로 전달 된다.. (setResult에 의해)
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        String data = result.getData().getStringExtra("data");
                        et_address.setText(data);
                    }
                }
            }
    );

}