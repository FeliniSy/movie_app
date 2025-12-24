package com.example.movieapp.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Movie model representing a movie from TMDB API
 */
public class Movie {
    @SerializedName("id")
    private int id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("overview")
    private String overview;
    
    @SerializedName("poster_path")
    private String posterPath;
    
    @SerializedName("backdrop_path")
    private String backdropPath;
    
    @SerializedName("release_date")
    private String releaseDate;
    
    @SerializedName("vote_average")
    private double voteAverage;
    
    @SerializedName("vote_count")
    private int voteCount;
    
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    
    @SerializedName("popularity")
    private double popularity;
    
    public Movie() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getOverview() {
        return overview;
    }
    
    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public String getPosterPath() {
        return posterPath;
    }
    
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    
    public String getBackdropPath() {
        return backdropPath;
    }
    
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
    
    public String getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public double getVoteAverage() {
        return voteAverage;
    }
    
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
    
    public int getVoteCount() {
        return voteCount;
    }
    
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
    
    public List<Integer> getGenreIds() {
        return genreIds;
    }
    
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
    
    public double getPopularity() {
        return popularity;
    }
    
    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
    
    /**
     * Get full poster URL
     */
    public String getFullPosterUrl() {
        if (posterPath == null || posterPath.isEmpty()) {
            return null;
        }
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }
    
    /**
     * Get full backdrop URL
     */
    public String getFullBackdropUrl() {
        if (backdropPath == null || backdropPath.isEmpty()) {
            return null;
        }
        return "https://image.tmdb.org/t/p/w1280" + backdropPath;
    }
}

