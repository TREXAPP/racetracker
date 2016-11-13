package com.trex.racetracker;

/**
 * Created by Igor_2 on 13.11.2016.
 */

public class RaceObj {
    private String RaceID;
    private String RaceDescription;
    private boolean ShowRacers;

    public RaceObj() {
        this.RaceID = "";
        this.RaceDescription = "";
        this.ShowRacers = false;
    }

    public RaceObj(String myRaceID, String myRaceDescription, boolean myShowRacers) {
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

        if (myShowRacers) {
            this.ShowRacers = true;
        } else {
            this.ShowRacers = false;
        }
    }

    public String getRaceID() {
        return RaceID;
    }
    public String getRaceDescription() {
        return RaceDescription;
    }
    public boolean getShowRacers() {
        return ShowRacers;
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
    public void setShowRacers(boolean myShowRacers) {
        if (myShowRacers) {
            this.ShowRacers = true;
        } else {
            this.ShowRacers = false;
        }
    }

}
