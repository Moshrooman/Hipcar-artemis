package com.example.justinkwik.hipcar;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Justin Kwik on 21/05/2017.
 */
public class ConnectionManager {

    private static RequestQueue requestQueue;

    public static RequestQueue getInstance(Context context) {

        if(requestQueue == null) {

            requestQueue = Volley.newRequestQueue(context);

        }

        return requestQueue;

    }

}
