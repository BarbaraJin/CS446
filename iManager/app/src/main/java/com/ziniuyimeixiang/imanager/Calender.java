package com.ziniuyimeixiang.imanager;

import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

public class Calender extends AppCompatActivity implements Observer {
    CalendarModel mModel;
    Button add_event;
    CalendarView mCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        mCalendarView = findViewById(R.id.calendarView);
        mModel = CalendarModel.getInstance();
        mModel.addObserver(this);
        setTitle("Calender");
        add_event = findViewById(R.id.add_event);
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call newgame activity
                Intent i = new Intent(getApplicationContext(), ADD_eventActivity.class);
                startActivity(i);
            }
        });
//        Calendar cal = new GregorianCalendar();
//        cal.setTime(new Date());
//        cal.add(Calendar.MONTH, 2);
//        long time = cal.getTime().getTime();
//        Uri.Builder builder =
//                CalendarContract.CONTENT_URI.buildUpon();
//        builder.appendPath("time");
//        builder.appendPath(Long.toString(time));
//        Intent intent =
//                new Intent(Intent.ACTION_VIEW, builder.build());
//        startActivity(intent);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                mModel.setCyear(i);
                mModel.setCmonth(i1);
                mModel.setCday(i2);
                Intent j = new Intent(getApplicationContext(), specificDay.class);
                startActivity(j);
            }
        });
    }
    public void update(Observable o, Object arg) {

    }
}
