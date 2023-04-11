package com.example.registerloginexample;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SPageRequest extends StringRequest {
    final static private String URL = "http://gamhs44.ivyro.net/call_sellboardinfo.php";
    private Map<String, String> map;

    public SPageRequest(String board_userID, String board_binname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put( "board_userID", board_userID);
        map.put( "board_binname", board_binname);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
