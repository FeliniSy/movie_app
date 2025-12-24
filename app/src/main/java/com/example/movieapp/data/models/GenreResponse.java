package com.example.movieapp.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response wrapper for genre list API call
 */
public class GenreResponse {
    @SerializedName("genres")
    private List<Genre> genres;
    
    public GenreResponse() {
    }
    
    public List<Genre> getGenres() {
        return genres;
    }
    
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}

