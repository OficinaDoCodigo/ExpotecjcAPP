package com.ifrn.expotec.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ifrn.expotec.R;
import com.ifrn.expotec.models.HttpConnections;
import com.ifrn.expotec.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Fabiano on 11/04/2016.
 */
public class HttpProgress extends AsyncTask<Void, Void, String> {
    String urlString;
    Context context;
    private String answer;
    ProgressDialog progress;
    private String method;
    private HashMap<String,String> data;
    public HttpProgress(Context context, String urlString, String method,HashMap<String, String> data){
        this.urlString = urlString;
        this.context = context;
        this.method = method;
        this.data = data;
    }
    protected void onPreExecute() {
    }
    @Override
    protected String doInBackground(Void... params) {
        if (method.equalsIgnoreCase("get")){
            return HttpConnections.getJson(urlString);
        }else{
            return HttpConnections.performPostCall(urlString,data);
        }
    }
    @Override
    protected void onPostExecute(String s) {
        answer = s;
    }
}
