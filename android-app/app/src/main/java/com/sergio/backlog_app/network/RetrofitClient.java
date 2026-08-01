package com.sergio.backlog_app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/"; // Mapping for local host in Android Emulator
    private static Retrofit retrofit = null;

    public static GameApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(GameApiService.class);
    }
}
