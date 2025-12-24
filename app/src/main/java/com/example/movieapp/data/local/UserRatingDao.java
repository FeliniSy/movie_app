package com.example.movieapp.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for UserRating entity
 * Provides methods to interact with user ratings in the database
 */
@Dao
public interface UserRatingDao {
    
    /**
     * Insert or update a user rating
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrUpdateRating(UserRating rating);
    
    /**
     * Get rating for a specific movie by a specific user
     */
    @Query("SELECT * FROM user_ratings WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    UserRating getUserRating(int userId, int movieId);
    
    /**
     * Get all ratings for a specific user
     */
    @Query("SELECT * FROM user_ratings WHERE userId = :userId ORDER BY timestamp DESC")
    List<UserRating> getUserRatings(int userId);
    
    /**
     * Get all ratings for a specific movie
     */
    @Query("SELECT * FROM user_ratings WHERE movieId = :movieId")
    List<UserRating> getMovieRatings(int movieId);
    
    /**
     * Calculate average rating for a movie
     */
    @Query("SELECT AVG(rating) FROM user_ratings WHERE movieId = :movieId")
    Float getAverageRating(int movieId);
    
    /**
     * Get count of ratings for a user
     */
    @Query("SELECT COUNT(*) FROM user_ratings WHERE userId = :userId")
    int getUserRatingCount(int userId);
    
    /**
     * Update a rating
     */
    @Update
    void updateRating(UserRating rating);
    
    /**
     * Delete a rating
     */
    @Query("DELETE FROM user_ratings WHERE userId = :userId AND movieId = :movieId")
    void deleteRating(int userId, int movieId);
}

