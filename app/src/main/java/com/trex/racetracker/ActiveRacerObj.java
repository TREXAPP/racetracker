package com.trex.racetracker;
import android.content.Context;

import static com.trex.racetracker.StaticMethods.*;

/**
 * Created by Igor_2 on 11.11.2016.
 */

public class ActiveRacerObj {

    private String BIB;
    private String FirstName;
    private String LastName;
    private String Country;
    private int Age;
    private String Gender;
    private String TimeLast;
    private String CPNo;
    private String CPName;
    private String DateOfBirth;

    public ActiveRacerObj() {
        this.BIB = "";
        this.FirstName = "";
        this.LastName = "";
        this.Country = "";
        this.Age = 0;
        this.Gender = "";
        this.TimeLast = "";
        this.CPNo = "";
        this.CPName = "";
        this.DateOfBirth = "";
    }

    public ActiveRacerObj(String myBIB, String myFirstName, String myLastName, String myCountry, int myAge, String myGender, String myTimeLast, String myCPNo, String myCPName, String myDateOfBirth) {
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

        if (myAge != 0) {
            this.Age = myAge;
        } else {
            this.Age = 0;
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

        if (myDateOfBirth != null) {
            this.DateOfBirth = myDateOfBirth;
        } else {
            this.DateOfBirth = "";
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
    public String getDateOfBirth() {
        return DateOfBirth;
    }
    public int getAge() {
        return Age;
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
    public void setDateOfBirth(String myDateOfBirth) {
        if (myDateOfBirth != null) {
            this.DateOfBirth = myDateOfBirth;
            this.Age = calculateAge(DateOfBirth);
        } else {
            this.DateOfBirth = "";
            this.Age = 0;
        }

        //TODO
        //set Age HERE!!!
    }
    public void setGender(String myGender) {
        if (myGender != null) {
            this.Gender = myGender;
        } else {
            this.Gender = "";
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
