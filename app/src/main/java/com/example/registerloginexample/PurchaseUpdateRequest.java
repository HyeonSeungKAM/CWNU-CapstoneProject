package com.example.registerloginexample;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PurchaseUpdateRequest extends StringRequest {

    final static private String URL = "http://gamhs44.ivyro.net/PurchaseUpdate.php";

    private Map<String, String> map;

    public PurchaseUpdateRequest(String seller_userID, String userID, String purchase_date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("seller_userID", seller_userID);
        map.put("userID", userID);
        map.put("purchase_date", purchase_date);
    }

    protected  Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
