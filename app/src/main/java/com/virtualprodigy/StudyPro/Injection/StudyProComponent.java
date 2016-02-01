package com.virtualprodigy.studypro.Injection;

import com.virtualprodigy.studypro.StudyProActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by virtualprodigyllc on 02/01/16.
 */
@Component(modules = {SingletonModule.class, ConfigurationModule.class})
@Singleton
public interface StudyProComponent {
    void inject(StudyProActivity studyProActivity);
}
