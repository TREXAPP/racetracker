package com.trex.racetracker.Models;

/**
 * Created by Igor on 18.11.2016.
 */

public class EntryObj {

    private Integer EntryID;
    private String CPID;
    private String CPName;
    private String UserID;
    private String ActiveRacerID;
    private String Barcode;
    private String BIB;
    private String Time;
    private Integer EntryTypeID;
    private String Comment;
    private boolean Synced;
    private boolean myEntry;
    private String Operator;
    private boolean Valid;
    private String CPNo;
    private String RaceID;
    private String FirstName;
    private String LastName;
    private String Country;
    private String Gender;
    private int Age;
    private String ReasonInvalid;
    private Long InsertTimeStamp;
    private Long UpdateTimeStamp;
    private Long DeleteTimeStamp;


    public EntryObj() {
        this.EntryID = null;
        this.CPID = null;
        this.CPName = "";
        this.UserID = null;
        this.ActiveRacerID = null;
        this.Barcode = "";
        this.Time = "";
        this.EntryTypeID = null;
        this.Comment = "";
        this.Synced = false;
        this.myEntry = false;
        this.Operator = "";
        this.CPNo = "";
        this.RaceID = null;
        this.FirstName = null;
        this.LastName = null;
        this.Country = null;
        this.Gender = null;
        this.Age = 0;
        this.ReasonInvalid = null;
        this.InsertTimeStamp = null;
        this.UpdateTimeStamp = null;
        this.DeleteTimeStamp = null;
    }

    public Integer getEntryID() {
        return EntryID;
    }

    public String getCPID() {
        return CPID;
    }

    public String getCPName() {
        return CPName;
    }

    public String getUserID() {
        return UserID;
    }

    public String getActiveRacerID() {
        return ActiveRacerID;
    }

    public String getBarcode() {
        return Barcode;
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

    public String getOperator() {
        return Operator;
    }

    public void setEntryID(Integer entryID) {
        EntryID = entryID;
    }

    public void setCPID(String CPID) {
        this.CPID = CPID;
    }

    public void setCPName(String CPName) {
        this.CPName = CPName;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public void setActiveRacerID(String activeRacerID) {
        ActiveRacerID = activeRacerID;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
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

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getBIB() {
        return BIB;
    }

    public void setBIB(String BIB) {
        this.BIB = BIB;
    }

    public boolean isValid() {
        return Valid;
    }

    public void setValid(boolean valid) {
        Valid = valid;
    }

    public String getCPNo() {
        return CPNo;
    }

    public void setCPNo(String CPNo) {
        this.CPNo = CPNo;
    }

    public String getRaceID() {
        return RaceID;
    }

    public void setRaceID(String raceID) {
        RaceID = raceID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getReasonInvalid() {
        return ReasonInvalid;
    }

    public void setReasonInvalid(String reasonInvalid) {
        ReasonInvalid = reasonInvalid;
    }

    public Long getInsertTimeStamp() {
        return this.InsertTimeStamp;
    }

    public void setInsertTimeStamp(Long insertTimeStamp) {
        this.InsertTimeStamp = insertTimeStamp;
    }

    public Long getUpdateTimeStamp() {
        return this.UpdateTimeStamp;
    }

    public void setUpdateTimeStamp(Long updateTimeStamp) {
        this.UpdateTimeStamp = updateTimeStamp;
    }

    public Long getDeleteTimeStamp() {
        return this.DeleteTimeStamp;
    }

    public void setDeleteTimeStamp(Long deleteTimeStamp) {
        this.DeleteTimeStamp = deleteTimeStamp;
    }
}
