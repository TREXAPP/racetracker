package com.trex.racetracker.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.trex.racetracker.Activities.MainActivity;
import com.trex.racetracker.Models.Response;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class RequestHelper {

    public static Response sendPost(String urlString, JSONObject postDataParams, SharedPreferences globals, boolean requiresJwt) throws Exception {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setReadTimeout(20000);
//        connection.setConnectTimeout(20000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("x-api-key", globals.getString("x-api-key", ""));
        if (requiresJwt) { connection.setRequestProperty("Authorization", "Bearer " + globals.getString("jwt-token", "")); }
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(encodeParams(postDataParams));
        writer.flush();
        writer.close();
        outputStream.close();

        BufferedReader bufferedReader;
        Response response = new Response();
        response.setResponseCode(connection.getResponseCode());
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 299) {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        StringBuffer stringBuffer = new StringBuffer("");
        String line="";
        while((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
            break;
        }
        bufferedReader.close();
        response.setMessage(stringBuffer.toString());
        return response;
    }

    public static Response sendGet(String urlString, SharedPreferences globals, boolean requiresJwt) throws IOException {
        URL obj = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("x-api-key", globals.getString("x-api-key", ""));
        if (requiresJwt) { connection.setRequestProperty("Authorization", "Bearer " + globals.getString("jwt-token", "")); }
        int responseCode = connection.getResponseCode();

        BufferedReader bufferedReader;
        Response response = new Response();
        response.setResponseCode(connection.getResponseCode());
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 299) {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        StringBuffer stringBuffer = new StringBuffer("");
        String line="";
        while((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
            break;
        }
        bufferedReader.close();
        response.setMessage(stringBuffer.toString());
        return response;
    }

    private static String encodeParams(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}