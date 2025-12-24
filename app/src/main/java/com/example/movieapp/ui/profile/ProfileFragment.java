package com.example.movieapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieapp.R;
import com.example.movieapp.data.local.User;
import com.example.movieapp.ui.SplashActivity;
import com.example.movieapp.viewmodel.ProfileViewModel;

/**
 * Profile Fragment
 * Displays user information and logout option
 */
public class ProfileFragment extends Fragment {
    private ProfileViewModel viewModel;
    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewRatedMovies;
    private Button buttonLogout;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupViewModel();
    }
    
    private void initViews(View view) {
        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewRatedMovies = view.findViewById(R.id.textViewRatedMovies);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        
        buttonLogout.setOnClickListener(v -> {
            viewModel.logout();
            // Navigate to SplashActivity which will redirect to LoginActivity
            Intent intent = new Intent(getContext(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        
        // Observe user data
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                textViewName.setText(user.getName());
                textViewEmail.setText(user.getEmail());
            } else {
                textViewName.setText("N/A");
                textViewEmail.setText("N/A");
            }
        });
        
        // Observe rated movies count
        viewModel.getRatedMoviesCountLiveData().observe(getViewLifecycleOwner(), count -> {
            textViewRatedMovies.setText(getString(R.string.rated_movies) + ": " + count);
        });
        
        // Load user profile
        viewModel.loadUserProfile();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment is resumed
        viewModel.loadUserProfile();
    }
}

