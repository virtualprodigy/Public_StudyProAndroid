package com.virtualprodigy.studypro.Injection;

import com.virtualprodigy.studypro.StudyProActivity;
import com.virtualprodigy.studypro.StudyTimer.StudyTimerFragment;
import com.virtualprodigy.studypro.StudyTimer.TimedBreaks;
import com.virtualprodigy.studypro.layouts.TimerDisplayLayout;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by virtualprodigyllc on 02/01/16.
 */
@Component(modules = {SingletonModule.class, ConfigurationModule.class})
@Singleton
public interface StudyProComponent {
    void inject(StudyProActivity studyProActivity);
    void inject(StudyTimerFragment studyTimerFragment);
    void inject(TimerDisplayLayout timerDisplayLayout);
    void inject(TimedBreaks timedBreaks);
}
