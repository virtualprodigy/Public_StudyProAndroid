package com.virtualprodigy.studypro.StudyTimer;


import android.app.Activity;
import android.content.Context;
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

import javax.inject.Inject;

public class TimedBreaks {
    private String TAG = this.getClass().getSimpleName();
    private long timerDuration;
    private boolean isBreak;
    private Context context;
    private StudyDurations studyDurations;

    @Inject Gson gson;

    public TimedBreaks(Context context) {
        this.context = context;
        ((StudyProApplication) ((Activity) context).getApplication()).getComponent().inject(this);
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
            InputStream inputStream = context.getResources().getAssets().open("file:///android_asset/timed_breaks.json");
            jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            //begin reading json
            JsonToken peekForType = jsonReader.peek();
            if (peekForType == JsonToken.BEGIN_ARRAY) {

                jsonReader.beginArray();

                while (jsonReader.hasNext()) {
                    String name = jsonReader.nextName();
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        TimedNotification timedNotification = gson.fromJson(jsonReader, TimedNotification.class);
                        studyDurations.addDurationNotification(name, timedNotification);
                    }
                    jsonReader.endArray();
                }
                jsonReader.endArray();
            }

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
}
