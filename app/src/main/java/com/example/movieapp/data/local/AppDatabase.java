package com.example.movieapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room Database class
 * Main database instance for the application
 */
@Database(
    entities = {User.class, UserRating.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase instance;
    private static final String DATABASE_NAME = "movie_app_database";
    
    public abstract UserDao userDao();
    public abstract UserRatingDao userRatingDao();
    
    /**
     * Get singleton instance of the database
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                DATABASE_NAME
            )
//            .allowMainThreadQueries() // Allow main thread queries for simplicity (development only)
            .fallbackToDestructiveMigration() // For development - remove in production
            .build();
        }
        return instance;
    }
}

