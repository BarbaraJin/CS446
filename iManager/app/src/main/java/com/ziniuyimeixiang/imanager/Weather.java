package com.ziniuyimeixiang.imanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Observable;
import java.util.Observer;

public class Weather extends AppCompatActivity implements Observer {
    Model mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = Model.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
    }
    public void update(Observable o, Object arg) {

    }
}
