package com.example.movieapp.ui.home;

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
import com.example.movieapp.data.models.Movie;
import com.example.movieapp.ui.details.MovieDetailsActivity;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.viewmodel.HomeViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * Home Fragment
 * Displays Popular, Top Rated, and Upcoming movies
 */
public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private RecyclerView recyclerViewMovies;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView textViewError;
    private TabLayout tabLayout;
    
    private String currentTab = Constants.MOVIE_TYPE_POPULAR;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerView();
        setupViewModel();
        setupTabs();
    }
    
    private void initViews(View view) {
        recyclerViewMovies = view.findViewById(R.id.recyclerViewMovies);
        progressBar = view.findViewById(R.id.progressBar);
        textViewError = view.findViewById(R.id.textViewError);
        tabLayout = view.findViewById(R.id.tabLayout);
    }
    
    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(movie -> {
            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_MOVIE_ID, movie.getId());
            startActivity(intent);
        });
        
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewMovies.setLayoutManager(layoutManager);
        recyclerViewMovies.setAdapter(movieAdapter);
        
        // Pagination: Load more when scrolling to bottom
        recyclerViewMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                        // Load more movies
                        loadMoreMovies();
                    }
                }
            }
        });
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        // Observe popular movies
        viewModel.getPopularMoviesLiveData().observe(getViewLifecycleOwner(), movies -> {
            if (currentTab.equals(Constants.MOVIE_TYPE_POPULAR)) {
                updateMoviesList(movies);
            }
        });
        
        // Observe top rated movies
        viewModel.getTopRatedMoviesLiveData().observe(getViewLifecycleOwner(), movies -> {
            if (currentTab.equals(Constants.MOVIE_TYPE_TOP_RATED)) {
                updateMoviesList(movies);
            }
        });
        
        // Observe upcoming movies
        viewModel.getUpcomingMoviesLiveData().observe(getViewLifecycleOwner(), movies -> {
            if (currentTab.equals(Constants.MOVIE_TYPE_UPCOMING)) {
                updateMoviesList(movies);
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
    }
    
    private void setupTabs() {
        if (getContext() == null || tabLayout == null || viewModel == null) {
            return;
        }
        
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.popular_movies)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.top_rated)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.upcoming)));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (viewModel == null) return;
                switch (tab.getPosition()) {
                    case 0:
                        currentTab = Constants.MOVIE_TYPE_POPULAR;
                        viewModel.loadPopularMovies(false);
                        break;
                    case 1:
                        currentTab = Constants.MOVIE_TYPE_TOP_RATED;
                        viewModel.loadTopRatedMovies(false);
                        break;
                    case 2:
                        currentTab = Constants.MOVIE_TYPE_UPCOMING;
                        viewModel.loadUpcomingMovies(false);
                        break;
                }
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        
        // Load popular movies by default
        viewModel.loadPopularMovies(false);
    }
    
    private void updateMoviesList(List<Movie> movies) {
        if (getContext() == null || movieAdapter == null || textViewError == null) {
            return;
        }
        
        if (movies != null && !movies.isEmpty()) {
            movieAdapter.setMovies(movies);
            textViewError.setVisibility(View.GONE);
        } else {
            textViewError.setText(getString(R.string.no_results));
            textViewError.setVisibility(View.VISIBLE);
        }
    }
    
    private void loadMoreMovies() {
        switch (currentTab) {
            case Constants.MOVIE_TYPE_POPULAR:
                viewModel.loadPopularMovies(true);
                break;
            case Constants.MOVIE_TYPE_TOP_RATED:
                viewModel.loadTopRatedMovies(true);
                break;
            case Constants.MOVIE_TYPE_UPCOMING:
                viewModel.loadUpcomingMovies(true);
                break;
        }
    }
}

