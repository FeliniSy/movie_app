package com.example.movieapp.data.local;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * UserRating entity for Room database
 * Stores user ratings for movies (1-5 stars)
 */
@Entity(
    tableName = "user_ratings",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("userId"), @Index("movieId")}
)
public class UserRating {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int userId;
    private int movieId;
    private float rating; // 1.0 to 5.0 stars
    private long timestamp; // When the rating was created/updated
    
    public UserRating() {
    }
    
    public UserRating(int userId, int movieId, float rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = System.currentTimeMillis();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getMovieId() {
        return movieId;
    }
    
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    
    public float getRating() {
        return rating;
    }
    
    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

