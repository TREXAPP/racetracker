package com.trex.racetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import static com.trex.racetracker.DbMethods.*;

/**
 * Created by Igor on 27.4.2017.
 */

public class SyncEntriesWorker extends AsyncTask<String,Void,String> {

    private Context context;
    private String type;
    private String payload;


    public SyncEntriesWorker(Context context) {
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        type = params[0];
        String result = "";
        //TODO get params here
        if (type.equals("sync_push_insert") || type.equals("sync_push_update")) try {
            String queryUrl = params[1];
            URL url = new URL(queryUrl);
            String rowsNo = params[2];
            payload = params[3];

            String post_data = "";

            if (!type.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            }

            if (!rowsNo.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("rowsNo", "UTF-8") + "=" + URLEncoder.encode(rowsNo, "UTF-8");
            }

            if (!payload.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("payload", "UTF-8") + "=" + URLEncoder.encode(payload, "UTF-8");
            }

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();


            //now get the response
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String line = "";
            //    result = bufferedReader.toString();
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (type.equals("sync_pull")) try {
            String queryUrl = params[1];
            //TODO get params here
        } catch (Exception e) {
            e.printStackTrace();
       }


        if (result.equals("")) {
            return null;
        } else {
            return result;
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {


            String error = "";
            if (type.equals("sync_push_insert") || type.equals("sync_push_update")) {

                try {
                    JSONObject jsonResult = new JSONObject(result);
                    if (jsonResult.has("success")) {
                        if (jsonResult.getString("success").equals("1")) {

                            if (!updateCPEntriesSynced(context, jsonResult)) {
                                error += "Error while writing in SQLite, CPEntries table. Contact the administrator;";
                            }
                            ;

                        } else {
                            if (jsonResult.has("error")) {
                                error += jsonResult.getString("error");
                            } else {
                                error += "Error with webserver! Contact the administrator.";
                            }
                        }
                    } else {
                        error += "Error with database! Contact the administrator.";

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            if (!error.equals("")) {
                Log.e("Sync error", error);
            } else {
                SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
                SharedPreferences.Editor editor = globals.edit();
                Long timeNow = System.currentTimeMillis();
                editor.putLong("lastPushInMillis",timeNow);
                editor.commit();
            }
        }
    }
}
