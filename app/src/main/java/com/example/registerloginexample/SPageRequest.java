/** package com.example.registerloginexample;

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

    public SPageRequest(String board_id, String board_userid, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put( "board_id", board_id);
        map.put( "board_userid", board_userid);


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
**/