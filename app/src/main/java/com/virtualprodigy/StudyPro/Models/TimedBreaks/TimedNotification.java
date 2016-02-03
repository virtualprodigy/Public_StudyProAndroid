package com.virtualprodigy.studypro.Models.TimedBreaks;

/**
 * Created by virtualprodigyllc on 2/2/16.
 */
public class TimedNotification {
    private int messageAtMin;
    private boolean isBreak;
    private String message;

    public TimedNotification() {
    }

    public int getMessageAtMin() {
        return messageAtMin;
    }

    public void setMessageAtMin(int messageAtMin) {
        this.messageAtMin = messageAtMin;
    }

    public boolean isBreak() {
        return isBreak;
    }

    public void setBreak(boolean aBreak) {
        isBreak = aBreak;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
