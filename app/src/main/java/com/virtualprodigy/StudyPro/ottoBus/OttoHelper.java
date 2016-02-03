package com.virtualprodigy.studypro.ottoBus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by virtualprodigyllc on 6/6/15.
 */
public class OttoHelper extends Bus {
    private final String TAG = this.getClass().getSimpleName();
    //try to keep otto on the main thread so ui can be updated
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private static OttoHelper instance;

    public static OttoHelper getInstance() {
        if (instance == null) {
            instance = new OttoHelper();
        }
        return instance;
    }

    @Override
    public void register(Object object) {
        super.register(object);
        Log.i(TAG, "otto register for " + object.getClass().getSimpleName());
    }

    @Override
    public void unregister(Object object) {
        super.unregister(object);
        Log.i(TAG, "otto unregistered for " + object.getClass().getSimpleName());
    }

    @Override
    public void post(final Object event) {
        Log.i(TAG, "otto post for " + event.getClass().getSimpleName());
        //TODO check if otto's ThreadEnforce keeps everything to a thread regardless of it was received on a bad thread
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            try {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        OttoHelper.super.post(event);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "OttoHelper post failed" + e.getMessage(), e);
            }

        }
    }
}