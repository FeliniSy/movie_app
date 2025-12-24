package com.example.movieapp.ui.details;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.data.local.UserRating;
import com.example.movieapp.data.models.Genre;
import com.example.movieapp.data.models.MovieDetails;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.viewmodel.MovieDetailsViewModel;

import java.util.List;

/**
 * Movie Details Activity
 * Displays full movie information and allows user rating
 */
public class MovieDetailsActivity extends AppCompatActivity {
    private MovieDetailsViewModel viewModel;
    private ImageView imageViewPoster;
    private ImageView imageViewBackdrop;
    private TextView textViewTitle;
    private TextView textViewReleaseDate;
    private TextView textViewRating;
    private TextView textViewOverview;
    private TextView textViewGenres;
    private TextView textViewRuntime;
    private TextView textViewLanguage;
    private RatingBar ratingBarUserRating;
    private ProgressBar progressBar;
    private TextView textViewError;
    
    private int movieId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        
        movieId = getIntent().getIntExtra(Constants.EXTRA_MOVIE_ID, -1);
        if (movieId == -1) {
            Toast.makeText(this, "Invalid movie", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        setupViewModel();
    }
    
    private void initViews() {
        imageViewPoster = findViewById(R.id.imageViewPoster);
        imageViewBackdrop = findViewById(R.id.imageViewBackdrop);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewRating = findViewById(R.id.textViewRating);
        textViewOverview = findViewById(R.id.textViewOverview);
        textViewGenres = findViewById(R.id.textViewGenres);
        textViewRuntime = findViewById(R.id.textViewRuntime);
        textViewLanguage = findViewById(R.id.textViewLanguage);
        ratingBarUserRating = findViewById(R.id.ratingBarUserRating);
        progressBar = findViewById(R.id.progressBar);
        textViewError = findViewById(R.id.textViewError);
        
        // Setup rating bar
        ratingBarUserRating.setNumStars(5);
        ratingBarUserRating.setStepSize(1.0f);
        ratingBarUserRating.setMax(5);
        
        ratingBarUserRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                // Convert 1-5 stars to 1.0-5.0 float
                viewModel.saveRating(movieId, rating);
                Toast.makeText(this, "Rating saved!", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Back button
        findViewById(R.id.imageViewBack).setOnClickListener(v -> finish());
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
        
        // Observe movie details
        viewModel.getMovieDetailsLiveData().observe(this, movieDetails -> {
            if (movieDetails != null) {
                displayMovieDetails(movieDetails);
            }
        });
        
        // Observe user rating
        viewModel.getUserRatingLiveData().observe(this, userRating -> {
            if (userRating != null) {
                ratingBarUserRating.setRating(userRating.getRating());
            }
        });
        
        // Observe average rating
        viewModel.getAverageRatingLiveData().observe(this, avgRating -> {
            if (avgRating != null) {
                textViewRating.setText(String.format("⭐ %.1f / 10", avgRating));
            }
        });
        
        // Observe loading state
        viewModel.getIsLoadingLiveData().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        
        // Observe errors
        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                textViewError.setText(error);
                textViewError.setVisibility(View.VISIBLE);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            } else {
                textViewError.setVisibility(View.GONE);
            }
        });
        
        // Load movie details
        viewModel.loadMovieDetails(movieId);
    }
    
    private void displayMovieDetails(MovieDetails movieDetails) {
        textViewTitle.setText(movieDetails.getTitle());
        
        if (movieDetails.getReleaseDate() != null && !movieDetails.getReleaseDate().isEmpty()) {
            textViewReleaseDate.setText(getString(R.string.release_date) + ": " + movieDetails.getReleaseDate());
        } else {
            textViewReleaseDate.setText(getString(R.string.release_date) + ": N/A");
        }
        
        textViewRating.setText(String.format("⭐ %.1f / 10", movieDetails.getVoteAverage()));
        textViewOverview.setText(movieDetails.getOverview());
        
        // Display genres
        List<Genre> genres = movieDetails.getGenres();
        if (genres != null && !genres.isEmpty()) {
            StringBuilder genresText = new StringBuilder();
            for (int i = 0; i < genres.size(); i++) {
                genresText.append(genres.get(i).getName());
                if (i < genres.size() - 1) {
                    genresText.append(", ");
                }
            }
            textViewGenres.setText(getString(R.string.genre) + ": " + genresText.toString());
        } else {
            textViewGenres.setText(getString(R.string.genre) + ": N/A");
        }
        
        // Display runtime
        if (movieDetails.getRuntime() != null) {
            textViewRuntime.setText(getString(R.string.runtime) + ": " + movieDetails.getFormattedRuntime());
        } else {
            textViewRuntime.setText(getString(R.string.runtime) + ": N/A");
        }
        
        // Display language
        if (movieDetails.getOriginalLanguage() != null && !movieDetails.getOriginalLanguage().isEmpty()) {
            textViewLanguage.setText(getString(R.string.language) + ": " + movieDetails.getOriginalLanguage().toUpperCase());
        } else {
            textViewLanguage.setText(getString(R.string.language) + ": N/A");
        }
        
        // Load images
        String posterUrl = movieDetails.getFullPosterUrl();
        if (posterUrl != null) {
            Glide.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageViewPoster);
        }
        
        String backdropUrl = movieDetails.getFullBackdropUrl();
        if (backdropUrl != null) {
            Glide.with(this)
                .load(backdropUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageViewBackdrop);
        }
    }
}

