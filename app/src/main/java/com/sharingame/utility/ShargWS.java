package com.sharingame.utility;

import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.sharingame.entity.ShargModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ShargWS extends AsyncTask<String, String, String> {

    //private String baseURL = "https://server-tptm2.herokuapp.com/api/";
    private String baseURL = "http://192.168.1.9:3000/api/";
    private String api;
    private String[] data;
    private String requestMode;
    private ArrayList<NameValuePair> params = null;

    public ShargWS(String requestMode, String api, @Nullable ArrayList<NameValuePair> params, @Nullable String...data){
        this.api = api;
        if(data == null)
            this.data = new String[]{};
        else
            this.data = data;
        this.requestMode = requestMode;
        this.params = params;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            String api_url = this.baseURL + this.api + "/" + ObjectUtils.joinString(data, "/");
            Log.i("URL_WS",api_url);
            URL url = new URL(api_url);

            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(requestMode);
            if(requestMode.equals("POST")) {
                connection.setDoInput(true);
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(this.params));
                writer.flush();
                writer.close();
                os.close();
                Log.i("------CODE------", connection.getResponseCode()+"");
                if(connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
                    return null;
            }
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();

            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
