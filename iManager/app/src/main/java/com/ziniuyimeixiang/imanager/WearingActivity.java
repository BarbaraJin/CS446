package com.ziniuyimeixiang.imanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class WearingActivity extends AppCompatActivity implements Observer {

    WeatherData weatherData;
    ClothesModel clothesData;

    private TextView lowerCloth, upperCloth;

    private int lastLowTemp, lastHighTemp, lowTemp, highTemp;
    private  Boolean tempChanged;


    private FloatingActionButton mainFloatingButton, weatherFloatingButton, homeFloatingButton;
    private Boolean mainFabOpen;

    private Animation fabOpen, fabClose, fabRotateClock, fabRotateAntiClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearing);
        getSupportActionBar().setTitle("Clothes");

        initiateWeatherInfo();
        initiateClothesInfo();

        initiateFloatingButton();
        listenFABButton();

        startClothTask();
    }

    /**
     * Clothes section
     */


    private void initiateClothesInfo() {
        clothesData = ClothesModel.getInstance();
        clothesData.addObserver(this);

        lowerCloth = findViewById(R.id.lowerBodySuggestionText);
        upperCloth = findViewById(R.id.upperBodySuggestionText);
    }

    private void changeClothes() {
        clothesData.changeClothesDependonTemp(lowTemp, highTemp);
    }


    private void updateClothes() {
        lowerCloth.setText(clothesData.getLowerCloth());
        upperCloth.setText(clothesData.getUpperCloth());
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
        weatherData.addObserver(this);
        lastLowTemp = 0;
        lastHighTemp = 0;
        lowTemp = 0;
        highTemp = 0;
    }

    private void updateWeather() {
        lowTemp = weatherData.getLowTemp();
        highTemp = weatherData.getHighTemp();
    }

    private Boolean checkTempChanged() {
        Boolean answer =  !(lowTemp == lastLowTemp && highTemp == lastHighTemp);
        lastLowTemp = lowTemp;
        lastHighTemp = highTemp;
        return answer;
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
                Intent wearingIntent = new Intent(WearingActivity.this, Weather.class);
                startActivity(wearingIntent);
            }
        });

        homeFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(WearingActivity.this, MainActivity.class);
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

    @Override
    public void update(Observable observable, Object o) {
        updateWeather();
        tempChanged = checkTempChanged();
        if (tempChanged){
            changeClothes();
        }
        updateClothes();
    }


}
