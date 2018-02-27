package com.ziniuyimeixiang.imanager;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.app.AlarmManager;
import java.util.List;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

 class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.info_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlarmAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        Log.d("ONCREATE", "system created a view");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mDataset[position]);
        Log.d("ONBIND","system binded" + mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

//class SingleAlarm extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent)
//    {
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, android.app.NotificationChannel.DEFAULT_CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Alarm")
//                .setContentText("Alarm!!!!!!")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_MAX);
//        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
//        nm.notify(1,mBuilder.build());
//    }
//
//    public void setAlarm(Context context, long set_time)
//    {
//        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, SingleAlarm.class);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
//        am.set(AlarmManager.RTC_WAKEUP, set_time,pi);
//    }
//
////    public void cancelAlarm(Context context)
////    {
////        Intent intent = new Intent(context, Alarm.class);
////        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
////        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
////        alarmManager.cancel(sender);
////    }
//}


public class Alarm extends AppCompatActivity implements Observer {
    Model mModel;
    private RecyclerView alarm_recycle_view;
    private RecyclerView.Adapter alarm_adapter;
    private RecyclerView.LayoutManager alarm_layout_manager;


//    class AlarmWrapper {
//        long triger_time;
//        SingleAlarm alarm;
//        public AlarmWrapper(long triger_time, Context context) {
//            this.triger_time = triger_time;
//            alarm = new SingleAlarm();
//            alarm.setAlarm(context,triger_time);
//        }
//    }
    private List alarms = new ArrayList();
//    testing
    private String[] mdata = new String[6];

//    private void Setalarm(long triger_time) {
//        alarms.add(new AlarmWrapper(triger_time,getApplicationContext()));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = Model.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarm_recycle_view = findViewById(R.id.alarm_recycler_view);
//        alarm_recycle_view.setHasFixedSize(true);

        alarm_layout_manager = new LinearLayoutManager(this);
        alarm_recycle_view.setLayoutManager(alarm_layout_manager);

        mdata[0] = "hi";
        mdata[1] = "world1";
        mdata[2] = "world2";
        mdata[3] = "world3";
        mdata[4] = "world4";
        mdata[5] = "world5";

        alarm_adapter = new AlarmAdapter(mdata);
        alarm_recycle_view.setAdapter(alarm_adapter);
//        Setalarm(System.currentTimeMillis() + 3000);
    }
    public void update(Observable o, Object arg) {

    }
}