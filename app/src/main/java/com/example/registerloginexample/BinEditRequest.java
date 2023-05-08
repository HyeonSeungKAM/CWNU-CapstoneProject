package com.example.registerloginexample;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BinEditRequest extends StringRequest {
    final static private String URL = "http://gamhs44.ivyro.net/binEdit.php";
    private Map<String, String> map;

    public BinEditRequest(String binName, String new_binName, String new_binLoc, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put( "binName", binName);
        map.put( "new_binName", new_binName);
        map.put( "new_binLoc", new_binLoc);


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
};
