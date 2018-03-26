package com.ziniuyimeixiang.imanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;


/**
 * A simple {@link Fragment} subclass.
 */
public class MaleClothFragment extends Fragment implements Observer {

    /* weather param */
    WeatherData weatherData;
    private int lowTemp, highTemp, currentTemp, weatherCode, windSpeed;

    /* clothes param */
    ClothesModel clothesData;

    public MaleClothFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initiateWeatherInfo();
        initiateClothesInfo();

        startClothTask();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_male_cloth, container, false);
    }

    /**
     * Clothes section
     */


    private void initiateClothesInfo() {
        clothesData = ClothesModel.getInstance();
        clothesData.addObserver(this);

    }

    private void changeClothes() {
        clothesData.changeMaleClothesDependonTemp(currentTemp, windSpeed, weatherCode);
    }

    private void updateClothes() {
//        lowerCloth.setText(clothesData.getLowerCloth());
//        upperCloth.setText(clothesData.getUpperCloth());
    }

    private void startClothTask() {
        getWeatherInfo();
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

    private void getWeatherInfo() {
        lowTemp = weatherData.getLowTemp();
        highTemp = weatherData.getHighTemp();
        currentTemp = weatherData.getCurrentTemp();
        weatherCode = weatherData.getCurrentWeatherCode();
        windSpeed = weatherData.getSpeed();
    }

    @Override
    public void update(Observable observable, Object o) {
        updateClothes();
    }

}
