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
 * ViewModel for Search Fragment
 */
public class SearchViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private MutableLiveData<List<Movie>> searchResultsLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    
    private int currentPage = 1;
    private boolean isLoading = false;
    private String currentQuery = "";
    
    public SearchViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        searchResultsLiveData = new MutableLiveData<>(new ArrayList<>());
        isLoadingLiveData = new MutableLiveData<>(false);
        errorLiveData = new MutableLiveData<>();
    }
    
    /**
     * Search movies
     */
    public void searchMovies(String query, boolean loadMore) {
        if (isLoading) return;
        
        if (!loadMore || !query.equals(currentQuery)) {
            currentPage = 1;
            currentQuery = query;
            searchResultsLiveData.setValue(new ArrayList<>());
        }
        
        if (query == null || query.trim().isEmpty()) {
            searchResultsLiveData.setValue(new ArrayList<>());
            return;
        }
        
        isLoading = true;
        isLoadingLiveData.setValue(true);
        
        movieRepository.searchMovies(query.trim(), currentPage, new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                List<Movie> currentMovies = searchResultsLiveData.getValue();
                if (currentMovies == null) {
                    currentMovies = new ArrayList<>();
                }
                if (loadMore) {
                    currentMovies.addAll(movies);
                } else {
                    currentMovies = movies;
                }
                searchResultsLiveData.setValue(currentMovies);
                currentPage++;
                isLoading = false;
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public void onError(String error) {
                errorLiveData.setValue(error);
                isLoading = false;
                isLoadingLiveData.setValue(false);
            }
            
            @Override
            public android.content.Context getContext() {
                return getApplication();
            }
        });
    }
    
    public LiveData<List<Movie>> getSearchResultsLiveData() {
        return searchResultsLiveData;
    }
    
    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }
    
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}

