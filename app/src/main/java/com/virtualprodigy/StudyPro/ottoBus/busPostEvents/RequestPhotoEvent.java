package com.virtualprodigy.studypro.ottoBus.busPostEvents;

/**
 * Created by virtualprodigyllc on 6/9/15.
 */
public class RequestPhotoEvent {

    public static final int ACTION_GALLERY = 1;
    public static final int ACTION_CAMERA = 2;

    public int action;

    /**
     * The constructor is for the event bus. Using a predefined
     * action code the events notifies the subscribes to pull a photo from
     * the gallery or camera
     * @param action
     */
    public RequestPhotoEvent(int action) {
        this.action = action;
    }
}
