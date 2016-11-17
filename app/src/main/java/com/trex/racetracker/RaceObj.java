package com.trex.racetracker;

/**
 * Created by Igor_2 on 13.11.2016.
 */

public class RaceObj {
    private String RaceID;
    private String RaceDescription;
    private String CPNo;

    public RaceObj() {
        this.RaceID = "";
        this.RaceDescription = "";
        this.CPNo = "";
    }

    public RaceObj(String myRaceID, String myRaceDescription, String myCPNo) {
        if (myRaceID != null) {
            this.RaceID = myRaceID;
        } else {
            this.RaceID = "";
        }

        if (myRaceDescription != null) {
            this.RaceDescription = myRaceDescription;
        } else {
            this.RaceDescription = "";
        }

        if (myCPNo != null) {
            this.CPNo = myCPNo;
        } else {
            this.CPNo = "";
        }
    }

    public String getRaceID() {
        return RaceID;
    }
    public String getRaceDescription() {
        return RaceDescription;
    }
    public String getCPNo() {
        return CPNo;
    }

    public void setRaceID(String myRaceID) {
        if (myRaceID != null) {
            this.RaceID = myRaceID;
        } else {
            this.RaceID = "";
        }

    }
    public void setRaceDescription(String myRaceDescription) {
        if (myRaceDescription != null) {
            this.RaceDescription = myRaceDescription;
        } else {
            this.RaceDescription = "";
        }
    }
    public void setCPNo(String myCPNo) {
        if (myCPNo != null) {
            this.CPNo = myCPNo;
        } else {
            this.CPNo = "";
        }
    }

}
