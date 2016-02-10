package com.virtualprodigy.studypro.ottoBus.busPostEvents;

import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by virtualprodigyllc on 11/16/15.
 */
public class ExpandImageEvent {

    public ImageView view;
    public Uri uri;

    public ExpandImageEvent(ImageView view, Uri uri) {
        this.view = view;
        this.uri = uri;
    }
}
