package com.example.Demo.RazorPayServices;

import com.example.Demo.Model.Donations;
import org.json.JSONException;

public interface RazorPayService {
    String createOrder(Donations donations) throws JSONException, JSONException, JSONException, org.json.JSONException;
}

