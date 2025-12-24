package com.example.movieapp.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit client setup for TMDB API
 */
public class ApiClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    
    /**
     * Get Retrofit instance (singleton pattern)
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
    
    /**
     * Get TMDB API service instance
     */
    public static TmdbApiService getApiService() {
        return getRetrofitInstance().create(TmdbApiService.class);
    }
}

