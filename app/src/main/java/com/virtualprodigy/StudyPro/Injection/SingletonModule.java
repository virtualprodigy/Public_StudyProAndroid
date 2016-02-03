package com.virtualprodigy.studypro.Injection;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.virtualprodigy.studypro.BuildConfig;
import com.virtualprodigy.studypro.Database.OrmHelper;
import com.virtualprodigy.studypro.StudyProApplication;
import com.virtualprodigy.studypro.StudyTimer.TimedBreaks;
import com.virtualprodigy.studypro.services.RetrofitService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by virtualprodigyllc on 02/01/16.
 */
@Module
public class SingletonModule {
    protected Context context;
    protected StudyProApplication application;

    public SingletonModule(StudyProApplication application) {
        this.context = application;
        this.application = application;
    }

    @Provides
    @Singleton
    OrmHelper provideOrmHelper(){
        return new OrmHelper(context);
    }

    @Provides
    @Singleton
    Gson provideGson(){
        //Google library that converts a class to json
        GsonBuilder gsonBuilder = new GsonBuilder();
        return  gsonBuilder.create();
    }

    //networking stuff / db inspection

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(){
        //Parse will probably want headers later on, here I can add an intercptor and inject the headers to every network call
        //make sure I @provide inceptor
        return  new OkHttpClient();
    }

    @Provides
    @Singleton
    RetrofitService provideRetroFitService (OkHttpClient okHttpClient, Gson gson){
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.BASIC)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(gson))
                .setEndpoint("");
        return builder.build().create(RetrofitService.class);

    }

    @Provides
    @Singleton
    TimedBreaks provideTimedBreaks(Context context){
        return  new TimedBreaks(context);
    }

}
