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



    //TIME DIFF
    private int hourdiff;
    //TIME AND LOCATION USED TO SHOW ROUTE INFORMATION ON MAIN PAGE
    private String location;
    private String time;
    //current location
    private double cur_lon;
    private double cur_att;

    private double event_lon;
    private  double event_att;

    public double getEvent_lon() {
        return event_lon;
    }

    public void setEvent_lon(double event_lon) {
        this.event_lon = event_lon;
    }

    public double getEvent_att() {
        return event_att;
    }

    public void setEvent_att(double event_att) {
        this.event_att = event_att;
    }

    public double getCur_lon() {
        return cur_lon;
    }

    public void setCur_lon(double cur_lan) {
        this.cur_lon = cur_lan;
    }

    public double getCur_att() {
        return cur_att;
    }

    public void setCur_att(double cur_att) {
        this.cur_att = cur_att;
    }

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

    public int getHourdiff() {
        return hourdiff;
    }

    public void setHourdiff(int hourdiff) {
        this.hourdiff = hourdiff;
    }

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
    public void curr_postion(double l, double a){
        this.setCur_att(a);
        this.setCur_lon(l);
        initObservers();
    }
}
