package com.ziniuyimeixiang.imanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class ClothActivity extends AppCompatActivity implements Observer {

    WeatherData weatherData;
    ClothesModel clothesData;


    private int lowTemp, highTemp, currentTemp, weatherCode, windSpeed;

    private FloatingActionButton mainFloatingButton, weatherFloatingButton, homeFloatingButton;
    private Boolean mainFabOpen;

    private Animation fabOpen, fabClose, fabRotateClock, fabRotateAntiClock;

    /* nav param */
    private BottomNavigationView clothNavigationView;
    private FrameLayout clothFrameLayout;
    private FemaleClothFragment femaleClothFragment;
    private MaleClothFragment maleClothFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearing);
        getSupportActionBar().setTitle("Clothes");

        initiateWeatherInfo();
        initiateClothesInfo();

        initiateFloatingButton();
        listenFABButton();

        initiateTabButton();
        listenTabButton();

        startClothTask();
    }



    /**
     * Clothes section
     */


    private void initiateClothesInfo() {
        clothesData = ClothesModel.getInstance();
        clothesData.addObserver(this);

    }

    private void changeClothes() {
        clothesData.changeClothesDependonTemp(currentTemp, windSpeed, weatherCode);
    }

    private void updateClothes() {
//        lowerCloth.setText(clothesData.getLowerCloth());
//        upperCloth.setText(clothesData.getUpperCloth());
    }


    private void startClothTask() {
        updateWeather();
        changeClothes();
    }


    /**
     * weather section
     */
    private void initiateWeatherInfo() {
        weatherData = WeatherData.getInstance();
//        weatherData.addObserver(this);
        lowTemp = 0;
        highTemp = 0;
    }

    private void updateWeather() {
        lowTemp = weatherData.getLowTemp();
        highTemp = weatherData.getHighTemp();
        currentTemp = weatherData.getCurrentTemp();
        weatherCode = weatherData.getCurrentWeatherCode();
        windSpeed = weatherData.getSpeed();
    }


    /**
     * floating button section
     */

    private void initiateFloatingButton() {
        mainFloatingButton = findViewById(R.id.floatingActionButton);
        weatherFloatingButton = findViewById(R.id.weatherFloatingButton);
        homeFloatingButton = findViewById(R.id.homeFloatingButton);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.float_button_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.float_button_close);
        fabRotateClock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise_45);
        fabRotateAntiClock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise_45);
        mainFabOpen = false;
    }

    public void listenFABButton(){
        weatherFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wearingIntent = new Intent(ClothActivity.this, Weather.class);
                startActivity(wearingIntent);
            }
        });

        homeFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ClothActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        mainFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mainFabOpen){
                    weatherFloatingButton.setClickable(false);
                    weatherFloatingButton.setVisibility(View.GONE);
                    homeFloatingButton.setClickable(false);
                    homeFloatingButton.setVisibility(View.GONE);
                    mainFloatingButton.startAnimation(fabRotateAntiClock);
                    mainFabOpen = false;
                }
                else{
                    weatherFloatingButton.setClickable(true);
                    weatherFloatingButton.setVisibility(View.VISIBLE);
                    homeFloatingButton.setClickable(true);
                    homeFloatingButton.setVisibility(View.VISIBLE);
                    mainFloatingButton.startAnimation(fabRotateClock);
                    mainFabOpen = true;
                }

            }
        });

    }

    /**
     * tab button section
     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.femaleCloth){
//            isFemale = true;
//            setContentView(R.layout.activity_wearing);
//        }
//        else if (item.getItemId() == R.id.maleCloth){
//            isFemale = false;
//            setContentView(R.layout.activity_male_cloth);
//        }
//        return super.onOptionsItemSelected(item);
//    }


    private void listenTabButton() {
        clothNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.male:
                        setFragment(maleClothFragment);
//                        Intent maleIntent = new Intent(ClothActivity.this, MaleClothActivity.class);
//                        startActivity(maleIntent);
                        return true;

                    case R.id.female:
                        setFragment(femaleClothFragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.clothFrame, fragment).commit();
    }


    private void initiateTabButton() {
        clothNavigationView = findViewById(R.id.clothNav);
        clothFrameLayout = findViewById(R.id.clothFrame);
        femaleClothFragment = new FemaleClothFragment();
        maleClothFragment = new MaleClothFragment();
        setFragment(femaleClothFragment);
    }


    @Override
    public void update(Observable observable, Object o) {
        updateClothes();
    }


}