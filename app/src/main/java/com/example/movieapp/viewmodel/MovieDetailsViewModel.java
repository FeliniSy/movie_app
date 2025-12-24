package com.example.movieapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.data.local.UserRating;
import com.example.movieapp.data.models.MovieDetails;
import com.example.movieapp.data.repository.MovieRepository;

/**
 * ViewModel for Movie Details
 */
public class MovieDetailsViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private MutableLiveData<MovieDetails> movieDetailsLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<UserRating> userRatingLiveData;
    private MutableLiveData<Float> averageRatingLiveData;
    
    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        movieDetailsLiveData = new MutableLiveData<>();
        isLoadingLiveData = new MutableLiveData<>(false);
        errorLiveData = new MutableLiveData<>();
        userRatingLiveData = new MutableLiveData<>();
        averageRatingLiveData = new MutableLiveData<>();
    }
    
    /**
     * Load movie details
     */
    public void loadMovieDetails(int movieId) {
        isLoadingLiveData.setValue(true);
        
        movieRepository.getMovieDetails(movieId, new MovieRepository.MovieDetailsCallback() {
            @Override
            public void onSuccess(MovieDetails movieDetails) {
                movieDetailsLiveData.setValue(movieDetails);
                isLoadingLiveData.setValue(false);
                
                // Load user rating if exists
                UserRating userRating = movieRepository.getUserRating(movieId);
                userRatingLiveData.setValue(userRating);
                
                // Calculate average rating
                float avgRating = movieRepository.getAverageRating(movieId, movieDetails.getVoteAverage());
                averageRatingLiveData.setValue(avgRating);
            }
            
            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public android.content.Context getContext() {
                return getApplication();
            }
        });
    }
    
    /**
     * Save user rating
     */
    public void saveRating(int movieId, float rating) {
        movieRepository.saveUserRating(movieId, rating);
        
        // Update user rating LiveData
        UserRating existingRating = movieRepository.getUserRating(movieId);
        if (existingRating != null) {
            userRatingLiveData.setValue(existingRating);
        } else {
            // Create new rating object (userId will be set by repository)
            UserRating newRating = new UserRating(0, movieId, rating);
            userRatingLiveData.setValue(newRating);
        }
        
        // Recalculate average rating
        MovieDetails movieDetails = movieDetailsLiveData.getValue();
        if (movieDetails != null) {
            float avgRating = movieRepository.getAverageRating(movieId, movieDetails.getVoteAverage());
            averageRatingLiveData.setValue(avgRating);
        }
    }
    
    public LiveData<MovieDetails> getMovieDetailsLiveData() {
        return movieDetailsLiveData;
    }
    
    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }
    
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
    
    public LiveData<UserRating> getUserRatingLiveData() {
        return userRatingLiveData;
    }
    
    public LiveData<Float> getAverageRatingLiveData() {
        return averageRatingLiveData;
    }
}

