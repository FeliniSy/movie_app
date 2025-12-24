package com.example.movieapp.ui.genres;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.data.models.Genre;
import com.example.movieapp.data.models.Movie;
import com.example.movieapp.ui.details.MovieDetailsActivity;
import com.example.movieapp.ui.home.MovieAdapter;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.viewmodel.GenresViewModel;

import java.util.List;

/**
 * Genres Fragment
 * Displays genres and allows filtering movies by genre
 */
public class GenresFragment extends Fragment {
    private GenresViewModel viewModel;
    private RecyclerView recyclerViewGenres;
    private RecyclerView recyclerViewMovies;
    private GenreAdapter genreAdapter;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView textViewError;
    private TextView textViewMoviesTitle;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_genres, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerViews();
        setupViewModel();
    }
    
    private void initViews(View view) {
        recyclerViewGenres = view.findViewById(R.id.recyclerViewGenres);
        recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        progressBar = view.findViewById(R.id.progressBar);
        textViewError = view.findViewById(R.id.textViewError);
        textViewMoviesTitle = view.findViewById(R.id.textViewMoviesTitle);
    }
    
    private void setupRecyclerViews() {
        if (getContext() == null) return;
        
        // Genres RecyclerView
        genreAdapter = new GenreAdapter(genre -> {
            if (textViewMoviesTitle != null && viewModel != null) {
                textViewMoviesTitle.setVisibility(View.VISIBLE);
                textViewMoviesTitle.setText("Movies: " + genre.getName());
                viewModel.loadMoviesByGenre(genre.getId(), false);
            }
        });
        
        recyclerViewGenres.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(
            getContext(),
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        ));
        recyclerViewGenres.setAdapter(genreAdapter);
        
        // Movies RecyclerView
        movieAdapter = new MovieAdapter(movie -> {
            if (getContext() != null) {
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra(Constants.EXTRA_MOVIE_ID, movie.getId());
                startActivity(intent);
            }
        });
        
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewMovies.setLayoutManager(layoutManager);
        recyclerViewMovies.setAdapter(movieAdapter);
        
        // Pagination
        recyclerViewMovies.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                        // Get selected genre and load more
                        Genre selectedGenre = genreAdapter.getSelectedGenre();
                        if (selectedGenre != null) {
                            viewModel.loadMoviesByGenre(selectedGenre.getId(), true);
                        }
                    }
                }
            }
        });
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GenresViewModel.class);
        
        // Observe genres
        viewModel.getGenresLiveData().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null && !genres.isEmpty()) {
                genreAdapter.setGenres(genres);
            }
        });
        
        // Observe genre movies
        viewModel.getGenreMoviesLiveData().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                movieAdapter.setMovies(movies);
                textViewError.setVisibility(View.GONE);
            } else {
                textViewError.setText(getString(R.string.no_results));
                textViewError.setVisibility(View.VISIBLE);
            }
        });
        
        // Observe loading state
        viewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        
        // Observe errors
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null && getContext() != null) {
                textViewError.setText(error);
                textViewError.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            } else {
                if (textViewError != null) {
                    textViewError.setVisibility(View.GONE);
                }
            }
        });
        
        // Load genres
        viewModel.loadGenres();
    }
}

