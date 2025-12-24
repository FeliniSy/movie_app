package com.example.movieapp.data.repository;

import android.content.Context;

import com.example.movieapp.BuildConfig;
import com.example.movieapp.data.local.AppDatabase;
import com.example.movieapp.data.local.User;
import com.example.movieapp.data.local.UserDao;
import com.example.movieapp.utils.PasswordHasher;
import com.example.movieapp.utils.SharedPrefsHelper;

/**
 * Repository for authentication operations
 */
public class AuthRepository {
    private UserDao userDao;
    private SharedPrefsHelper sharedPrefsHelper;
    
    public AuthRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        userDao = database.userDao();
        sharedPrefsHelper = SharedPrefsHelper.getInstance(context);
    }
    
    /**
     * Register a new user
     * @param name User's name
     * @param email User's email
     * @param password Plain text password
     * @return RegistrationResult with success status and message
     */
    public RegistrationResult register(String name, String email, String password) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            return new RegistrationResult(false, "Name cannot be empty");
        }
        
        if (email == null || email.trim().isEmpty() || !isValidEmail(email)) {
            return new RegistrationResult(false, "Please enter a valid email address");
        }
        
        if (password == null || password.length() < 6) {
            return new RegistrationResult(false, "Password must be at least 6 characters");
        }
        
        // Check if email already exists
        if (userDao.emailExists(email) > 0) {
            return new RegistrationResult(false, "Email already registered");
        }
        
        // Hash password and create user
        String passwordHash = PasswordHasher.hashPassword(password);
        User user = new User(name.trim(), email.trim().toLowerCase(), passwordHash);
        
        long userId = userDao.insertUser(user);
        if (userId > 0) {
            // Save session
            sharedPrefsHelper.saveUserSession((int) userId, name, email);
            return new RegistrationResult(true, "Registration successful");
        } else {
            return new RegistrationResult(false, "Registration failed. Please try again.");
        }
    }
    
    /**
     * Login user
     * @param email User's email
     * @param password Plain text password
     * @return LoginResult with success status and message
     */
    public LoginResult login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return new LoginResult(false, "Please enter your email");
        }
        
        if (password == null || password.isEmpty()) {
            return new LoginResult(false, "Please enter your password");
        }
        
        User user = userDao.getUserByEmail(email.trim().toLowerCase());
        if (user == null) {
            return new LoginResult(false, "Invalid email or password");
        }
        
        if (PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            // Save session
            sharedPrefsHelper.saveUserSession(user.getId(), user.getName(), user.getEmail());
            return new LoginResult(true, "Login successful", user);
        } else {
            return new LoginResult(false, "Invalid email or password");
        }
    }
    
    /**
     * Logout user
     */
    public void logout() {
        sharedPrefsHelper.clearUserSession();
    }
    
    /**
     * Get current logged in user
     */
    public User getCurrentUser() {
        int userId = sharedPrefsHelper.getUserId();
        if (userId > 0) {
            return userDao.getUserById(userId);
        }
        return null;
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Result class for registration
     */
    public static class RegistrationResult {
        private boolean success;
        private String message;
        
        public RegistrationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    /**
     * Result class for login
     */
    public static class LoginResult {
        private boolean success;
        private String message;
        private User user;
        
        public LoginResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public LoginResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public User getUser() {
            return user;
        }
    }
}

