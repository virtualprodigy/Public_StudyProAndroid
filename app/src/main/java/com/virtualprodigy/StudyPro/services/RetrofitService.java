package com.virtualprodigy.studypro.services;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by virtualprodigyllc on 02/01/16.
 */
public interface RetrofitService {
    @POST("/route/route/route")
    void bsRoute(@Body JSONObject jsonObject, Callback callback);
}
