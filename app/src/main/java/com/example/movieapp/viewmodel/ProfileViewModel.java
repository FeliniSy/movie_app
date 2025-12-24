package com.example.movieapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.data.local.User;
import com.example.movieapp.data.repository.AuthRepository;
import com.example.movieapp.data.repository.MovieRepository;

/**
 * ViewModel for Profile Fragment
 */
public class ProfileViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MovieRepository movieRepository;
    private MutableLiveData<User> userLiveData;
    private MutableLiveData<Integer> ratedMoviesCountLiveData;
    
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        movieRepository = new MovieRepository(application);
        userLiveData = new MutableLiveData<>();
        ratedMoviesCountLiveData = new MutableLiveData<>(0);
    }
    
    /**
     * Load user profile data
     */
    public void loadUserProfile() {
        try {
            User user = authRepository.getCurrentUser();
            userLiveData.setValue(user);
            
            int ratedCount = movieRepository.getUserRatingCount();
            ratedMoviesCountLiveData.setValue(ratedCount);
        } catch (Exception e) {
            e.printStackTrace();
            userLiveData.setValue(null);
            ratedMoviesCountLiveData.setValue(0);
        }
    }
    
    /**
     * Logout user
     */
    public void logout() {
        authRepository.logout();
    }
    
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
    
    public LiveData<Integer> getRatedMoviesCountLiveData() {
        return ratedMoviesCountLiveData;
    }
}

