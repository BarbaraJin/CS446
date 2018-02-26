package com.ziniuyimeixiang.imanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import android.database.Cursor;
public class specificDay extends AppCompatActivity implements Observer {
    Model mModel;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_day);
        mModel = Model.getInstance();
        setTitle(mModel.getCyear()+"/"+(mModel.getCmonth()+1)+"/"+mModel.getCday());

        //get hour diffenerce between time zone
        long currentTime = System.currentTimeMillis();
        int calOffset = TimeZone.getTimeZone("UTC").getOffset(currentTime);
        int localOffset = TimeZone.getDefault().getOffset(currentTime);
        int hourDifference = (calOffset - localOffset) / (1000 * 60 * 60);

        int year = mModel.getCyear();
        int month = mModel.getCmonth();
        int day = mModel.getCday();
        // query events on this day from calendar
        Calendar c_start= Calendar.getInstance();
        c_start.set(year,month,day-1,23,59);
        Calendar c_end= Calendar.getInstance();

        c_end.set(year,month, day,23,59);

        long mil_start = c_start.getTimeInMillis()-(60*60*1000)*hourDifference;
        long mil_end = c_end.getTimeInMillis() - (60*60*1000)*hourDifference;
        String selection = "((dtstart >= "+mil_start+") AND (dtstart <= "+mil_end+"))";

        //get permission
        if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, 120);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        //getcursor
        else {
            cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, selection, null, null);
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor != null) {
                    int id1 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                    int id2 = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                    int id3 = cursor.getColumnIndex(CalendarContract.Events.DTEND);
                    int id4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                    int id5 = cursor.getColumnIndex(CalendarContract.Events.EVENT_TIMEZONE);
                    String x = cursor.getString(id5);


                    //display the event
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    String title = cursor.getString(id1);
                    Date start_date = new Date(Long.valueOf(cursor.getString(id2)) + (60 * 60 * 1000) * hourDifference);
                    Date end_date = new Date(Long.valueOf(cursor.getString(id3)) + (60 * 60 * 1000) * hourDifference);
                    String start = formatter.format(start_date);
                    String end = formatter.format(end_date);
                    String location = cursor.getString(id4);
                    Toast.makeText(this, title + "," + start + "," + end + "," + location, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "no more", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public void update(Observable o, Object arg) {

    }
}
