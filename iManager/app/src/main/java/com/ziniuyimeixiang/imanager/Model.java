package com.ziniuyimeixiang.imanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Calendar;
import android.content.Context;
class Model extends Observable{
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

    private static final Model ourInstance = new Model();
    static Model getInstance()
    {
        return ourInstance;
    }

    public void addEvent(){
        //renew the nearest event's time and information

        initObservers();
    }
    public void initObservers()
    {

        setChanged();
        notifyObservers();
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    @Override
    public synchronized void deleteObserver(Observer o)
    {
        super.deleteObserver(o);
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    @Override
    public synchronized void deleteObservers()
    {
        super.deleteObservers();
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    @Override
    public void notifyObservers()
    {
        super.notifyObservers();
    }

}
