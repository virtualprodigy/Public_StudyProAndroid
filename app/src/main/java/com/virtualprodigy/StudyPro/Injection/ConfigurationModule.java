package com.virtualprodigy.studypro.Injection;

import android.content.Context;

import com.virtualprodigy.studypro.StudyProApplication;

import dagger.Module;

/**
 * Created by virtualprodigyllc on 02/01/16.
 */
@Module
public class ConfigurationModule {

    private Context context;

    public ConfigurationModule(StudyProApplication application) {
        this.context = application;
    }
}
