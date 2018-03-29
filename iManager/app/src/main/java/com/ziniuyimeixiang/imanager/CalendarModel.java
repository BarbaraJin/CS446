package com.ziniuyimeixiang.imanager;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Response;

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
    private String location = "toronto";
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
        if (!location.equals("")){
            this.location = location;
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        setChanged();
        notifyObservers("new route");
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

    //get information of next event location
    public String getLocationInfo() {
        try {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            Log.d("1","1");
            String address = this.getLocation().replaceAll(" ", "%20");
            connection = (HttpURLConnection) (new URL("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false")).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }
            return buffer.toString();


        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    public void getLatLong(JSONObject jsonObject) {

        try {

            double longitute = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            setEvent_att(latitude);
            setEvent_lon(longitute);
        } catch (JSONException e) {

        }
    }
    public void route(){
        Eventposition ep = new Eventposition();
        ep.execute(new String[]{location});
    }
    private class Eventposition extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String JsonWeatherData = getLocationInfo();
            try {
                getLatLong(new JSONObject(JsonWeatherData));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setChanged();
            notifyObservers("new position");
        }


    }
    public void addEvent(){
        //renew the nearest event's time and information

        setChanged();
        notifyObservers("new event");
    }
    public void curr_postion(double l, double a){
        this.setCur_att(a);
        this.setCur_lon(l);
        setChanged();
        notifyObservers("new position");
    }
}
