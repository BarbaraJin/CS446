package com.ziniuyimeixiang.imanager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.Build;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import android.provider.CalendarContract;
import android.widget.Toast;

public class ADD_eventActivity extends AppCompatActivity implements Observer {
    EditText DateInput;
    EditText NameInput;
    EditText BeginTime;
    EditText EndTime;
    EditText Location;
    Button Save_event;

    Model mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = Model.getInstance();
        Save_event = findViewById(R.id.save);
        setTitle("New Event");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }
    public void SaveClick (View v){
        //save the information provided by the user
        DateInput  = findViewById(R.id.dateInput);
        NameInput = findViewById(R.id.eventName);
        BeginTime = findViewById(R.id.beginTime);
        EndTime = findViewById(R.id.endTime);
        Location = findViewById(R.id.Location);

        ContentResolver cr = this.getContentResolver();
        ContentValues cv = new ContentValues();

        cv.put(CalendarContract.Events.TITLE, NameInput.getText().toString());
        cv.put(CalendarContract.Events.EVENT_LOCATION, Location.getText().toString());
        //start and end date
        String date = DateInput.getText().toString();
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7))-1;
        int day = Integer.parseInt(date.substring(8,10));
        //start time
        String s_time = BeginTime.getText().toString();
        int s_hour = Integer.parseInt(s_time.substring(0,2));
        int s_minute = Integer.parseInt(s_time.substring(3,5));
        //end time
        String e_time = EndTime.getText().toString();
        int e_hour = Integer.parseInt(e_time.substring(0,2));
        int e_minute = Integer.parseInt(e_time.substring(3,5));

        Calendar start = Calendar.getInstance();
        start.set(year, month, day, s_hour, s_minute, 0);

        Calendar end = Calendar.getInstance();
        end.set(year, month, day, e_hour, e_minute, 0);

        long startTime = start.getTimeInMillis();
        long endTime = end.getTimeInMillis();

        cv.put(CalendarContract.Events.DTSTART, startTime);
        cv.put(CalendarContract.Events.DTEND, endTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            cv.put(CalendarContract.Events.CALENDAR_ID, 3);
        }else{
            cv.put(CalendarContract.Events.CALENDAR_ID, 1);
        }

        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, 120);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
                // Android version is lesser than 6.0 or the permission is already granted.
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
            }
        //change the data in model
        mModel.addEvent();
        this.finish();
    }
//    public void test(View v){
//        if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, 120);
//            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
//        }
//        else {
//            cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
//        }
//        while(cursor.moveToNext()){
//            if (cursor != null){
//                int id1 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
//                int id2 = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
//                int id3 = cursor.getColumnIndex(CalendarContract.Events.DTEND);
//                int id4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
//
//                String title = cursor.getString(id1);
//                String start = cursor.getString(id2);
//                String end = cursor.getString(id3);
//                String location = cursor.getString(id4);
//                Toast.makeText(this, title+","+start+","+end+","+location, Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(this, "no more", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions,
        int[] grantResults) {
            if (requestCode == 120) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted
                } else {
                    Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
                }
            }
        }

    public void update(Observable o, Object arg) {

    }
}
