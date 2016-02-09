package com.virtualprodigy.studypro.ottoBus.busPostEvents;

import android.os.Bundle;

/**
 * Created by virtualprodigyllc on 2/9/16.
 */
public class LaunchNoteEvent {
    private Bundle bundle;

    public LaunchNoteEvent(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

}
