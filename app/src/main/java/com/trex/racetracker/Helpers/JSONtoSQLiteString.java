package com.trex.racetracker.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Igor on 03.11.2016.
 */

public class JSONtoSQLiteString {

    private JSONObject json;

    public JSONtoSQLiteString(JSONObject jsonString) {
        json = jsonString;
    }

    public String CPID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CPID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String CPComment(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CPComment");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String CPName(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CPName");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String CPNo(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CPNo");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String RaceID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RaceID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String RaceName(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RaceName");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String RaceDescription(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RaceDescription");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String UsersComment(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("UsersComment");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    //for ActiveRacers table SQLite
    public String ActiveRacerID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("ActiveRacerID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String RacerID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RacerID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

 /*
    public String RaceID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RaceID");
        } catch (JSONException e) {
            result = "-1";
        }
        return result;
    }
    */

    public String Age(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Age");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String BIB(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("BIB");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String ChipCode(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("ChipCode");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Hide(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Hide");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Registered(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Registered");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String ActiveRacersTS(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("ActiveRacersTS");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String ActiveRacersComment(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("ActiveRacersComment");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String FirstName(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("FirstName");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String LastName(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("LastName");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Gender(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Gender");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String DateOfBirth(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("DateOfBirth");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String YearBirth(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("YearBirth");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Nationality(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Nationality");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Country(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Country");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String TeamID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("TeamID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String CityOfResidence(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("CityOfResidence");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String TShirtSize(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("TShirtSize");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Email(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Email");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Tel(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Tel");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Food(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Food");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String RacersTS(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("RacersTS");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Racers_Comment(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Racers_Comment");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String TeamName(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("TeamName");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String TeamDescription(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("TeamDescription");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String EntryID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("EntryID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String LocalEntryID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("LocalEntryID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String UserID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("UserID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Barcode(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Barcode");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Time(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Time");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String EntryTypeID(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("EntryTypeID");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }
    public String Timestamp(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Timestamp");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public String Operator(int i) {
        String result;
        try {
            result = json.getJSONObject(String.valueOf(i)).getString("Operator");
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }
}
