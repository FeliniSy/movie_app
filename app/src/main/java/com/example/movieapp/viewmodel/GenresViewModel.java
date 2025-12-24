package com.example.movieapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.data.models.Genre;
import com.example.movieapp.data.models.Movie;
import com.example.movieapp.data.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for Genres Fragment
 */
public class GenresViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private MutableLiveData<List<Genre>> genresLiveData;
    private MutableLiveData<List<Movie>> genreMoviesLiveData;
    private MutableLiveData<Boolean> isLoadingLiveData;
    private MutableLiveData<String> errorLiveData;
    
    private int currentPage = 1;
    private boolean isLoading = false;
    private int currentGenreId = -1;
    
    public GenresViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        genresLiveData = new MutableLiveData<>(new ArrayList<>());
        genreMoviesLiveData = new MutableLiveData<>(new ArrayList<>());
        isLoadingLiveData = new MutableLiveData<>(false);
        errorLiveData = new MutableLiveData<>();
    }
    
    /**
     * Load all genres
     */
    public void loadGenres() {
        isLoadingLiveData.setValue(true);
        
        movieRepository.getGenres(new MovieRepository.GenreCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                genresLiveData.setValue(genres);
                isLoadingLiveData.setValue(false);
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
     * Load movies by genre
     */
    public void loadMoviesByGenre(int genreId, boolean loadMore) {
        if (isLoading) return;
        
        if (!loadMore || currentGenreId != genreId) {
            currentPage = 1;
            currentGenreId = genreId;
            genreMoviesLiveData.setValue(new ArrayList<>());
        }
        
        isLoading = true;
        isLoadingLiveData.setValue(true);
        
        movieRepository.getMoviesByGenre(genreId, currentPage, new MovieRepository.MovieCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                List<Movie> currentMovies = genreMoviesLiveData.getValue();
                if (currentMovies == null) {
                    currentMovies = new ArrayList<>();
                }
                if (loadMore) {
                    currentMovies.addAll(movies);
                } else {
                    currentMovies = movies;
                }
                genreMoviesLiveData.setValue(currentMovies);
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
    
    public LiveData<List<Genre>> getGenresLiveData() {
        return genresLiveData;
    }
    
    public LiveData<List<Movie>> getGenreMoviesLiveData() {
        return genreMoviesLiveData;
    }
    
    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }
    
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}

