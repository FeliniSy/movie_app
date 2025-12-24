package com.example.movieapp.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.movieapp.ui.home.MovieAdapter;
import com.example.movieapp.utils.Constants;
import com.example.movieapp.viewmodel.SearchViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Search Fragment
 * Allows users to search for movies
 */
public class SearchFragment extends Fragment {
    private SearchViewModel viewModel;
    private EditText editTextSearch;
    private RecyclerView recyclerViewResults;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView textViewError;
    private TextView textViewEmpty;
    
    private Timer searchTimer;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerView();
        setupViewModel();
        setupSearchListener();
    }
    
    private void initViews(View view) {
        editTextSearch = view.findViewById(R.id.editTextSearch);
        recyclerViewResults = view.findViewById(R.id.recyclerViewResults);
        progressBar = view.findViewById(R.id.progressBar);
        textViewError = view.findViewById(R.id.textViewError);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
    }
    
    private void setupRecyclerView() {
        if (getContext() == null) return;
        
        movieAdapter = new MovieAdapter(movie -> {
            if (getContext() != null) {
                Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
                intent.putExtra(Constants.EXTRA_MOVIE_ID, movie.getId());
                startActivity(intent);
            }
        });
        
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewResults.setLayoutManager(layoutManager);
        recyclerViewResults.setAdapter(movieAdapter);
        
        // Pagination
        recyclerViewResults.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
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
                        String query = editTextSearch.getText().toString();
                        if (!query.trim().isEmpty()) {
                            viewModel.searchMovies(query, true);
                        }
                    }
                }
            }
        });
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        
        // Observe search results
        viewModel.getSearchResultsLiveData().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                movieAdapter.setMovies(movies);
                textViewError.setVisibility(View.GONE);
                textViewEmpty.setVisibility(View.GONE);
            } else {
                String query = editTextSearch.getText().toString();
                if (query.trim().isEmpty()) {
                    textViewEmpty.setText("Enter a movie name to search");
                    textViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    textViewEmpty.setText(getString(R.string.no_results));
                    textViewEmpty.setVisibility(View.VISIBLE);
                }
                textViewError.setVisibility(View.GONE);
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
    
    private void setupSearchListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                // Cancel previous timer
                if (searchTimer != null) {
                    searchTimer.cancel();
                }
                
                // Debounce search - wait 500ms after user stops typing
                searchTimer = new Timer();
                searchTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                String query = s.toString();
                                if (query.trim().isEmpty()) {
                                    movieAdapter.setMovies(new java.util.ArrayList<>());
                                    textViewEmpty.setText("Enter a movie name to search");
                                    textViewEmpty.setVisibility(View.VISIBLE);
                                } else {
                                    viewModel.searchMovies(query, false);
                                }
                            });
                        }
                    }
                }, 500);
            }
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchTimer != null) {
            searchTimer.cancel();
        }
    }
}

