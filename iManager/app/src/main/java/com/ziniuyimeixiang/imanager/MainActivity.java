package com.ziniuyimeixiang.imanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
