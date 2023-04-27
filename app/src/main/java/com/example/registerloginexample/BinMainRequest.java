package com.example.registerloginexample;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class BinMainRequest extends StringRequest {
    final static private String URL = "http://gamhs44.ivyro.net/call_BinInfo.php";
    private Map<String, String> map;

    public BinMainRequest(String userID, String binName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put( "userID", userID);
        map.put( "binName", binName);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
};
