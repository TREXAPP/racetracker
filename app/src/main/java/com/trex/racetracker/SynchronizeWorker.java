package com.trex.racetracker;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Igor on 04.11.2016.
 */

public class SynchronizeWorker extends AsyncTask<String,Void,String> {
    private Context context;
    private View rootView;
    private String queryUrl;
    private String Username;
    private String Operator;
    private String DeviceID;
    private String SyncComment;

    //constructor
    public SynchronizeWorker(Context ctx, View view) {
        this.context = ctx;
        this.rootView = view;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String result = "";

        if (type.equals("sync")) try {

            queryUrl = params[1];
            Username = params[2];
            Operator = params[3];
            DeviceID = params[4];
            SyncComment = params[5];
            URL url = new URL(queryUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = "";
            if (!Username.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(Username, "UTF-8");
            }
            if (!Operator.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("operator", "UTF-8") + "=" + URLEncoder.encode(Operator, "UTF-8");
            }
            if (!DeviceID.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("deviceid", "UTF-8") + "=" + URLEncoder.encode(DeviceID, "UTF-8");
            }
            if (!SyncComment.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("synccomment", "UTF-8") + "=" + URLEncoder.encode(SyncComment, "UTF-8");
            }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {

    }

}
