package com.example.movieapp.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.data.models.Movie;

/**
 * ViewHolder for movie items in RecyclerView
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    
    public MovieViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.cardView);
        imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewRating = itemView.findViewById(R.id.textViewRating);
        textViewReleaseDate = itemView.findViewById(R.id.textViewReleaseDate);
    }
    
    public void bind(Movie movie, MovieAdapter.OnMovieClickListener listener) {
        textViewTitle.setText(movie.getTitle());
        textViewRating.setText(String.format("⭐ %.1f", movie.getVoteAverage()));
        
        if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
            textViewReleaseDate.setText(movie.getReleaseDate());
        } else {
            textViewReleaseDate.setText("N/A");
        }
        
        // Load poster image using Glide
        String posterUrl = movie.getFullPosterUrl();
        if (posterUrl != null) {
            Glide.with(itemView.getContext())
                .load(posterUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageViewPoster);
        } else {
            imageViewPoster.setImageResource(R.drawable.ic_launcher_background);
        }
        
        // Set click listener
        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }
}

