package com.ziniuyimeixiang.imanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{

    Model mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = Model.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent alarm = new Intent(MainActivity.this,Alarm.class);
//        startActivity(alarm);
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
    public void update(Observable o, Object arg) {

    }
}
