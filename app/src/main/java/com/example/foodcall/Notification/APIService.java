package com.example.foodcall.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-type:application/json",
            "Authorization:key=AAAA1POMuGg:APA91bGrcQ72R4w2xaLiPwVn0HXy-O5oi5uZy3B4MhRmIwsLPPq-xAdk7UNjJEMm_EUiVNMgCuElrt-lMYPKo1DZ9TL28fs39z2eTCxzYuqCK72fUSHH-40TE-MhRiW7j0XdLc7SWx5k"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
