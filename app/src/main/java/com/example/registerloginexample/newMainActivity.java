package com.example.registerloginexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class newMainActivity extends AppCompatActivity {

    private static final String TAG_BinName = "binName";
    private static final String TAG_STATUS = "status";

    private Button btn_salesList,btn_logout, btn_binList, btn_list; // 판매하기, 목록 버튼
    private String userID, userName, kind, address;

    TextView textview_result;

    List<Map<String, Object>> dialogItemList;
    int[] image = {R.drawable.ic_001_grape, R.drawable.ic_002_cherry, R.drawable.ic_003_watermelon};
    String[] text = {"포도", "체리", "수박"};


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
            itemMap.put(TAG_BinName, image[i]);
            itemMap.put(TAG_STATUS, text[i]);

            dialogItemList.add(itemMap);

        }

    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);

        final ListView listview = (ListView)view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();

        SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, dialogItemList,
                R.layout.alert_dialog_row,
                new String[]{TAG_IMAGE, TAG_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});

        listview.setAdapter(simpleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                textview_result.setText(text[position] + "를(을) 선택했습니다.");
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
