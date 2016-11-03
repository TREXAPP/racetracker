package com.trex.racetracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Igor on 03.11.2016.
 */

public class JSONtoSQLiteLoginString {

    private JSONObject json;

    public JSONtoSQLiteLoginString(JSONObject jsonString) {
        json = jsonString;
    }

    public String CPID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CPID");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }

    public String CPComment(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CPComment");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }

    public String CPName(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CPName");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }

    public String CPNo(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CPNo");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }

    public String RaceID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RaceID");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }

    public String RaceName(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RaceName");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }

    public String RaceDescription(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RaceDescription");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }

    public String UsersComment(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("UsersComment");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }

}
