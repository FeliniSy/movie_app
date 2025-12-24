package com.example.movieapp.utils;

/**
 * Constants used throughout the application
 */
public class Constants {
    
    // SharedPreferences keys
    public static final String PREF_NAME = "MovieAppPrefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    
    // API Configuration
    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String TMDB_POSTER_SIZE = "w500";
    public static final String TMDB_BACKDROP_SIZE = "w1280";
    
    // Movie list types
    public static final String MOVIE_TYPE_POPULAR = "popular";
    public static final String MOVIE_TYPE_TOP_RATED = "top_rated";
    public static final String MOVIE_TYPE_UPCOMING = "upcoming";
    
    // Pagination
    public static final int INITIAL_PAGE = 1;
    public static final int PAGE_SIZE = 20;
    
    // Rating
    public static final float MIN_RATING = 1.0f;
    public static final float MAX_RATING = 5.0f;
    
    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    
    // Intent extras
    public static final String EXTRA_MOVIE_ID = "movie_id";
    public static final String EXTRA_GENRE_ID = "genre_id";
    public static final String EXTRA_GENRE_NAME = "genre_name";
}

