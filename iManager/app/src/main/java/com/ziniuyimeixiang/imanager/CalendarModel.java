package com.ziniuyimeixiang.imanager;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Austin on 2018/3/21.
 */

public class CalendarModel extends Model {
    //used to show information on a specific data
    private int cyear;
    private int cmonth;
    private int cday;

    public int getHourdiff() {
        return hourdiff;
    }

    public void setHourdiff(int hourdiff) {
        this.hourdiff = hourdiff;
    }

    //TIME DIFF
    private int hourdiff;
    //TIME AND LOCATION USED TO SHOW ROUTE INFORMATION ON MAIN PAGE
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;




    public int getCyear() {
        return cyear;
    }

    public void setCyear(int cyear) {
        this.cyear = cyear;
    }

    public int getCmonth() {
        return cmonth;
    }

    public void setCmonth(int cmonth) {
        this.cmonth = cmonth;
    }

    public int getCday() {
        return cday;
    }

    public void setCday(int cday) {
        this.cday = cday;
    }

    private static final CalendarModel ourInstance = new CalendarModel();
    static CalendarModel getInstance()
    {
        return ourInstance;
    }

    public void addEvent(){
        //renew the nearest event's time and information

        initObservers();
    }
}
