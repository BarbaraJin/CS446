package com.ziniuyimeixiang.imanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Observable;
import java.util.Observer;


/**
 * A simple {@link Fragment} subclass.
 */
public class MaleClothFragment extends Fragment implements Observer {

    private View view;

    /* view param */
    private ImageButton hoodiesButton, leggingButton, jeansButton, springCoatButton, shortsButton, sweaterButton, tshirtButton, windProofJacketButton, winterCoatButton;

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
        view = inflater.inflate(R.layout.fragment_male_cloth, container, false);

        initiateWeatherInfo();
        initiateClothesInfo();
        initiateViewFromFrament(view);

        startClothTask();

        return view;
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
        if (clothesData.getHoodies()){
            hoodiesButton.setImageResource(R.drawable.blue_hoodies_button);
        }
        if (clothesData.getJeans()){
            jeansButton.setImageResource(R.drawable.blue_jeans_button);
        }
        if (clothesData.getLegging()){
            leggingButton.setImageResource(R.drawable.blue_legging_button);
        }
        if (clothesData.getSpringCoat()){
            springCoatButton.setImageResource(R.drawable.blue_male_spring_coat_button);
        }
        if (clothesData.getShorts()){
            shortsButton.setImageResource(R.drawable.blue_shorts_button);
        }
        if (clothesData.getSweater()){
            sweaterButton.setImageResource(R.drawable.blue_sweater_button);
        }
        if (clothesData.gettShirt()){
            tshirtButton.setImageResource(R.drawable.blue_tshirt_button);
        }
        if (clothesData.getWindAndWaterProofJacket()){
            windProofJacketButton.setImageResource(R.drawable.blue_wind_proof_jacket_button);
        }
        if (clothesData.getWinterCoat()){
            winterCoatButton.setImageResource(R.drawable.blue_round_winter_coat_button);
        }
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

    /**
     * view initiate
     */


    private void initiateViewFromFrament(View view) {
        hoodiesButton = view.findViewById(R.id.hoodiesButton);
        leggingButton = view.findViewById(R.id.leggingButton);
        jeansButton = view.findViewById(R.id.jeansButton);
        springCoatButton = view.findViewById(R.id.maleSpringCoatButton);
        shortsButton = view.findViewById(R.id.shortsButton);
        sweaterButton = view.findViewById(R.id.sweaterButton);
        tshirtButton = view.findViewById(R.id.tshirtButton);
        windProofJacketButton = view.findViewById(R.id.windProofJacketButton);
        winterCoatButton = view.findViewById(R.id.winterCoatButton);
    }

    @Override
    public void update(Observable observable, Object o) {
        updateClothes();
    }

}
