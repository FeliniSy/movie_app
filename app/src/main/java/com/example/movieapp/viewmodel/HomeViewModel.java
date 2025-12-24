package com.example.movieapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.data.models.Movie;
import com.example.movieapp.data.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for Home Fragment
 */
public class HomeViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private MutableLiveData<List<Movie>> popularMoviesLiveData;
    private MutableLiveData<List<Movie>> topRatedMoviesLiveData;
    private MutableLiveData<List<Movie>> upcomingMoviesLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    
    private int currentPopularPage = 1;
    private int currentTopRatedPage = 1;
    private int currentUpcomingPage = 1;
    private boolean isLoadingPopular = false;
    private boolean isLoadingTopRated = false;
    private boolean isLoadingUpcoming = false;
    
    public HomeViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        popularMoviesLiveData = new MutableLiveData<>(new ArrayList<>());
        topRatedMoviesLiveData = new MutableLiveData<>(new ArrayList<>());
        upcomingMoviesLiveData = new MutableLiveData<>(new ArrayList<>());
        isLoadingLiveData = new MutableLiveData<>(false);
        errorLiveData = new MutableLiveData<>();
    }
    
    /**
     * Load popular movies
     */
    public void loadPopularMovies(boolean loadMore) {
        if (isLoadingPopular) return;
        
        if (!loadMore) {
            currentPopularPage = 1;
            popularMoviesLiveData.setValue(new ArrayList<>());
        }
        
        isLoadingPopular = true;
        isLoadingLiveData.setValue(true);
        
        movieRepository.getPopularMovies(currentPopularPage, new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                List<Movie> currentMovies = popularMoviesLiveData.getValue();
                if (currentMovies == null) {
                    currentMovies = new ArrayList<>();
                }
                if (loadMore) {
                    currentMovies.addAll(movies);
                } else {
                    currentMovies = movies;
                }
                popularMoviesLiveData.setValue(currentMovies);
                currentPopularPage++;
                isLoadingPopular = false;
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
                isLoadingPopular = false;
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public android.content.Context getContext() {
                return getApplication();
            }
        });
    }
    
    /**
     * Load top rated movies
     */
    public void loadTopRatedMovies(boolean loadMore) {
        if (isLoadingTopRated) return;
        
        if (!loadMore) {
            currentTopRatedPage = 1;
            topRatedMoviesLiveData.setValue(new ArrayList<>());
        }
        
        isLoadingTopRated = true;
        isLoadingLiveData.setValue(true);
        
        movieRepository.getTopRatedMovies(currentTopRatedPage, new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                List<Movie> currentMovies = topRatedMoviesLiveData.getValue();
                if (currentMovies == null) {
                    currentMovies = new ArrayList<>();
                }
                if (loadMore) {
                    currentMovies.addAll(movies);
                } else {
                    currentMovies = movies;
                }
                topRatedMoviesLiveData.setValue(currentMovies);
                currentTopRatedPage++;
                isLoadingTopRated = false;
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
                isLoadingTopRated = false;
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public android.content.Context getContext() {
                return getApplication();
            }
        });
    }
    
    /**
     * Load upcoming movies
     */
    public void loadUpcomingMovies(boolean loadMore) {
        if (isLoadingUpcoming) return;
        
        if (!loadMore) {
            currentUpcomingPage = 1;
            upcomingMoviesLiveData.setValue(new ArrayList<>());
        }
        
        isLoadingUpcoming = true;
        isLoadingLiveData.setValue(true);
        
        movieRepository.getUpcomingMovies(currentUpcomingPage, new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                List<Movie> currentMovies = upcomingMoviesLiveData.getValue();
                if (currentMovies == null) {
                    currentMovies = new ArrayList<>();
                }
                if (loadMore) {
                    currentMovies.addAll(movies);
                } else {
                    currentMovies = movies;
                }
                upcomingMoviesLiveData.setValue(currentMovies);
                currentUpcomingPage++;
                isLoadingUpcoming = false;
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
                isLoadingUpcoming = false;
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public android.content.Context getContext() {
                return getApplication();
            }
        });
    }
    
    public LiveData<List<Movie>> getPopularMoviesLiveData() {
        return popularMoviesLiveData;
    }
    
    public LiveData<List<Movie>> getTopRatedMoviesLiveData() {
        return topRatedMoviesLiveData;
    }
    
    public LiveData<List<Movie>> getUpcomingMoviesLiveData() {
        return upcomingMoviesLiveData;
    }
    
    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }
    
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}

