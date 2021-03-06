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
    WeatherData weatherModel;
    ClothesModel clothesData;
    Button C_Button;
    Button R_Button;


    private TextView weatherLocation;
    private TextView weatherTemp;
    private TextView weatherInfo;

    private TextView et;
    private TextView st;
    private TextView lt;
    private TextView rt;

    Button test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = CalendarModel.getInstance();
        mModel.addObserver(this);

        weatherModel = WeatherData.getInstance();
        weatherModel.setContext(this);
        weatherModel.setWeatherInfo();
        weatherModel.addObserver(this);
        // TODO somthing wrong in cloth update logic
//        clothesData = ClothesModel.getInstance();
//        clothesData.addObserver(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        st = findViewById(R.id.start_t);
        et = findViewById(R.id.event_t);
        lt = findViewById(R.id.location_t);
        rt = findViewById(R.id.route_t);
//        Intent alarm = new Intent(MainActivity.this,Alarm.class);
//        startActivity(alarm);
        this.askP();

        //this.getLocationFromAddress(this,mModel.getLocation());
        this.getPosition();
        this.setCalender();
        mModel.route();
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
                while(cursor.moveToNext()) {
                    int id1 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                    int id2 = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                    int id4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);

                    //display the event
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    String title = cursor.getString(id1);
                    Date start_date = new Date(Long.valueOf(cursor.getString(id2)));
                    String start = formatter.format(start_date);
                    location = cursor.getString(id4);
                    et.setText(title);
                    st.setText(start);
                    lt.setText(location);
                    if (!location.equals("")) {
                        mModel.setLocation(location);
                        mModel.setTime(start);
                        break;
                    }
                }
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
        rt.setText("You need "+estimatedDriveTimeInMinutes+" minutes to reach your event location.");
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

    /**
     * Weather section
     */
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
    /**
     * update function
     * @param o
     * @param arg
     */

    public void update(Observable o, Object arg) {
        if (arg == null){
            updateWeather();
        }



        if ((String)arg == "new event") {
            this.setCalender();
        }
        if ((String)arg == "new route"){
            //getLocationFromAddress(this,mModel.getLocation());

            mModel.route();
            this.setRoute();
        }
        if ((String)arg == "new position") {
            //toronto should be replaced by the nearest event position
            this.setRoute();
        }

    }
}
