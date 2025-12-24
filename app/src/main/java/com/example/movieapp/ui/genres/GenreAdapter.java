package com.example.movieapp.ui.genres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.data.models.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying genres in RecyclerView
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private List<Genre> genres;
    private OnGenreClickListener listener;
    private Genre selectedGenre;
    private int selectedPosition = -1;
    
    public GenreAdapter(OnGenreClickListener listener) {
        this.genres = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.bind(genre, position == selectedPosition);
        
        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;
            selectedGenre = genre;
            
            if (previousPosition != -1) {
                notifyItemChanged(previousPosition);
            }
            notifyItemChanged(selectedPosition);
            
            if (listener != null) {
                listener.onGenreClick(genre);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return genres.size();
    }
    
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        notifyDataSetChanged();
    }
    
    public Genre getSelectedGenre() {
        return selectedGenre;
    }
    
    public interface OnGenreClickListener {
        void onGenreClick(Genre genre);
    }
    
    static class GenreViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewGenreName;
        
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGenreName = itemView.findViewById(R.id.textViewGenreName);
        }
        
        public void bind(Genre genre, boolean isSelected) {
            textViewGenreName.setText(genre.getName());
            itemView.setSelected(isSelected);
            itemView.setBackgroundResource(
                isSelected ? R.drawable.genre_selected : R.drawable.genre_unselected
            );
        }
    }
}

