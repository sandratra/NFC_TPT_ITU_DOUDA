package com.sharingame.utility;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.sharingame.entity.DataJsonMapping;
import com.sharingame.entity.ShargModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShargWS extends AsyncTask<String, String, String> {

    private String baseURL = "https://server-tptm2.herokuapp.com/api/";
    private String api;
    private String[] data;

    public ShargWS(String api, String...data){
        this.api = api;
        this.data = data;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }
    public <T extends ShargModel>T FromJsonDataMapping(Class<T> targetClass, String result) {
        String minimal = result.replace("{\"data\":", "");
        minimal = minimal.substring(0, minimal.length()-2);
        Log.w("MINIMAL", minimal);
        return new Gson().fromJson(minimal, targetClass);
    }

    public <T>T FromJsonSimple(Class<T> targetClass, String result) {
        return new Gson().fromJson(result, targetClass);
    }

    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            String api_url = this.baseURL + this.api + "/" + ObjectUtils.strJoin(data, "/");
            Log.i("URL_WS",api_url);
            URL url = new URL(api_url);
            connection = (HttpURLConnection) url.openConnection();
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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
