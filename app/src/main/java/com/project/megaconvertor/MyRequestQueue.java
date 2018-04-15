package com.project.megaconvertor;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Andrew on 4/15/2018.
 */

public class MyRequestQueue {

    private static MyRequestQueue INSTANCE;
    private RequestQueue myRequestQueue;

    private MyRequestQueue( Context context ) {
        if (myRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            myRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static MyRequestQueue getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MyRequestQueue(context);
        }
        return INSTANCE;
    }

    public RequestQueue getQueue() {
        return myRequestQueue;
    }

}
