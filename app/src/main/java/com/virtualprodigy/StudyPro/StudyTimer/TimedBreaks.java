package com.virtualprodigy.studypro.StudyTimer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.virtualprodigy.studypro.Models.TimedBreaks.StudyDurations;
import com.virtualprodigy.studypro.Models.TimedBreaks.TimedNotification;
import com.virtualprodigy.studypro.StudyProApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import javax.inject.Inject;

public class TimedBreaks {
    private String TAG = this.getClass().getSimpleName();
    private long timerDuration;
    private boolean isBreak;
    private Context context;
    private StudyDurations studyDurations;

    @Inject
    Gson gson;

    public TimedBreaks(Context context) {
        this.context = context;
        ((StudyProApplication) context.getApplicationContext()).getComponent().inject(this);
        studyDurations = new StudyDurations();
        parseTimedBreaks();
    }

    /**
     * Sets the duration of the timer
     *
     * @param timerDuration - duration of the timer in milliseconds
     */
    public void setTimerDuration(long timerDuration) {
        this.timerDuration = timerDuration;

    }

    /**
     * Sets the isBreak Flag
     *
     * @param isBreak - true or false
     */
    private void setBreak(boolean isBreak) {
        this.isBreak = isBreak;
    }

    /**
     * Returns the T/F flag if it is a break
     *
     * @return
     */
    protected boolean isBreak() {
        return isBreak;
    }

    private void parseTimedBreaks() {

        JsonReader jsonReader = null;
        try {
            InputStream inputStream = context.getResources().getAssets().open("timed_breaks.json");
            jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            //begin reading json
            JsonToken peekForType;
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                peekForType = jsonReader.peek();
                if (peekForType == JsonToken.NAME) {
                    //get the named of the array of timed notifications
                    String name = jsonReader.nextName();
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        TimedNotification timedNotification = gson.fromJson(jsonReader, TimedNotification.class);
                        studyDurations.addDurationNotification(name, timedNotification);
                    }

                    jsonReader.endArray();
                }
            }

            jsonReader.endObject();
        } catch (Exception e) {
        } finally {
            try {
                if (jsonReader != null) {
                    jsonReader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed closing Json reader", e);
            }
        }
    }

    /**
     * This method is called when the timer is started to schedule the alarms
     * and the break notification
     */
    public void scheduleAlarmPlusBreaks() {
//        TODO finish logic to create alarms
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
//        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
//        Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, 0);
//        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
//
//
//        else{
//            alarmManager.cancel(pendingIntent);
//            setAlarmText("");
//            Log.d("MyActivity", "Alarm Off");
//        }

    }

    /**
     * Converts the duration time to minutes
     *
     * @return
     */
    private int convertTimeMsToMins() {

        return -1;
    }
}
