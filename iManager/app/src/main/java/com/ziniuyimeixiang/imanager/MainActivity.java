package com.ziniuyimeixiang.imanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{

    Model mModel;
    Button C_Button;
    Button R_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = Model.getInstance();
        mModel.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent alarm = new Intent(MainActivity.this,Alarm.class);
//        startActivity(alarm);
        C_Button = findViewById(R.id.Cbutton);
        R_Button = findViewById(R.id.Trans);
        this.setCalender();
        this.setRoute();
    }
    // TODO need to change main activity's view
    public void weatherBottonClicked(View view){
        Intent weatherIntent = new Intent(MainActivity.this, Weather.class);
        startActivity(weatherIntent);
    }

    public void calenderBottonClicked(View view){
        Intent weatherIntent = new Intent(MainActivity.this, Calender.class);
        startActivity(weatherIntent);
    }

    public void alarmBottonClicked(View view){
        Intent weatherIntent = new Intent(MainActivity.this, Alarm.class);
        startActivity(weatherIntent);
    }
    public void transClick(View view){
        Intent weatherIntent = new Intent(MainActivity.this, TransActivity.class);
        startActivity(weatherIntent);
    }
    public void setCalender(){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String y = date.substring(0,4);
        String m = date.substring(5,7);
        String d = date.substring(8,10);
        String location;
        int time;
        String event_name;
        int myear = Integer.parseInt(y);
        int mmonth = Integer.parseInt(m);
        int mday = Integer.parseInt(d);
        long currentTime = System.currentTimeMillis();
        Cursor cursor = null;
        String selection = "(dtstart >= "+currentTime+")";
        if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, 120);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        //getcursor
        else {
            cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, selection, null, CalendarContract.Events.DTSTART + " ASC");
        }
        cursor.moveToNext();
        int id1 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
        int id2 = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
        int id4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);

        //display the event
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String title = cursor.getString(id1);
        Date start_date = new Date(Long.valueOf(cursor.getString(id2)) + (60 * 60 * 1000) * mModel.getHourdiff());
        String start = formatter.format(start_date);
        location = cursor.getString(id4);
        C_Button.setText(start+title+location);
        mModel.setLocation(location);
        mModel.setTime(start);
    }
    public void setRoute(){
        double lat = 23.33;
        double lng = 25.88;
        float distance;
        Location locationA=new Location("A");
        locationA.setLatitude(lat);
        locationA.setLongitude(lng);

        Location locationB = new Location("B");
        locationB.setLatitude(lng);
        locationB.setLongitude(lat);

        distance = locationA.distanceTo(locationB)/1000;

        LatLng From = new LatLng(lat,lng);
        LatLng To = new LatLng(lat,lng);


        int speedIs1KmMinute = 100;
        float estimatedDriveTimeInMinutes = distance / speedIs1KmMinute;
        R_Button.setText("distance is "+distance+"time needed is " + estimatedDriveTimeInMinutes);
    }

    public void update(Observable o, Object arg) {
        this.setCalender();
    }
}
