package com.example.registerloginexample;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PurchaseUpdateRequest extends StringRequest {

    final static private String URL = "http://gamhs44.ivyro.net/PurchaseUpdate.php";

    private Map<String, String> map;

    public PurchaseUpdateRequest(String seller_userID, String userID, String purchase_date, String board_contents,
                                 String total_payment, String seller_userIDName, String seller_address, String seller_phoneNum, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("seller_userID", seller_userID);
        map.put("userID", userID);
        map.put("purchase_date", purchase_date);
        map.put("board_contents", board_contents);
        map.put("total_payment", total_payment);
        map.put("seller_userIDName", seller_userIDName);
        map.put("seller_address", seller_address);
        map.put("seller_phoneNum", seller_phoneNum);
    }

    protected  Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
