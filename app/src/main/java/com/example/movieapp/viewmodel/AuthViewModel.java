package com.example.movieapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.data.local.User;
import com.example.movieapp.data.repository.AuthRepository;

/**
 * ViewModel for authentication operations
 */
public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<String> messageLiveData;
    private MutableLiveData<Boolean> isSuccessLiveData;
    
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        messageLiveData = new MutableLiveData<>();
        isSuccessLiveData = new MutableLiveData<>();
    }
    
    /**
     * Register a new user
     */
    public void register(String name, String email, String password) {
        AuthRepository.RegistrationResult result = authRepository.register(name, email, password);
        messageLiveData.setValue(result.getMessage());
        isSuccessLiveData.setValue(result.isSuccess());
    }
    
    /**
     * Login user
     */
    public void login(String email, String password) {
        AuthRepository.LoginResult result = authRepository.login(email, password);
        messageLiveData.setValue(result.getMessage());
        isSuccessLiveData.setValue(result.isSuccess());
    }
    
    /**
     * Logout user
     */
    public void logout() {
        authRepository.logout();
    }
    
    /**
     * Get current user
     */
    public User getCurrentUser() {
        return authRepository.getCurrentUser();
    }
    
    /**
     * Get message LiveData
     */
    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }
    
    /**
     * Get success status LiveData
     */
    public LiveData<Boolean> getIsSuccessLiveData() {
        return isSuccessLiveData;
    }
}

