package com.virtualprodigy.studypro.Models.TimedBreaks;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by virtualprodigyllc on 2/2/16.
 */
public class StudyDurations {
    public HashMap<String, ArrayList<TimedNotification>> durationNotifications;

    public StudyDurations() {
        durationNotifications = new HashMap<>();
    }

    public HashMap<String, ArrayList<TimedNotification>> getDurationNotifications() {
        return durationNotifications;
    }

    public void setDurationNotifications(HashMap<String, ArrayList<TimedNotification>> durationNotifications) {
        this.durationNotifications = durationNotifications;
    }

    /**
     * Adds a timedNotification to the map of durationNotifications
     * @param key - the study time duration
     * @param timedNotification - a notifcation to appear during the timer
     */
    public void addDurationNotification(String key, TimedNotification timedNotification){
        if(durationNotifications.containsKey(key)){
            ArrayList<TimedNotification> timedNotifications = durationNotifications.get(key);
            timedNotifications.add(timedNotification);
            durationNotifications.put(key, timedNotifications);
        }else{
            ArrayList<TimedNotification> timedNotifications = new ArrayList<>();
            timedNotifications.add(timedNotification);
            durationNotifications.put(key, timedNotifications);

        }

    }
}
