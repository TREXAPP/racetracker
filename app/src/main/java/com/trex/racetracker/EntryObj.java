package com.trex.racetracker;

/**
 * Created by Igor on 18.11.2016.
 */

public class EntryObj {

    private Integer EntryID;
    private Integer CPID;
    private String CPName;
    private Integer UserID;
    private Integer ActiveRacerID;
    private String BIBCODE;
    private String Time;
    private Integer EntryTypeID;
    private String Comment;
    private boolean Synced;
    private boolean myEntry;

    public EntryObj() {
        this.EntryID = null;
        this.CPID = null;
        this.CPName = "";
        this.UserID = null;
        this.ActiveRacerID = null;
        this.BIBCODE = "";
        this.Time = "";
        this.EntryTypeID = null;
        this.Comment = "";
        this.Synced = false;
        this.myEntry = false;
    }

    public Integer getEntryID() {
        return EntryID;
    }

    public Integer getCPID() {
        return CPID;
    }

    public String getCPName() {
        return CPName;
    }

    public Integer getUserID() {
        return UserID;
    }

    public Integer getActiveRacerID() {
        return ActiveRacerID;
    }

    public String getBIBCODE() {
        return BIBCODE;
    }

    public String getTime() {
        return Time;
    }

    public Integer getEntryTypeID() {
        return EntryTypeID;
    }

    public String getComment() {
        return Comment;
    }

    public boolean isSynced() {
        return Synced;
    }

    public boolean isMyEntry() {
        return myEntry;
    }

    public void setEntryID(Integer entryID) {
        EntryID = entryID;
    }

    public void setCPID(Integer CPID) {
        this.CPID = CPID;
    }

    public void setCPName(String CPName) {
        this.CPName = CPName;
    }

    public void setUserID(Integer userID) {
        UserID = userID;
    }

    public void setActiveRacerID(Integer activeRacerID) {
        ActiveRacerID = activeRacerID;
    }

    public void setBIBCODE(String BIBCODE) {
        this.BIBCODE = BIBCODE;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setEntryTypeID(Integer entryTypeID) {
        EntryTypeID = entryTypeID;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public void setSynced(boolean synced) {
        Synced = synced;
    }

    public void setMyEntry(boolean myEntry) {
        this.myEntry = myEntry;
    }
}
