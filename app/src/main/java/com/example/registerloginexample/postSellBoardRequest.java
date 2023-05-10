package com.example.registerloginexample;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class postSellBoardRequest extends StringRequest {

    final static private String URL = "http://gamhs44.ivyro.net/postsellBoardinfo.php";

    private Map<String, String> map;

    public postSellBoardRequest(String userID, String binName, String binLoc, String contents, String Date,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("binName", binName);
        map.put("binLoc",binLoc);
        map.put("contents", contents);
        map.put("Date", Date);

    }

    protected  Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}


