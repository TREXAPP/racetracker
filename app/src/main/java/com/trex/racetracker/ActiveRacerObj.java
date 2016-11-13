package com.trex.racetracker;

/**
 * Created by Igor_2 on 11.11.2016.
 */

public class ActiveRacerObj {

    private String BIB;
    private String FirstName;
    private String LastName;
    private String Country;
    private String Age;
    private String Gender;
    private String TimeLast;
    private String CPNo;
    private String CPName;

    public ActiveRacerObj() {
        this.BIB = "";
        this.FirstName = "";
        this.LastName = "";
        this.Country = "";
        this.Age = "";
        this.Gender = "";
        this.TimeLast = "";
        this.CPNo = "";
        this.CPName = "";
    }

    public ActiveRacerObj(String myBIB, String myFirstName, String myLastName, String myCountry, String myAge, String myGender, String myTimeLast, String myCPNo, String myCPName) {
       if (myBIB != null) {
           this.BIB = myBIB;
       } else {
           this.BIB = "";
       }

        if (myFirstName != null) {
            this.FirstName = myFirstName;
        } else {
            this.FirstName = "";
        }

        if (myLastName != null) {
            this.LastName = myLastName;
        } else {
            this.LastName = "";
        }

        if (myCountry != null) {
            this.Country = myCountry;
        } else {
            this.Country = "";
        }

        if (myAge != null) {
            this.Age = myAge;
        } else {
            this.Age = "";
        }

        if (myGender != null) {
            this.Gender = myGender;
        } else {
            this.Gender = "";
        }

        if (myTimeLast != null) {
            this.TimeLast = myTimeLast;
        } else {
            this.TimeLast = "";
        }

        if (myCPNo != null) {
            this.CPNo = myCPNo;
        } else {
            this.CPNo = "";
        }

        if (myCPName != null) {
            this.CPName = myCPName;
        } else {
            this.CPName = "";
        }

    }

    public String getBIB() {
        return BIB;
    }
    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }
    public String getCountry() {
        return Country;
    }
    public String getAge() {
        return Age;
    }
    public String getGender() {
        return Gender;
    }
    public String getTimeLast() {
        return TimeLast;
    }
    public String getCPNo() {
        return CPNo;
    }
    public String getCPName() {
        return CPName;
    }

    public void setBIB(String myBIB) {
        if (myBIB != null) {
            this.BIB = myBIB;
        } else {
            this.BIB = "";
        }

    }
    public void setFirstName(String myFirstName) {
        if (myFirstName != null) {
            this.FirstName = myFirstName;
        } else {
            this.FirstName = "";
        }
    }
    public void setLastName(String myLastName) {
        if (myLastName != null) {
            this.LastName = myLastName;
        } else {
            this.LastName = "";
        }
    }
    public void setCountry(String myCountry) {
        if (myCountry != null) {
            this.Country = myCountry;
        } else {
            this.Country = "";
        }
    }
    public void setAge(String myAge) {
        if (myAge != null) {
            this.Age = myAge;
        } else {
            this.Age = "";
        }
    }
    public void setGender(String myGender) {
        if (myGender != null) {
            this.Gender = myGender;
        } else {
            this.Gender = "";
        }
    }
    public void setTimeLast(String myTimeLast) {
        if (myTimeLast != null) {
            this.TimeLast = myTimeLast;
        } else {
            this.TimeLast = "";
        }
    }
    public void setCPNo(String myCPNo) {
        if (myCPNo != null) {
            this.CPNo = myCPNo;
        } else {
            this.CPNo = "";
        }
    }
    public void setCPName(String myCPName) {
        if (CPName != null) {
            this.CPName = myCPName;
        } else {
            this.CPName = "";
        }
    }


}