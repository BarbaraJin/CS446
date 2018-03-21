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
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    CalendarModel mModel;
    WeatherData weatherModel;
    Button C_Button;
    Button R_Button;

    private TextView weatherLocation;
    private TextView weatherTemp;
    private TextView weatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = CalendarModel.getInstance();
        mModel.addObserver(this);
        weatherModel = WeatherData.getInstance();
        weatherModel.addObserver(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent alarm = new Intent(MainActivity.this,Alarm.class);
//        startActivity(alarm);
        C_Button = findViewById(R.id.Cbutton);
        R_Button = findViewById(R.id.Trans);
        this.askP();
        this.getLocationFromAddress(this, "Toronto");
        this.getPosition();
        this.setCalender();
        this.setRoute();
        //toronto should be replaced by the nearest event position

        /* weather button*/
        setWeatherTextView();
        getWeatherInfo();

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
        Intent weatherIntent = new Intent(MainActivity.this, TransActivity.class);
        startActivity(weatherIntent);
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
        C_Button.setText(start + title + location);
        mModel.setLocation(location);
        mModel.setTime(start);
    }

    public void setRoute() {
        double lat = 23.33;
        double lng = 25.88;
        float distance;
        Location locationA = new Location("A");
        locationA.setLatitude(mModel.getCur_att());
        locationA.setLongitude(mModel.getCur_lon());

        Location locationB = new Location("B");
        locationB.setLatitude(mModel.getEvent_att());
        locationB.setLongitude(mModel.getEvent_lon());

        distance = locationA.distanceTo(locationB) / 1000;

        LatLng From = new LatLng(lat, lng);
        LatLng To = new LatLng(lat, lng);


        int speedIs1KmMinute = 100;
        float estimatedDriveTimeInMinutes = distance / speedIs1KmMinute;
        R_Button.setText("distance is " + distance + "time needed is " + estimatedDriveTimeInMinutes);
    }
    private boolean permission = false;
    public void getPosition() {
        Location location = null;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (permission) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            mModel.curr_postion(longitude, latitude);
        }
    }
    public void getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        mModel.setEvent_att(p1.lat);
        mModel.setEvent_lon(p1.lng);
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

    public void setWeatherTextView(){
        weatherLocation = findViewById(R.id.locationOnWeatherButton);
        weatherTemp = findViewById(R.id.tempOnWeatherButton);
        weatherInfo = findViewById(R.id.weatherInfoOnWeatherButton);
    }

    public void getWeatherInfo(){
        String defaultLocation = weatherModel.getDefaultCityRegion();
        weatherModel.setCurrentLocation(defaultLocation);
    }

    public void updateWeather(){
        weatherLocation.setText(weatherModel.getCity() + ", " + weatherModel.getRegion());
        weatherTemp.setText(weatherModel.getCurrentTemp() + "°C");
        weatherInfo.setText(weatherModel.getWeatherText());
    }

    public void update(Observable o, Object arg) {
        this.setCalender();
        this.setRoute();
        //toronto should be replaced by the nearest event position
        this.getLocationFromAddress(this, "Toronto");

        updateWeather();

    }
}
