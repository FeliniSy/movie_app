package com.example.movieapp.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Data Access Object for User entity
 * Provides methods to interact with user data in the database
 */
@Dao
public interface UserDao {
    
    /**
     * Insert a new user
     */
    @Insert
    long insertUser(User user);
    
    /**
     * Get user by email
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
    
    /**
     * Get user by ID
     */
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);
    
    /**
     * Check if email already exists
     */
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int emailExists(String email);
    
    /**
     * Update user information
     */
    @Update
    void updateUser(User user);
}

