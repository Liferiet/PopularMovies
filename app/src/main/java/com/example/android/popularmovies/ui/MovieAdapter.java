package com.example.android.popularmovies.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.databinding.ListItemGridMovieBinding;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter that manages movie views in MainActivity
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private int mNumberItems;
    private ArrayList<Movie> mMovieList;
    private OnListItemClickListener mOnListItemClickListener;

    /**
     * Constructor
     * @param listener listener that handles onListItemClick events for each specific movie view
     */
    public MovieAdapter(OnListItemClickListener listener) {
        mOnListItemClickListener = listener;
        this.mNumberItems = 0;
    }

    /**
     * Creates view holders for adapter
     * Developer doesn't need to call this function
     */
    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForItem = R.layout.list_item_grid_movie;
        LayoutInflater inflater = LayoutInflater.from(context);

        ListItemGridMovieBinding binding = DataBindingUtil.inflate(
                                inflater, layoutIdForItem, parent, false);

        return new MovieViewHolder(binding);
    }

    /**
     * Interface for class that will handle click events on view holders
     */
    interface OnListItemClickListener {
        void onListItemClick(Movie movie);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public void setMovieData(@NonNull ArrayList<Movie> movieList) {
        mMovieList = movieList;
        mNumberItems = mMovieList.size();
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getMovieData() {
        return mMovieList;
    }

    /**
     * Class that holds the information for each particular element in data list
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ListItemGridMovieBinding binding;

        public MovieViewHolder(ListItemGridMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Movie movie = mMovieList.get(position);
            binding.titleMovieTv.setText(movie.getTitle());

            Picasso.get().load(movie.getMoviePosterUri())
                    .placeholder(R.drawable.placeholder)
                    .into(binding.imageMovieIv);
        }

        @Override
        public void onClick(View view) {
            mOnListItemClickListener.onListItemClick(mMovieList.get(getAdapterPosition()));
        }
    }
}
