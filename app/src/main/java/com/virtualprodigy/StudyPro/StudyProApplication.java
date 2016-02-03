package com.virtualprodigy.studypro;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.virtualprodigy.studypro.Injection.ConfigurationModule;
import com.virtualprodigy.studypro.Injection.DaggerStudyProComponent;
import com.virtualprodigy.studypro.Injection.SingletonModule;
import com.virtualprodigy.studypro.Injection.StudyProComponent;

/**
 * Created by virtualprodigyllc on 9/14/15.
 */
public class StudyProApplication extends Application {
    protected StudyProComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
        //only use Stetho in debug
            Stetho.initialize(Stetho.newInitializerBuilder(this)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
            .build());
        }

        component = DaggerStudyProComponent.builder()
                .configurationModule(new ConfigurationModule(this))
                .singletonModule(new SingletonModule(this)).build();
    }

    /**
     * returns the dagger injection component
     * @return
     */
    public StudyProComponent getComponent(){
        return component;
    }


}

