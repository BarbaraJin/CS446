package com.ziniuyimeixiang.imanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class Weather extends AppCompatActivity implements Observer {

    private ImageView weatherIcon;
    private TextView temperature;
    private TextView highLowTemp;
    private TextView cityRegion;
    private TextView wind;
    private TextView humidity;
    private TextView visibility;
    private TextView sunset;
    private TextView sunrise;
    private TextView location, hardTemperature, hardWind, hardVisibility, hardHuminidity, hardSunrise, hardSunset;
    private Bitmap bitmapIcon;

    WeatherData weatherData;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private FloatingActionButton mainFloatingButton, clothFloatingButton, homeFloatingButton;
    private Boolean mainFabOpen;

    private ConstraintLayout constraintLayout;
    private int weatherCode;

    private Animation fabOpen, fabClose, fabRotateClock, fabRotateAntiClock;

    /**
     * do weather task of default city
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherData = WeatherData.getInstance();
        weatherData.addObserver(this);

        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this,null);

        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        temperature = (TextView) findViewById(R.id.currentTempValue);
        highLowTemp = (TextView) findViewById(R.id.highLowTempValue);
        cityRegion = (TextView) findViewById(R.id.locationText);
        wind = (TextView) findViewById(R.id.windValue);
        humidity = (TextView) findViewById(R.id.humidityValue);
        visibility = (TextView) findViewById(R.id.visibilityValue);
        sunset = (TextView) findViewById(R.id.sunsetValue);
        sunrise = (TextView) findViewById(R.id.sunriseValue);
        location = findViewById(R.id.locationText);
        hardTemperature = findViewById(R.id.hardcodeTemperatureTextText);
        hardWind = findViewById(R.id.hardcodeWindTextText);
        hardVisibility = findViewById(R.id.hardcodeVisibilityText);
        hardHuminidity = findViewById(R.id.hardcodeHumidityText);
        hardSunrise = findViewById(R.id.hardcodeSunriseText);
        hardSunset = findViewById(R.id.hardcodeSunsetText);

        constraintLayout = findViewById(R.id.weatherConstraintLayout);

        initiateFloatingButton();
        listenFABButton();

        /* check if network is available */
        if (isNetworkConnected(Weather.this)){
            String defaultCity = weatherData.getDefaultCityRegion();
            doWeatherTask(defaultCity); // TODO add search location function
        }
        else{
            networkFailDialog(Weather.this).show();
        }

        // TODO get current location

    }

    /**
     * if item clicked, do something
     *      if search item: call get city from search item
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ((item.getItemId()) == R.id.app_location_search) {
            getCityFromSearchItem();
//            getsupportMenu
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * check if network is connected
     */
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo lte = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((lte != null && lte.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * if net work connection fail, show dialog
     * @param c
     * @return
     */
    public AlertDialog.Builder networkFailDialog(Context c) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(c);
        alertBuilder.setTitle("No Internet Connection");
        alertBuilder.setMessage("You need to connect wifi or open mobil data to access. Press OK to Exit.");

        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        return alertBuilder;
    }

    /**
     * show dialog, get city from text input, do weather task
     */
    private void getCityFromSearchItem() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    /**
     * get city name request code = 1
     *  if city changed, then call do weather task
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String cityRegionString = place.getAddress().toString();

                defaultLocationDialog(this, cityRegionString);
                /* before get json, check network*/
                if (isNetworkConnected(Weather.this)){
                    doWeatherTask(cityRegionString);
                }
                else{
                    networkFailDialog(Weather.this).show();
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void defaultLocationDialog(Context c, final String typedInLocation) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(c);
        alertBuilder.setTitle("default location");
        alertBuilder.setMessage("Do you want to set this to the default location?");

        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                weatherData.setDefaultCityRegion(typedInLocation);
            }
        });

        alertBuilder.setNegativeButton("ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        });
        alertBuilder.show();
    }

    /**
     * if change city, do weather task: fetch data, change UI
     * @param city
     */
    public void doWeatherTask(String city){
        weatherData.setCurrentLocation(city);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * floating button section
     */


    private void initiateFloatingButton() {
        mainFloatingButton = findViewById(R.id.floatingActionButton);
        clothFloatingButton = findViewById(R.id.weatherFloatingButton);
        homeFloatingButton = findViewById(R.id.homeFloatingButton);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.float_button_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.float_button_close);
        fabRotateClock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise_45);
        fabRotateAntiClock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise_45);
        mainFabOpen = false;
    }

    public void listenFABButton(){
        clothFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wearingIntent = new Intent(Weather.this, ClothActivity.class);
                startActivity(wearingIntent);
            }
        });

        homeFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(Weather.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        mainFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mainFabOpen){
                    clothFloatingButton.setClickable(false);
                    clothFloatingButton.setVisibility(View.GONE);
                    homeFloatingButton.setClickable(false);
                    homeFloatingButton.setVisibility(View.GONE);
                    mainFloatingButton.startAnimation(fabRotateAntiClock);
                    mainFabOpen = false;
                }
                else{
                    clothFloatingButton.setClickable(true);
                    clothFloatingButton.setVisibility(View.VISIBLE);
                    homeFloatingButton.setClickable(true);
                    homeFloatingButton.setVisibility(View.VISIBLE);
                    mainFloatingButton.startAnimation(fabRotateClock);
                    mainFabOpen = true;
                }

            }
        });

    }



    /**
     * update function
     * @param o
     * @param arg
     */
    public void update(Observable o, Object arg) {
        cityRegion.setText(weatherData.getCity() + ", " + weatherData.getRegion());
        temperature.setText(weatherData.getCurrentTemp() + "°C");
        highLowTemp.setText("(" + weatherData.getHighTemp() + "°C — " + weatherData.getLowTemp() + "°C" +")");
        wind.setText(weatherData.getSpeed() + " mph");
        humidity.setText(weatherData.getHumidity() + " %");
        visibility.setText(weatherData.getVisibility() + " mi");
        sunset.setText(weatherData.getSunset());
        sunrise.setText(weatherData.getSunrise());
        weatherIcon.setImageBitmap(weatherData.getWeatherIcon());
        weatherCode = weatherData.getCurrentWeatherCode();
        updateBackground();
    }

    private void updateBackground() {
        if (weatherCode == 3 || weatherCode == 4 || weatherCode == 45|| weatherCode == 47 || (weatherCode >=37 && weatherCode <= 39)){
            constraintLayout.setBackgroundResource(R.drawable.thunder);
        }
        else if (weatherCode == 5 || weatherCode == 7 || weatherCode == 46 || (weatherCode >=13 && weatherCode <= 16) || (weatherCode >=41 && weatherCode <= 43)){
            constraintLayout.setBackgroundResource(R.drawable.snow);
        }
        else if (weatherCode == 6 || (weatherCode >=8 && weatherCode <= 12) || weatherCode == 35 || weatherCode == 40){
            constraintLayout.setBackgroundResource(R.drawable.rain);
        }
        else if (weatherCode == 17 || weatherCode == 18){
            constraintLayout.setBackgroundResource(R.drawable.hail);
        }
        else if (weatherCode >=19 && weatherCode <= 22){
            constraintLayout.setBackgroundResource(R.drawable.foggy);
        }
        else if ((weatherCode >=26 && weatherCode <= 30) || weatherCode == 44){
            constraintLayout.setBackgroundResource(R.drawable.cloud);
        }
        else{
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            if (hour>= 7 && hour<=19){
                constraintLayout.setBackgroundResource(R.drawable.day);
            }
//            else{
//                constraintLayout.setBackgroundResource(R.drawable.night);
//                temperature.setTextColor(Color.WHITE);
//                highLowTemp.setTextColor(Color.WHITE);
//                cityRegion.setTextColor(Color.WHITE);
//                wind.setTextColor(Color.WHITE);
//                humidity.setTextColor(Color.WHITE);
//                visibility.setTextColor(Color.WHITE);
//                sunset.setTextColor(Color.WHITE);
//                sunrise.setTextColor(Color.WHITE);
//                location.setTextColor(Color.WHITE);
//                hardTemperature.setTextColor(Color.WHITE);
//                hardWind.setTextColor(Color.WHITE);
//                hardVisibility.setTextColor(Color.WHITE);
//                hardHuminidity.setTextColor(Color.WHITE);
//                hardSunrise.setTextColor(Color.WHITE);
//                hardSunset.setTextColor(Color.WHITE);
//                return;
//            }
        }
//        temperature.setTextColor(Color.BLACK);
//        highLowTemp.setTextColor(Color.BLACK);
//        cityRegion.setTextColor(Color.BLACK);
//        wind.setTextColor(Color.BLACK);
//        humidity.setTextColor(Color.BLACK);
//        visibility.setTextColor(Color.BLACK);
//        sunset.setTextColor(Color.BLACK);
//        sunrise.setTextColor(Color.BLACK);
//        location.setTextColor(Color.BLACK);
//        hardTemperature.setTextColor(Color.BLACK);
//        hardWind.setTextColor(Color.BLACK);
//        hardVisibility.setTextColor(Color.BLACK);
//        hardHuminidity.setTextColor(Color.BLACK);
//        hardSunrise.setTextColor(Color.BLACK);
//        hardSunset.setTextColor(Color.BLACK);
    }


}
