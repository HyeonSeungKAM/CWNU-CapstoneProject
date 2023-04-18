package com.example.registerloginexample;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BuyInfoRequest extends StringRequest {

    final static private String URL = "http://gamhs44.ivyro.net/BuyInfoRequest_sellerInfo.php";

    private Map<String, String> map;


    public BuyInfoRequest(String board_userID,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("board_userID", board_userID);

    }

    protected  Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
