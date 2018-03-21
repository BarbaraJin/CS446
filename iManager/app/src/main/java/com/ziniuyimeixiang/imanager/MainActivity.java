package com.ziniuyimeixiang.imanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer{

    Model mModel;
    Button C_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = Model.getInstance();
        mModel.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent alarm = new Intent(MainActivity.this,Alarm.class);
//        startActivity(alarm);
        C_Button = findViewById(R.id.Cbutton);
        this.setCalender();
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
    public void transClick(View view){
        Intent weatherIntent = new Intent(MainActivity.this, TransActivity.class);
        startActivity(weatherIntent);
    }
    public void setCalender(){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String y = date.substring(0,4);
        String m = date.substring(5,7);
        String d = date.substring(8,10);
        String location;
        int time;
        String event_name;
        int myear = Integer.parseInt(y);
        int mmonth = Integer.parseInt(m);
        int mday = Integer.parseInt(d);
        long currentTime = System.currentTimeMillis();
        Cursor cursor = null;
        String selection = "(dtstart >= "+currentTime+")";
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
        C_Button.setText(start+title+location);
    }


    public void update(Observable o, Object arg) {
        this.setCalender();
    }
}
