package com.example.movieapp.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.movieapp.BuildConfig;
import com.example.movieapp.data.api.ApiClient;
import com.example.movieapp.data.api.TmdbApiService;
import com.example.movieapp.data.local.AppDatabase;
import com.example.movieapp.data.local.UserRating;
import com.example.movieapp.data.local.UserRatingDao;
import com.example.movieapp.data.models.Genre;
import com.example.movieapp.data.models.GenreResponse;
import com.example.movieapp.data.models.Movie;
import com.example.movieapp.data.models.MovieDetails;
import com.example.movieapp.data.models.MovieResponse;
import com.example.movieapp.utils.NetworkUtils;
import com.example.movieapp.utils.SharedPrefsHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for movie data operations
 * Handles API calls and local database operations
 */
public class MovieRepository {
    private TmdbApiService apiService;
    private UserRatingDao userRatingDao;
    private SharedPrefsHelper sharedPrefsHelper;
    private String apiKey;
    
    public MovieRepository(Context context) {
        apiService = ApiClient.getApiService();
        AppDatabase database = AppDatabase.getInstance(context);
        userRatingDao = database.userRatingDao();
        sharedPrefsHelper = SharedPrefsHelper.getInstance(context);
//        apiKey = BuildConfig.TMDB_API_KEY;
        apiKey = "f8f2226c42d08ad9c4519618b6304b92";
        
        // Validate API key
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("\"\"")) {
            android.util.Log.e("MovieRepository", "TMDB API key is missing! Please add it to local.properties and sync Gradle");
            android.util.Log.e("MovieRepository", "Current API key value: " + apiKey);
        } else {
            android.util.Log.d("MovieRepository", "API key loaded successfully");
        }
    }
    
    /**
     * Get popular movies
     */
    public void getPopularMovies(int page, MovieCallback callback) {
        Log.d("API_KEY_CHECK", "Using API key: " + apiKey);

        if (apiKey == null || apiKey.trim().isEmpty()) {
            Log.e("API_KEY_CHECK", "API KEY IS EMPTY!!!");
            callback.onError("API key is missing");
            return;
        }
        
        if (!NetworkUtils.isNetworkAvailable(callback.getContext())) {
            callback.onError("No internet connection");
            return;
        }
        
        Call<MovieResponse> call = apiService.getPopularMovies(apiKey, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResults());
                } else {
                    callback.onError("Failed to load movies");
                }
            }
            
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Get top rated movies
     */
    public void getTopRatedMovies(int page, MovieCallback callback) {
        if (apiKey == null || apiKey.isEmpty()) {
            callback.onError("API key is missing. Please configure TMDB_API_KEY in local.properties");
            return;
        }
        
        if (!NetworkUtils.isNetworkAvailable(callback.getContext())) {
            callback.onError("No internet connection");
            return;
        }
        
        Call<MovieResponse> call = apiService.getTopRatedMovies(apiKey, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResults());
                } else {
                    callback.onError("Failed to load movies");
                }
            }
            
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Get upcoming movies
     */
    public void getUpcomingMovies(int page, MovieCallback callback) {
        if (apiKey == null || apiKey.isEmpty()) {
            callback.onError("API key is missing. Please configure TMDB_API_KEY in local.properties");
            return;
        }
        
        if (!NetworkUtils.isNetworkAvailable(callback.getContext())) {
            callback.onError("No internet connection");
            return;
        }
        
        Call<MovieResponse> call = apiService.getUpcomingMovies(apiKey, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResults());
                } else {
                    callback.onError("Failed to load movies");
                }
            }
            
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Get movie genres
     */
    public void getGenres(GenreCallback callback) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("\"\"")) {
            callback.onError("API key is missing. Please configure TMDB_API_KEY in local.properties and sync Gradle");
            return;
        }
        
        if (!NetworkUtils.isNetworkAvailable(callback.getContext())) {
            callback.onError("No internet connection");
            return;
        }
        
        Call<GenreResponse> call = apiService.getGenres(apiKey);
        call.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getGenres());
                } else {
                    callback.onError("Failed to load genres");
                }
            }
            
            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Search movies
     */
    public void searchMovies(String query, int page, MovieCallback callback) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("\"\"")) {
            callback.onError("API key is missing. Please configure TMDB_API_KEY in local.properties and sync Gradle");
            return;
        }
        
        if (!NetworkUtils.isNetworkAvailable(callback.getContext())) {
            callback.onError("No internet connection");
            return;
        }
        
        Call<MovieResponse> call = apiService.searchMovies(apiKey, query, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResults());
                } else {
                    callback.onError("Failed to search movies");
                }
            }
            
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Get movies by genre
     */
    public void getMoviesByGenre(int genreId, int page, MovieCallback callback) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("\"\"")) {
            callback.onError("API key is missing. Please configure TMDB_API_KEY in local.properties and sync Gradle");
            return;
        }
        
        if (!NetworkUtils.isNetworkAvailable(callback.getContext())) {
            callback.onError("No internet connection");
            return;
        }
        
        Call<MovieResponse> call = apiService.getMoviesByGenre(apiKey, genreId, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResults());
                } else {
                    callback.onError("Failed to load movies");
                }
            }
            
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Get movie details
     */
    public void getMovieDetails(int movieId, MovieDetailsCallback callback) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("\"\"")) {
            callback.onError("API key is missing. Please configure TMDB_API_KEY in local.properties and sync Gradle");
            return;
        }
        
        if (!NetworkUtils.isNetworkAvailable(callback.getContext())) {
            callback.onError("No internet connection");
            return;
        }
        
        Call<MovieDetails> call = apiService.getMovieDetails(movieId, apiKey);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to load movie details");
                }
            }
            
            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    /**
     * Save user rating for a movie
     */
    public void saveUserRating(int movieId, float rating) {
        int userId = sharedPrefsHelper.getUserId();
        if (userId > 0) {
            UserRating userRating = new UserRating(userId, movieId, rating);
            userRatingDao.insertOrUpdateRating(userRating);
        }
    }
    
    /**
     * Get user rating for a movie
     */
    public UserRating getUserRating(int movieId) {
        int userId = sharedPrefsHelper.getUserId();
        if (userId > 0) {
            return userRatingDao.getUserRating(userId, movieId);
        }
        return null;
    }
    
    /**
     * Get average rating for a movie (combining API and user ratings)
     */
    public float getAverageRating(int movieId, double apiRating) {
        Float userAvgRating = userRatingDao.getAverageRating(movieId);
        if (userAvgRating != null && userAvgRating > 0) {
            // Combine API rating and user ratings
            return (float) ((apiRating + userAvgRating) / 2.0);
        }
        return (float) apiRating;
    }
    
    /**
     * Get count of user ratings
     */
    public int getUserRatingCount() {
        int userId = sharedPrefsHelper.getUserId();
        if (userId > 0) {
            return userRatingDao.getUserRatingCount(userId);
        }
        return 0;
    }
    
    /**
     * Callback interface for movie list operations
     */
    public interface MovieCallback {
        void onSuccess(List<Movie> movies);
        void onError(String error);
        Context getContext();
    }
    
    /**
     * Callback interface for genre operations
     */
    public interface GenreCallback {
        void onSuccess(List<Genre> genres);
        void onError(String error);
        Context getContext();
    }
    
    /**
     * Callback interface for movie details
     */
    public interface MovieDetailsCallback {
        void onSuccess(MovieDetails movieDetails);
        void onError(String error);
        Context getContext();
    }
}

