package com.example.mytranslator;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyRequestQueue {
    private static MyRequestQueue instance;
    private Context context;
    private static RequestQueue queue;
    private String baseURL = "https://translate.yandex.net/api/v1.5/tr.json/";
    private String key = "trnsl.1.1.20200506T080306Z.e5463e2f6bfc35ea.49287e37ae9dbbf5098d1d3683e18ea05e27448c";

    private MyRequestQueue(Context context) {
        this.context = context;
    }

    static MyRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new MyRequestQueue(context);
        }
        return instance;
    }

    RequestQueue getQueue() {
        if (queue == null)
            queue = Volley.newRequestQueue(context);
        return queue;
    }

    void addRequest(Request request) {
        getQueue().add(request);
    }

    String getLangsURL(String ui) {
        return baseURL + "getLangs?ui=" + ui + "&key=" + key;
    }

    String getTranslationURL(String text, String to) {
        return  baseURL + "translate?key=" + key + "&text=" + text + "&lang=" + to + "&format=plain" +
                "&options=1";
    }

}
