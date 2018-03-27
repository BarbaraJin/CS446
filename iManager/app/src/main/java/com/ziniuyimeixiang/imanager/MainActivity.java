package com.ziniuyimeixiang.imanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Observer {

    CalendarModel mModel;
    Button C_Button;
    Button R_Button;
    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = CalendarModel.getInstance();
        mModel.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent alarm = new Intent(MainActivity.this,Alarm.class);
//        startActivity(alarm);
        C_Button = findViewById(R.id.Cbutton);
        R_Button = findViewById(R.id.Trans);
        this.askP();
        mModel.route();
        this.getPosition();
        this.setCalender();
        this.setRoute();

    }

    public void askP(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
    }
    // TODO need to change main activity's view
    public void weatherBottonClicked(View view) {
        Intent weatherIntent = new Intent(MainActivity.this, Weather.class);
        startActivity(weatherIntent);
    }

    public void calenderBottonClicked(View view) {
        Intent weatherIntent = new Intent(MainActivity.this, Calender.class);
        startActivity(weatherIntent);
    }

    public void transClick(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+mModel.getLocation()));
        startActivity(intent);
    }

    public void setCalender() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String y = date.substring(0, 4);
        String m = date.substring(5, 7);
        String d = date.substring(8, 10);
        String location;
        int time;
        String event_name;
        int myear = Integer.parseInt(y);
        int mmonth = Integer.parseInt(m);
        int mday = Integer.parseInt(d);
        long currentTime = System.currentTimeMillis();
        Cursor cursor = null;
        String selection = "(dtstart >= " + currentTime + ")";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, 120);
                }
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            }
            //getcursor
            else {
                cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, selection, null, CalendarContract.Events.DTSTART + " ASC");
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
                C_Button.setText(start + title + location);
                mModel.setLocation(location);
                mModel.setTime(start);
            }
        }
    }

    public void setRoute() {
        //replace with google map api later
        float distance;
        Location locationA = new Location("A");
        locationA.setLatitude(mModel.getCur_att());
        locationA.setLongitude(mModel.getCur_lon());

        Location locationB = new Location("B");
        locationB.setLatitude(mModel.getEvent_att());
        locationB.setLongitude(mModel.getEvent_lon());

        distance = locationA.distanceTo(locationB) / 1000;

        int speedIs1KmMinute = 100;
        float estimatedDriveTimeInMinutes = distance / speedIs1KmMinute;
        R_Button.setText("distance is " + distance + "time needed is " + estimatedDriveTimeInMinutes);
    }
    private boolean permission = false;
    public void getPosition() {
        Location location = null;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (permission) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            mModel.curr_postion(longitude, latitude);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permission = true;
                }else{
                    permission = false;
                }
                break;
        }
    }

    public void update(Observable o, Object arg) {
        if ((String)arg == "new event") {
            this.setCalender();
        }
        if ((String)arg == "new route"){
            mModel.route();
            this.setRoute();
        }
        if ((String)arg == "new position") {
            //toronto should be replaced by the nearest event position
            this.setRoute();
        }
    }
}
