package com.example.administrator.myapplication;

import android.content.Context;
import android.net.Uri;
import android.net.http.HttpResponseCache;

import java.io.IOException;
import java.net.CacheResponse;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class ConnectNet {
    public ConnectNet(Context context){

    }
    public void getNet(Map<String, List<String>> ma){
        HttpResponseCache httpResponseCache = HttpResponseCache.getInstalled();
        URI uri = URI.create("192.168.1.201");
        try {
            CacheResponse response = httpResponseCache.get(uri, "domain", ma);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
