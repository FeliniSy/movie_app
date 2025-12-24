package com.example.movieapp.data.api;

import com.example.movieapp.data.models.GenreResponse;
import com.example.movieapp.data.models.MovieDetails;
import com.example.movieapp.data.models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for TMDB API endpoints
 */
public interface TmdbApiService {
    
    /**
     * Get popular movies
     * @param apiKey TMDB API key
     * @param page Page number for pagination
     * @return MovieResponse with list of popular movies
     */
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
        @Query("api_key") String apiKey,
        @Query("page") int page
    );
    
    /**
     * Get top rated movies
     * @param apiKey TMDB API key
     * @param page Page number for pagination
     * @return MovieResponse with list of top rated movies
     */
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
        @Query("api_key") String apiKey,
        @Query("page") int page
    );
    
    /**
     * Get upcoming movies
     * @param apiKey TMDB API key
     * @param page Page number for pagination
     * @return MovieResponse with list of upcoming movies
     */
    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(
        @Query("api_key") String apiKey,
        @Query("page") int page
    );
    
    /**
     * Get movie genres
     * @param apiKey TMDB API key
     * @return GenreResponse with list of genres
     */
    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
        @Query("api_key") String apiKey
    );
    
    /**
     * Search movies by query
     * @param apiKey TMDB API key
     * @param query Search query
     * @param page Page number for pagination
     * @return MovieResponse with search results
     */
    @GET("search/movie")
    Call<MovieResponse> searchMovies(
        @Query("api_key") String apiKey,
        @Query("query") String query,
        @Query("page") int page
    );
    
    /**
     * Get movies by genre
     * @param apiKey TMDB API key
     * @param genreId Genre ID to filter by
     * @param page Page number for pagination
     * @return MovieResponse with filtered movies
     */
    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(
        @Query("api_key") String apiKey,
        @Query("with_genres") int genreId,
        @Query("page") int page
    );
    
    /**
     * Get movie details by ID
     * @param movieId Movie ID
     * @param apiKey TMDB API key
     * @return MovieDetails with full movie information
     */
    @GET("movie/{movie_id}")
    Call<MovieDetails> getMovieDetails(
        @Path("movie_id") int movieId,
        @Query("api_key") String apiKey
    );
}

